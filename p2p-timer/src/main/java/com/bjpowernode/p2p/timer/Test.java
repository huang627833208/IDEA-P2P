package com.bjpowernode.p2p.timer;

import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName:Test
 * Package:com.bjpowernode.p2p.timer
 * Description:TODO
 *
 * @date:2019/9/7 14:13
 * @author:guoxin
 */
public class Test {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        RechargeRecordService rechargeRecordService = (RechargeRecordService) applicationContext.getBean("rechargeRecordServiceImpl");

        rechargeRecordService.dealRechargeRecord();
    }
}
