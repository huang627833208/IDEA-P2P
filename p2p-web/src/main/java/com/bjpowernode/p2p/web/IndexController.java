package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName:IndexController
 * Package:com.bjpowernode.p2p.web
 * Description:TODO
 *
 * @date:2019/8/29 10:39
 * @author:guoxin
 */
@Controller
public class IndexController extends BaseController{

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping(value = "/index")
    public String index(Model model) {

        //创建一个固定的线程池
        /*ExecutorService executorService = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 1000; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Double historyAverageYearRate = loanInfoService.queryHistoryAverageYearRate();
                    model.addAttribute(Constants.HISTORY_AVERAGE_YEAR_RATE,historyAverageYearRate);
                }
            });
        }

        executorService.shutdown();*/


        //获取历史平均年化收益率
        Double historyAverageYearRate = loanInfoService.queryHistoryAverageYearRate();
        model.addAttribute(Constants.HISTORY_AVERAGE_YEAR_RATE,historyAverageYearRate);

        //获取平台注册总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT,allUserCount);

        //获取平台累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY,allBidMoney);

        //将以下查询看作是一个分页,使用MySQL数据库中的limit 起始下标,截取长度
        //根据产品类型获取产品信息列表(产品类型,页码,每页显示的条数)
        //准备参数
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentPage",0);

        //获取新手宝产品，产品类型：0，显示第1页，每页显示1个
        paramMap.put("pageSize",1);
        paramMap.put("productType",Constants.PRODUCT_TYPE_X);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("xLoanInfoList",xLoanInfoList);


        //获取优选产品，产品类型：1，显示第1页，每页显示4个
        paramMap.put("pageSize",4);
        paramMap.put("productType",Constants.PRODUCT_TYPE_U);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("uLoanInfoList",uLoanInfoList);

        //获取散标产品，产品类型：2，显示第1页，每页显示8个
        paramMap.put("pageSize",8);
        paramMap.put("productType",Constants.PRODUCT_TYPE_S);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("sLoanInfoList",sLoanInfoList);


        return "index";
    }
}
