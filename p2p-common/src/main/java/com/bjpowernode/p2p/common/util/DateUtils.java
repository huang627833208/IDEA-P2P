package com.bjpowernode.p2p.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ClassName:DateUtils
 * Package:com.bjpowernode.p2p.common.util
 * Description:TODO
 *
 * @date:2019/9/5 16:13
 * @author:guoxin
 */
public class DateUtils {


    public static Date getDateByAddDays(Date date, Integer count) {

        //创建一个日期处理类对象
        Calendar calendar = Calendar.getInstance();

        //设置日期处理类对象的时间为指定日期
        calendar.setTime(date);

        //在当前日期上添加天数
        calendar.add(Calendar.DATE,count);

        return calendar.getTime();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getDateByAddDays(new SimpleDateFormat("yyyy-MM-dd").parse("2008-08-08"),-1));
    }

    public static Date getDateByAddMonths(Date date, Integer count) {
        //创建一个日期处理类对象
        Calendar calendar = Calendar.getInstance();

        //设置日期处理类对象的时间为指定日期
        calendar.setTime(date);

        //在当前日期上添加天数
        calendar.add(Calendar.MONTH,count);

        return calendar.getTime();
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
}
