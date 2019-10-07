package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:UserServiceImpl
 * Package:com.bjpowernode.p2p.service.user
 * Description:TODO
 *
 * @date:2019/8/29 15:11
 * @author:guoxin
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Long queryAllUserCount() {
        Long allUserCount = (Long) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);

        //判断用户总人数是否为空
        if (!ObjectUtils.allNotNull(allUserCount)) {

            //设置同步代码块
            synchronized (this) {

                //再次从缓存中获取该值
                allUserCount = (Long) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);

                //再次判断是否有值
                if (!ObjectUtils.allNotNull(allUserCount)) {

                    //去数据库查询
                    allUserCount = userMapper.selectAllUserCount();

                    //并存放到redis缓存中
                    redisTemplate.opsForValue().set(Constants.ALL_USER_COUNT, allUserCount, 15, TimeUnit.MINUTES);

                }

            }


        }

        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public User register(String phone, String loginPassword) throws Exception {
        //新增用户
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        int insertUserCount = userMapper.insertSelective(user);

        if (insertUserCount <= 0) {
            throw new Exception("新增用户失败");

        }

//        int i = 9/0;

        User userDetail = userMapper.selectUserByPhone(phone);
        //开立帐户
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(userDetail.getId());
        financeAccount.setAvailableMoney(888.0);
        int insertSelective = financeAccountMapper.insertSelective(financeAccount);

        if (insertSelective <= 0) {
            throw new Exception("新增帐户失败");
        }



        return user;
    }

    @Override
    public int modifyUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User login(String loginPassword, String phone) throws Exception {

        //根据手机号和密码查询用户信息
        User user = userMapper.selectUserByPhoneAndLoginPassword(loginPassword,phone);

        //判断用户是否存在
        if (!ObjectUtils.allNotNull(user)) {
            throw new Exception("用户名或密码有误");
        }

        //更新最近登录时间
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(new Date());
        int i = userMapper.updateByPrimaryKeySelective(updateUser);

        if (i <= 0) {
            throw new Exception("用户更新最近登录时间失败");
        }

        return user;
    }
}
