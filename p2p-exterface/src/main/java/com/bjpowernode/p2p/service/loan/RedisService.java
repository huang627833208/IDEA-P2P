package com.bjpowernode.p2p.service.loan;

/**
 * ClassName:RedisService
 * Package:com.bjpowernode.p2p.service.loan
 * Description:TODO
 *
 * @date:2019/9/3 14:45
 * @author:guoxin
 */
public interface RedisService {

    /**
     * 将指定的value值存放到redis的Key中
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 从redis缓存中获取key的值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 从redis中获取唯一数字
     * @return
     */
    Long getOnlyNumber();
}
