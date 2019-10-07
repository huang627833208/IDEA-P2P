package com.bjpowernode.pay.web;

import com.bjpowernode.http.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:WxpayController
 * Package:com.bjpowernode.pay.web
 * Description:TODO
 *
 * @date:2019/9/9 11:14
 * @author:guoxin
 */
@Controller
public class WxpayController {


    @RequestMapping(value = "/api/wxpay")
    public @ResponseBody Object wxpay(HttpServletRequest request,
                                      @RequestParam (value = "body",required = true) String body,
                                      @RequestParam (value = "out_trade_no",required = true) String out_trade_no,
                                      @RequestParam (value = "total_fee",required = true) String total_fee) throws Exception {

        //准备Map集合的请求参数
        Map<String,String> requestDataMap = new HashMap<String, String>();
        requestDataMap.put("appid","wx8a3fcf509313fd74");
        requestDataMap.put("mch_id","1361137902");
        requestDataMap.put("nonce_str",WXPayUtil.generateNonceStr());
        requestDataMap.put("body",body);
        requestDataMap.put("out_trade_no",out_trade_no);

        BigDecimal bigDecimal = new BigDecimal(total_fee);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        int i = multiply.intValue();
        requestDataMap.put("total_fee",String.valueOf(i));
        requestDataMap.put("spbill_create_ip","127.0.0.1");
        requestDataMap.put("notify_url","http://localhost:8080/p2p/loan/wxpayNotify");
        requestDataMap.put("trade_type","NATIVE");
        requestDataMap.put("product_id",out_trade_no);

        String signature = WXPayUtil.generateSignature(requestDataMap, "367151c5fd0d50f1e34a68a802d6bbca");
        requestDataMap.put("sign",signature);

        //将map集合的请求参数转换为xml格式的请求参数
        String requestDataXml = WXPayUtil.mapToXml(requestDataMap);

        //将请求参数传递给对应
        String responseDataXml = HttpClientUtils.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder", requestDataXml);

        //将xml格式的响应参数转换为map集合的响应参数
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);


        return responseDataMap;
    }
}
