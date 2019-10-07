package com.bjpowernode.p2p.service.loan;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:RechargeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/9/2 15:57
 * @author:guoxin
 */
@Service("rechargeRecordServiceImpl")
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;


    @Override
    public List<RechargeRecord> queryRecentlyRechargeRecordListByUid(Map<String, Object> paramMap) {
        return rechargeRecordMapper.selectRecentlyRechargeRecordListByUid(paramMap);
    }

    @Override
    public void addRechargeRecord(RechargeRecord rechargeRecord) throws Exception {

        int insertRechargeRecordCount = rechargeRecordMapper.insertSelective(rechargeRecord);
        if (insertRechargeRecordCount <= 0) {
            throw new Exception("新增充值记录失败");
        }

    }

    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
    }

    @Override
    public void recharge(Map<String, Object> paramMap) throws Exception {

        //更新帐户可用余额
        int updateFinanceCount = financeAccountMapper.updateFinanceAccountByRecharge(paramMap);
        if (updateFinanceCount <= 0) {
            throw new Exception("用户充值：更新帐户可用余额失败");
        }

        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRechargeNo((String) paramMap.get("out_trade_no"));
        rechargeRecord.setRechargeStatus("1");
        //更新充值记录的状态
        int i = rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
        if (i <= 0) {
            throw new Exception("用户充值：更新充值记录状态失败");
        }


    }

    @Override
    public void dealRechargeRecord() throws Exception {
        //查询充值记录状态为0 -> 返回List<充值记录>
        List<RechargeRecord> rechargeRecordList = rechargeRecordMapper.selectRechargeRecordListByRechargeStatus(0);

        //循环遍历，获取到每一条充值记录
        for (RechargeRecord rechargeRecord : rechargeRecordList) {
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("out_trade_no",rechargeRecord.getRechargeNo());

            //调用pay工程的订单查询接口
            String jsonString = HttpClientUtils.doPost("http://localhost:9090/pay/api/alipayQuery", paramMap);

            //将json格式的字符串转换为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);

            //获取alipay_trade_query_response所对应的json对象
            JSONObject tradeQueryResponseJsonObject = jsonObject.getJSONObject("alipay_trade_query_response");

            //获取通信标识
            String code = tradeQueryResponseJsonObject.getString("code");
            if (!StringUtils.equals("10000", code)) {
                throw new Exception("通信异常");
            }

            //获取业务处理的结果
            String tradeStatus = tradeQueryResponseJsonObject.getString("trade_status");

             /*交易状态：
            WAIT_BUYER_PAY（交易创建，等待买家付款）
            TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
            TRADE_SUCCESS（交易支付成功）
            TRADE_FINISHED（交易结束，不可退款）*/

            if (StringUtils.equals("TRADE_CLOSED", tradeStatus)) {
                //更新充值记录的状态为2失败
                RechargeRecord updateRecharge = new RechargeRecord();
                updateRecharge.setRechargeNo(rechargeRecord.getRechargeNo());
                updateRecharge.setRechargeStatus("2");
                int i = rechargeRecordMapper.updateRechargeRecordByRechargeNo(updateRecharge);
                if (i <= 0) {
                    throw new Exception("更新状态失败");
                }
            }

            if (StringUtils.equals("TRADE_SUCCESS", tradeStatus)) {

                RechargeRecord rechargeRecordDetail = rechargeRecordMapper.selectRechargeRecordByRechargeNo(rechargeRecord.getRechargeNo());

                //再次判断当前充值记录的状态是否为充值完成
                if (StringUtils.equals("0", rechargeRecordDetail.getRechargeStatus())) {
                    paramMap.put("uid",rechargeRecord.getUid());
                    paramMap.put("rechargeMoney",rechargeRecord.getRechargeMoney());

                    //更新帐户可用余额
                    int updateFInanceAccountCount = financeAccountMapper.updateFinanceAccountByRecharge(paramMap);
                    if (updateFInanceAccountCount <= 0) {
                        throw new Exception("更新帐户余额失败");
                    }

                    //更新充值记录的状态
                    RechargeRecord updateRechargeRecord = new RechargeRecord();
                    updateRechargeRecord.setRechargeNo(rechargeRecord.getRechargeNo());
                    updateRechargeRecord.setRechargeStatus("1");
                    int updateCount = rechargeRecordMapper.updateRechargeRecordByRechargeNo(updateRechargeRecord);
                    if (updateCount <= 0) {
                        throw new Exception("更新充值记录的状态失败");
                    }

                }





            }

        }


    }

    @Override
    public RechargeRecord queryRechargeRecordByRechargeNo(String rechargeNo) {
        return rechargeRecordMapper.selectRechargeRecordByRechargeNo(rechargeNo);
    }
}
