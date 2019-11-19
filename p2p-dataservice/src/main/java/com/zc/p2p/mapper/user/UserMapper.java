package com.zc.p2p.mapper.user;

import com.zc.p2p.model.user.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 获取平台注册总人数
     * @return
     */
    Long selectAllUserCount();

    /**
     *
     * 根据手机号查询用户信息
      * @return
     */
    User selectUserByPhone(String phone);

    /**
     * 根据手机号和密码查询用户信息
     * @return
     */
    User selectUserByPhoneAndLoginPassword(String phone, String loginPassword);
}