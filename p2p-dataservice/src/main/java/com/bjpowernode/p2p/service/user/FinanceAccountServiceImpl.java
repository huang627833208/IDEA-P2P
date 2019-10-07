package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName:FinanceAccountServiceImpl
 * Package:com.bjpowernode.p2p.service.user
 * Description:TODO
 *
 * @date:2019/8/31 11:18
 * @author:guoxin
 */
@Service("financeAccountServiceImpl")
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer uid) {
        return financeAccountMapper.selectFinanceAccountByUid(uid);
    }
}
