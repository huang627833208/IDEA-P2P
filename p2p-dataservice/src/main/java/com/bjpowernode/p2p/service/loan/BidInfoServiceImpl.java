package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.BidUser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.beans.Expression;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:BidInfoServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/8/29 15:49
 * @author:guoxin
 */
@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;


    @Override
    public Double queryAllBidMoney() {

        //操作指定key的value的操作对象
        BoundValueOperations<Object, Object> valueOperations = redisTemplate.boundValueOps(Constants.ALL_BID_MONEY);

        //首先去redis缓存中获取平台累计投资金额
        Double allBidMoney = (Double) valueOperations.get();

        //判断是否有值
        if (!ObjectUtils.allNotNull(allBidMoney)) {

            //设置同步代码块
            synchronized (this) {

                //再次从Redis缓存中查询
                allBidMoney = (Double) valueOperations.get();

                //再次进行判断
                if (!ObjectUtils.allNotNull(allBidMoney)) {

                    //去数据库查询，并存放到redis缓存中
                    allBidMoney = bidInfoMapper.selectAllBidMoney();

                    valueOperations.set(allBidMoney,15, TimeUnit.SECONDS);

                }

            }

        }

        return allBidMoney;
    }

    @Override
    public List<BidInfo> queryRecentlyBidInfoListByLoanId(Map<String, Object> paramMap) {
        return bidInfoMapper.selectRecentlyBidInfoListByLoanId(paramMap);
    }

    @Override
    public List<BidInfo> queryRecentlyBidInfoListByUid(Map<String, Object> paramMap) {
        return bidInfoMapper.selectRecentlyBidInfoListByUid(paramMap);
    }

    @Override
    public void invest(Map<String, Object> paramMap) throws Exception {
        Integer uid = (Integer) paramMap.get("uid");
        Integer loanId = (Integer) paramMap.get("loanId");
        Double bidMoney = (Double) paramMap.get("bidMoney");
        String phone = (String) paramMap.get("phone");

        //更新产品剩余可投金额，产生一种超卖现象：实际销售的数量超过库存数量
        //使用数据库乐观锁机制来解析这个问题
        //再次查询产品信息
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        paramMap.put("version",loanInfo.getVersion());
        int updateFinanceAccount = loanInfoMapper.updateLeftProductMoneyByLoanId(paramMap);

        if (updateFinanceAccount <= 0) {
            throw new Exception("用户投资：更新产品剩余可投金额失败");
        }

        //更新帐户的可用余额
        int updateFinanceAccountCount = financeAccountMapper.updateFinanceAccountByInvest(paramMap);
        if (updateFinanceAccountCount <= 0) {
            throw new Exception("用户投资：更新帐户可用余额失败");
        }

        //新增投资记录
        BidInfo bidInfo = new BidInfo();
        bidInfo.setUid(uid);
        bidInfo.setLoanId(loanId);
        bidInfo.setBidMoney(bidMoney);
        bidInfo.setBidTime(new Date());
        bidInfo.setBidStatus(1);
        int insertSelectCount = bidInfoMapper.insertSelective(bidInfo);
        if (insertSelectCount <= 0) {
            throw new Exception("用户投资：新增投资记录失败");
        }

        //再次查询产品详情
        LoanInfo loanDetail = loanInfoMapper.selectByPrimaryKey(loanId);

        //判断是否满标
        if (0 == loanDetail.getLeftProductMoney()) {

            //产品满标：更新产品的状态及产品的满标时间
            LoanInfo updateLoanInfo = new LoanInfo();
            updateLoanInfo.setId(loanId);
            updateLoanInfo.setProductStatus(1);
            updateLoanInfo.setProductFullTime(new Date());
            int i = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
            if (i <= 0) {
                throw new Exception("用户投资：更新产品的状态失败");
            }
        }

        //将用户投资的信息存放到redis缓存中
        redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,phone,bidMoney);

    }

    @Override
    public List<BidUser> queryBidUserTop() {
        List<BidUser> bidUserList = new ArrayList<BidUser>();

        //从redis中获取投资排行榜
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 5);

        //获取集合的迭代器
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();

        //循环遍历
        while (iterator.hasNext()) {

            //获取每一个用户投资
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String phone = (String) next.getValue();
            Double score = next.getScore();

            BidUser bidUser = new BidUser();
            bidUser.setPhone(phone);
            bidUser.setScore(score);

            bidUserList.add(bidUser);
        }

        return bidUserList;
    }
}
