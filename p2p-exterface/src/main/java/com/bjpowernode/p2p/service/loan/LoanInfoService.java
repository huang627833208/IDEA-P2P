package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 * ClassName:LoanInfoService
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/8/29 10:53
 * @author:guoxin
 */
public interface LoanInfoService {

    /**
     * 获取历史平均年化收益率
     * @return Double数据
     */
    Double queryHistoryAverageYearRate();

    /**
     * 根据产品类型获取产品信息列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    /**
     * 分页查询产品信息列表
     * @param paramMap
     * @return
     */
    PaginationVO<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap);

    /**
     * 根据产品标识获取产品的详情
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);
}
