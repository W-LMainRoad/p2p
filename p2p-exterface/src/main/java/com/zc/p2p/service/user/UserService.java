package com.zc.p2p.service.user;

import com.zc.p2p.model.user.User;
import com.zc.p2p.model.vo.ResultObject;

/**
 * ClassName:UserService
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/10/29 10:16
 * @author:youxiang@163.com
 */
public interface UserService {
    /**
     * 获取平台注册总人数
     * @return
     */
    Long queryAllUserCount();

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 用户注册
     * @param phone
     * @param loginPassword
     * @return
     */
    ResultObject register(String phone, String loginPassword);

    /**
     * 根据用户表示更新用户信息
     * @param user
     * @return
     */
    int modifyUserById(User user);

    /**
     * 用户登录
     * @param phone
     * @param loginPassword
     * @return
     */
    User login(String phone, String loginPassword);
}
