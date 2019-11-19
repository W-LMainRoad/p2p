package com.zc.p2p.model.config;

/**
 * ClassName:Config
 * Package:com.zc.config
 * Description:
 *
 * @date:2019/11/1 11:23
 * @author:youxiang@163.com
 */
public class Config {

    /**
     * 实名认证的key
     */
    private String realNameAppkey;

    /**
     * 实名认证的URL
     */
    private String realNameUrl;

    public String getRealNameAppkey() {
        return realNameAppkey;
    }

    public void setRealNameAppkey(String realNameAppkey) {
        this.realNameAppkey = realNameAppkey;
    }

    public String getRealNameUrl() {
        return realNameUrl;
    }

    public void setRealNameUrl(String realNameUrl) {
        this.realNameUrl = realNameUrl;
    }
}
