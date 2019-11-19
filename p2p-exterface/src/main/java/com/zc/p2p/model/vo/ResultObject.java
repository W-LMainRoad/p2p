package com.zc.p2p.model.vo;

import java.io.Serializable;

/**
 * ClassName:ResultObject
 * Package:com.zc.p2p.model.vo
 * Description:
 *
 * @date:2019/10/31 18:53
 * @author:youxiang@163.com
 */
public class ResultObject implements Serializable {
    /**
     * 错误码
     */
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ResultObject{" +
                "errorCode='" + errorCode + '\'' +
                '}';
    }
}
