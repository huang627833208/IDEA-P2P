package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.RechargeRecord;

import java.util.List;
import java.util.Map;

/**
 * ClassName:RechargeRecordService
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/9/2 15:56
 * @author:guoxin
 */
public interface RechargeRecordService {
    /**
     * 根据用户标识获取最近的充值记录
     * @param paramMap
     * @return
     */
    List<RechargeRecord> queryRecentlyRechargeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 新增充值记录
     * @param rechargeRecord
     */
    void addRechargeRecord(RechargeRecord rechargeRecord) throws Exception;

    /**
     * 根据充值订单号更新充值记录
     * @param rechargeRecord
     * @return
     */
    int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);

    /**
     * 用户充值
     * @param paramMap
     */
    void recharge(Map<String, Object> paramMap) throws Exception;

    /**
     * 通过自动补偿机制解决充值掉单
     */
    void dealRechargeRecord() throws Exception;

    /**
     * 根据充值订单号获取充值记录的详情
     * @param rechargeNo
     * @return
     */
    RechargeRecord queryRechargeRecordByRechargeNo(String rechargeNo);
}
