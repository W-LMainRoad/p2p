package com.zc.p2p.model.user;

import java.io.Serializable;

/**
 * 用户资金账户实体
 * ClassName:FinanceAccount
 * <p>Description:</p>
 */
public class FinanceAccount implements Serializable {
    /**
     * 用户资金账户标识
     */
    private Integer id;
    /**
     * 用户标识
     */
    private Integer uid;
    /**
     * 用户可用金额
     */
    private Double availableMoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(Double availableMoney) {
        this.availableMoney = availableMoney;
    }
}