package com.zc.p2p.service.user;

import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.mapper.user.FinanceAccountMapper;
import com.zc.p2p.mapper.user.UserMapper;
import com.zc.p2p.model.user.FinanceAccount;
import com.zc.p2p.model.user.User;
import com.zc.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:UserServiceImpl
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/10/29 10:16
 * @author:youxiang@163.com
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public Long queryAllUserCount() {
        //先去redis中获取缓存

        //修改redis中key值的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //获取指定操作某一个key的操作对象
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(Constants.ALL_USER_COUNT);
        //获取指定key的value值
        Long allUserCount = (Long) boundValueOps.get();

        if (null == allUserCount) {
            //没有值去数据库查
            allUserCount = userMapper.selectAllUserCount();

            //将用户总人数放入redis缓存中
            boundValueOps.set(allUserCount,15,TimeUnit.MINUTES);
        }
        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        //通过手机号查到的用户数据返回
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public ResultObject register(String phone, String loginPassword) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);

        //新增用户
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        int insertUserCount = userMapper.insertSelective(user);
        if(insertUserCount>0){
            User userInfo = userMapper.selectUserByPhone(phone);
            //新增用户
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(userInfo.getId());
            financeAccount.setAvailableMoney(888.0);
            int insertFinanceCount = financeAccountMapper.insertSelective(financeAccount);
            if (insertFinanceCount < 0){
                resultObject.setErrorCode(Constants.FAIL);
            }
        } else {
            resultObject.setErrorCode(Constants.FAIL);
        }

        return resultObject;
    }

    @Override
    public int modifyUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User login(String phone, String loginPassword) {

        //根据手机号和密码查询用户
        User user = userMapper.selectUserByPhoneAndLoginPassword(phone,loginPassword);

        //判断用户是否存在
        if (user!=null){

            //更新用户最近登录时间
            User updateUser = new User();
            updateUser.setLastLoginTime(new Date());
            updateUser.setId(user.getId());
            userMapper.updateByPrimaryKeySelective(updateUser);

        }

        return user;
    }


}
