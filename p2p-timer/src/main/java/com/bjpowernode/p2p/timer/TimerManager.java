package com.bjpowernode.p2p.timer;

import com.bjpowernode.p2p.service.loan.IncomeRecordService;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ClassName:TimerManager
 * Package:com.bjpowernode.p2p.timer
 * Description:TODO
 *
 * @date:2019/9/5 15:12
 * @author:guoxin
 */
@Component
public class TimerManager {

    //获取日志记录对象
    private Logger logger = LogManager.getLogger(TimerManager.class);

    @Autowired
    private IncomeRecordService incomeRecordService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    //    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomePlan() {
        try {
            logger.info("------------生成收益计划开始-----------");

            incomeRecordService.generateIncomePlan();

            logger.info("------------生成收益计划结束-----------");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }


    //    @Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomeBack() {
        try {
            logger.info("------------收益返还开始-----------");

            incomeRecordService.generateIncomeBack();

            logger.info("------------收益返还结束-----------");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }

    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void dealRechargeRecord() {

        try {
            logger.info("------------处理掉单开始-----------");

            rechargeRecordService.dealRechargeRecord();

            logger.info("------------处理掉单结束-----------");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }

    }


}
