package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.common.util.DateUtils;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:IncomeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/9/2 16:01
 * @author:guoxin
 */
@Service("incomeRecordServiceImpl")
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<IncomeRecord> queryRecentlyIncomeRecordListByUid(Map<String, Object> paramMap) {
        return incomeRecordMapper.selectRecentlyIncomeRecordListByUid(paramMap);
    }

    @Override
    public void generateIncomePlan() throws Exception {

        //获取到所有产品状态为1已满标的产品 -> 返回List<已满标产品>
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoListByProductStatus(1);

        //循环遍历，获取每一个产品
        for (LoanInfo loanInfo : loanInfoList) {

            //获取当前产品的所有投资记录List<投资记录>
            List<BidInfo> bidInfoList = bidInfoMapper.selectBidInfoListByLoanId(loanInfo.getId());

            //循环遍历，获取每一条投资记录
            for (BidInfo bidInfo : bidInfoList) {

                //将当前的投资记录生成对应的收益计划
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setIncomeStatus(0);

                //收益时间(Date) = 满标时间(Date) + 产品周期(int天/月)
                Date incomeDate = null;

                //收益金额 = 投资金额 * 日利率 * 投资天数
                Double incomeMoney = null;

                if (Constants.PRODUCT_TYPE_X == loanInfo.getProductType()) {
                    //新手宝
                    incomeDate = DateUtils.getDateByAddDays(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate() / 100 /365) * loanInfo.getCycle();
                } else {
                    //优选或散标
                    incomeDate = DateUtils.getDateByAddMonths(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate() / 100 /365) * loanInfo.getCycle()*30;
                }

                incomeMoney = Math.round(incomeMoney * Math.pow(10,2)) / Math.pow(10,2);
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);

                int insertIncomeCount = incomeRecordMapper.insertSelective(incomeRecord);
                if (insertIncomeCount <= 0) {
                    throw new Exception("新增收益计划失败：产品标识为：" + loanInfo.getId() + ",投资记录标识为：" + bidInfo.getId());
                }


            }

            //更新产品的状态为2满标且生成收益计划
            LoanInfo updateLoanInfo = new LoanInfo();
            updateLoanInfo.setId(loanInfo.getId());
            updateLoanInfo.setProductStatus(2);
            int i = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
            if (i <= 0) {
                throw new Exception("产品标识为：" + loanInfo.getId() + ",更新状态为2失败");
            }
        }
    }

    @Override
    public void generateIncomeBack() throws Exception {
        //查询收益时间与当前时间一致且收益状态为未返还的
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordListByIncomeStatusAndCurDate(0);

        //循环遍历，获取每一条收益计划
        for (IncomeRecord incomeRecord : incomeRecordList) {
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("uid",incomeRecord.getUid());
            paramMap.put("bidMoney",incomeRecord.getBidMoney());
            paramMap.put("incomeMoney",incomeRecord.getIncomeMoney());

            //将当前收益计划的本金及利率返还给对应的用户
            int updateCount = financeAccountMapper.updateFinanceAccountByIncomeBack(paramMap);
            if (updateCount <= 0) {
                throw  new Exception("收益记录标识为：" + incomeRecord.getId() + ",收益返还失败");
            }


            //更新当前收益记录的状态为1已返还
            IncomeRecord updateIncome = new IncomeRecord();
            updateIncome.setId(incomeRecord.getId());
            updateIncome.setIncomeStatus(1);
            int i = incomeRecordMapper.updateByPrimaryKeySelective(updateIncome);
            if (i <= 0) {
                throw new Exception("收益记录标识为:" + incomeRecord.getId() + ",更新状态为1失败");
            }
        }


    }
}
