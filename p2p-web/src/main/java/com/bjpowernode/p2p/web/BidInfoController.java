package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.common.util.Result;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName:BidInfController
 * Package:com.bjpowernode.p2p.web
 * Description:TODO
 *
 * @date:2019/9/5 11:04
 * @author:guoxin
 */
@Controller
public class BidInfoController {

    @Autowired
    private BidInfoService bidInfoService;


    @RequestMapping(value = "/loan/invest")
    public @ResponseBody
    Result invest(HttpServletRequest request,
                  @RequestParam(value = "loanId", required = true) Integer loanId,
                  @RequestParam(value = "bidMoney", required = true) Double bidMoney) {
        //从session中获取用户的信息
        User sessionUSer = (User) BaseController.getSessionValue(request, Constants.SESSION_USER);

        try {

            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("uid",sessionUSer.getId());
            paramMap.put("loanId",loanId);
            paramMap.put("bidMoney",bidMoney);
            paramMap.put("phone",sessionUSer.getPhone());

            //用户投资【1.更新产品剩余可投金额 2.更新帐户可用余额 3.新增投资记录 4.判断是否满标】
            bidInfoService.invest(paramMap);

            //创建一个固定的线程池
            ExecutorService executorService = Executors.newFixedThreadPool(100);

            /*//准备参数
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("uid", 1);
            paramMap.put("loanId", 3);
            paramMap.put("bidMoney", 1.0);

            for (int i = 0; i < 1000; i++) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bidInfoService.invest(paramMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            executorService.shutdownNow();*/
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败");
        }


        return Result.success();
    }

}
