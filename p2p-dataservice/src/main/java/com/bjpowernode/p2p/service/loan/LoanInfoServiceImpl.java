package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:LoanInfoServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/8/29 10:56
 * @author:guoxin
 */
@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Double queryHistoryAverageYearRate() {

        //将redisTemplate对象的Key的序列化方式改为StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //好处：减少对数据库的访问，减轻了数据库的压力，提升系统的性能，提升用户的体验

        //首先去redis缓存中获取历史平均年化收益率，有：直接使用，没有：去数据库查询并存放到redis缓存中

        //当前情况下，以下代码在多线程高并发的时候可以引发“缓存穿透”
        //通过双重检测+同步代码步来解决“缓存穿透”现象


        //去redis缓存中获取历史平均年化收益率
        Double historyAverageYearRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_YEAR_RATE);

        //判断该值是否为空
        if (!ObjectUtils.allNotNull(historyAverageYearRate)) {

            //设置同步代码块
            synchronized (this) {

                //再次从redis缓存中获取历史平均年化收益率
                historyAverageYearRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_YEAR_RATE);

                //再次判断该值是否为空
                if (!ObjectUtils.allNotNull(historyAverageYearRate)) {

                    //数据库查询
                    System.out.println("从数据库中获取数据。。。。。。");

                    //为空，去数据库查询
                    historyAverageYearRate = loanInfoMapper.selectHistoryAverageYearRate();

                    //并存放到redis缓存中
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_YEAR_RATE, historyAverageYearRate, 15, TimeUnit.MINUTES);
                } else {
                    System.out.println("从Redis缓存中获取数据。。。。。。");
                }

            }


        } else {
            System.out.println("从Redis缓存中获取数据。。。。。。");
        }

        return historyAverageYearRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoListByProductType(paramMap);
    }

    @Override
    public PaginationVO<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap) {
        PaginationVO<LoanInfo> paginationVO = new PaginationVO<>();

        Long total = loanInfoMapper.selectTotal(paramMap);
        paginationVO.setTotal(total);

        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoListByProductType(paramMap);
        paginationVO.setDataList(loanInfoList);


        return paginationVO;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }
}
