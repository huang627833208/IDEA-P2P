package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.BidUser;
import com.bjpowernode.p2p.model.vo.PaginationVO;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:LoanInfoController
 * Package:com.bjpowernode.p2p.web
 * Description:TODO
 *
 * @date:2019/8/30 10:27
 * @author:guoxin
 */
@Controller
public class LoanInfoController extends BaseController{

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @RequestMapping(value = "/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam (value = "ptype",required = false) Integer ptype,
                       @RequestParam (value = "currentPage",defaultValue = "1") Integer currentPage) {

        //准备参数
        Map<String,Object> paramMap = new HashMap<String,Object>();

        if (ObjectUtils.allNotNull(ptype)) {
            paramMap.put("productType",ptype);
        }

        int pageSize = 9;
        paramMap.put("currentPage",(currentPage-1)*pageSize);
        paramMap.put("pageSize",pageSize);

        //分页查询产品信息列表(产品类型,页码,每页显示条数) -> 返回数据：List<产品>，总记录数，分页模型对象PaginationVO
        PaginationVO<LoanInfo> paginationVO = loanInfoService.queryLoanInfoListByPage(paramMap);

        //计算总页数
        int totalPage = paginationVO.getTotal().intValue() / pageSize;
        int mod = paginationVO.getTotal().intValue() % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }

        model.addAttribute("loanInfoList",paginationVO.getDataList());
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);
        if (ObjectUtils.allNotNull(ptype)) {
            model.addAttribute("ptype",ptype);
        }

        //查询用户投资排行榜
        List<BidUser> bidUserList = bidInfoService.queryBidUserTop();
        model.addAttribute("bidUserList",bidUserList);



        return "loan";
    }



    @RequestMapping(value = "/loan/loanInfo")
    public String loanInfo(HttpServletRequest request,Model model,
                           @RequestParam (value = "id",required = true) Integer id) {

        //根据产品标识获取产品的详情
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);

        //根据产品标识获取产品的最近10条投资记录
        //使用limit
        //准备参数
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("loanId",id);
        paramMap.put("currentPage",0);
        paramMap.put("pageSize",10);

        List<BidInfo> bidInfoList = bidInfoService.queryRecentlyBidInfoListByLoanId(paramMap);

        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("bidInfoList",bidInfoList);

        //从session中获取用户的信息
        User sessionUser = (User) BaseController.getSessionValue(request, Constants.SESSION_USER);

        //判断用户是否存在
        if (ObjectUtils.allNotNull(sessionUser)) {

            //根据用户标识获取用户的帐户资金信息
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
            model.addAttribute("financeAccount",financeAccount);
        }




        return "loanInfo";
    }
}
