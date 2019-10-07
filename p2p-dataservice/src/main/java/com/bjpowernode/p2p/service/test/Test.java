package com.bjpowernode.p2p.service.test;

import com.bjpowernode.p2p.service.loan.BidInfoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName:Test
 * Package:com.bjpowernode.p2p.service.test
 * Description:TODO
 *
 * @date:2019/9/5 12:04
 * @author:guoxin
 */
public class Test {

    public static void main(String[] args) {
        //获取spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        //获取指定key的bean
        BidInfoService bidInfoService = (BidInfoService) applicationContext.getBean("bidInfoServiceImpl");

        //创建一个固定的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        //准备参数
        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uid",1);
        paramMap.put("loanId",3);
        paramMap.put("bidMoney",1.0);

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

        executorService.shutdownNow();
    }
}
