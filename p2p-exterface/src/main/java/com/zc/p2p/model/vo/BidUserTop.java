package com.zc.p2p.model.vo;

import java.io.Serializable;

/**
 * ClassName:BidUserTop
 * Package:com.zc.p2p.model.vo
 * Description:
 *
 * @date:2019/11/8 11:07
 * @author:youxiang@163.com
 */
public class BidUserTop implements Serializable {
    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 分数，累计投资金额
     */
    private Double score;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
