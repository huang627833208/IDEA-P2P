package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;

/**
 * ClassName:FinanceAccountService
 * Package:com.bjpowernode.p2p.service.user
 * Description:TODO
 *
 * @date:2019/8/31 11:18
 * @author:guoxin
 */
public interface FinanceAccountService {

    /**
     * 根据用户标识获取帐户资金信息
     * @param uid
     * @return
     */
    FinanceAccount queryFinanceAccountByUid(Integer uid);
}
