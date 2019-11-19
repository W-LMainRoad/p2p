package com.zc.p2p.model.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体
 * ClassName:User
 * <p>Description:必须要序列化</p>
 */
public class User implements Serializable {
    /**
     * 用户标识
     */
    private Integer id;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户身份证号码
     */
    private String idCard;
    /**
     * 用户注册时间
     */
    private Date addTime;
    /**
     * 最近登录时间
     */
    private Date lastLoginTime;
    /**
     * 用户头像文件路径
     */
    private String headerImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword == null ? null : loginPassword.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage == null ? null : headerImage.trim();
    }
}