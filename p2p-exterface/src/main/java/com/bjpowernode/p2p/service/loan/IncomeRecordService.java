package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.IncomeRecord;

import java.util.List;
import java.util.Map;

/**
 * ClassName:IncomeRecordService
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/9/2 16:00
 * @author:guoxin
 */
public interface IncomeRecordService {

    /**
     * 根据用户标识获取最近的收益记录（包含：产品信息）
     * @param paramMap
     * @return
     */
    List<IncomeRecord> queryRecentlyIncomeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 生成收益计划
     */
    void generateIncomePlan() throws Exception;

    /**
     * 收益返还
     */
    void generateIncomeBack() throws Exception;
}
