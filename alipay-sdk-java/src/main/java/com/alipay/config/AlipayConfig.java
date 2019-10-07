package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/**
 * ClassName:AlipayConfig
 * Package:com.alipay.config
 * Description:TODO
 *
 * @date:2019/9/6 15:12
 * @author:guoxin
 */
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101300673386";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDd1SlqsKKLu8YISlMxHcMf4148WUOu0mlf4KBnZtksEPh4qUNd002zFjWvf9qDsuy517tSw7xy9HR0KgbrEJcdBtpSU6Z/BDvl5ngRWNYUP1NqzWpEB/3JWNltSagzdyCYfgyTgU1gtEG9R3AkfonKX0PM8TGEH0dUXgmLn8KDOO5dT8V1in9gYXJC5Yq7MsTML+lkpVxw9YpJXizlqq86mC0gnDuQjwZQwyV/9sArcfIF93ZL35uhG3x2OsrZv24WzBI+CfGiNPM8XlT88Q2QTMbG/IvWbpxux/9DZnOktpF7e6uzPF9427ixIb4kwDYQcSthVUm89YGf3YkolLFzAgMBAAECggEALb8GsnzImH5ZoV4Kd+eGVK4jwdg6r/OuTVpaWdiI0qCRTPjC7rdjn5nl6lI4cs5UKSMG+1NoKmClH0uHb7RVJWRDY5TikfEClS+I9TLXuwcwIadI0czAFmSfOf1llW9spmXgHA8fMOEbaXgw9SWvLq+koL/gkTmOESTvXP7OMxcJDXa4obEQpGpMNKasAk/ROg+nxLavS344HzaYJZELRBM6VuOuhlauQe81fmUxjTK6Rit6VGNGtE6VWumfPsIMg1g8i/oz0f1E0YWB3Gze025bvGHXWgM6VYOv6o2t+IOFmLgAMYO6+xdtWpka+b0jw/NrmWepasu49s4bwu1VyQKBgQD6/QVo6kxKDfce1YCTRq6XqDzKibaFUtx7nn67jltMRlm2iKWopd2LKYYLt5ndlT+aBsvRJNAteIYiOPGJUFN07X0cnyosewtM4zOksRwDeGQiTc0IN1ui2EGPjQLPfBTq4dUmFlxmomTc1iZaA1YQhEMsvi7pZwaUbpuYHSLLFwKBgQDiQxrytSX00MwEO22NwnJXfzH0P3L6nIGpbwWQ5S5zINLsEbPVa+RnkxqjI1PtNKjRBMNXauE6rqrz4wdCZqOLMjJqgdJrf0w48Zrpijtgul69/jkCU54T7BEB3R05Zf5x9L/xkmufE0xJsvfM3QNgkaGkwt9ngYjyoz+oZttWBQKBgQCkl/1OOihhgBsJK3YwHvAsIHYNVJZPhHF22A/AK7EPqir3G1GX8X4B3u87C+Jpf5DAD6jltMCYsSs1kiZDqFuymHOtq2ta1C4q8+xQGp2GYRM5s8+7jaIYfr0vyIeJTZPiHMtNOOjLnb+LWxy3duyf1DzBQM8+3QNhn8+Fx8WjbQKBgQCRcbGQ8wFDLjT3Q3xY8/WsaQcjiuXOU2zGDaf2TFxfhWoQdhCz0HcyXU1yMPNyN7eNJRKszveZUU5zGC1CcdgPUmU2Md2hXGMS97wtA77HnwXn+9OJu1aSV91W5Vwz2WyGtF3uW8DysOUJOxIvjqLtILBXySzyzEHMN0RkbDnyQQKBgFL7AlwJECtU9aHAXjbm/485M6ojavyuYTHJ0uMfRaJ/OH+3iDj2R2/NvfD8qCLXCSxmkJynbpBgsyNKD7I1A+P+jc+I+oMHcGJEolGvjzEyj1PVstR7SsN/49HvOUN3qFZu46V7+8ix9ML+rSRGM9NI/O/7LacQXSQIyLl8PgpQ";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtUbaH3eHrZWAuAryGOROWxFzfTMhyLP6bvjeYDGhMXqc+UKLdM9mc7OjhUOSVcPQN6TNnzaqdnMwfTmSwN0olPVGBlaO1LAvEqPunJKcewxCdzJes71ieVCbe3a/Mu7z9IYqp6zBBCOS9wtEheagd3qyVlgkwJrNrMsPhEWnUeFJBdqNH/NxglvNsbj0QKV6lwATEFIrHld8H5YOY6y6IplRr5FguXvQ6TQLp+GJp+/B1jINZolQZt9c5DfvtaODDSqddD6q4ZfYQcfZstTxSdOGqwJdwH8S6m0Vl6qDKSQc3v6R9o6ryf4uD327T8bwKzSIwlY5dkjGxpRVb4Z2fQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8090/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8090/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
