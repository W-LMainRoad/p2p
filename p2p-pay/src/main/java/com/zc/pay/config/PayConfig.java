package com.zc.pay.config;

/**
 * ClassName:PayConfig
 * Package:com.zc.pay.config
 * Description:
 *
 * @date:2019/11/12 16:26
 * @author:youxiang@163.com
 */
public class PayConfig {
    private String alipayGatewayUrl;
    private String alpayAppid;
    private String alipayMerchantPrivateKey;
    private String alipayFormat;
    private String alipayCharset;
    private String alipayPublicKey;
    private String alipaySignType;
    private String alipayReturnUrl;
    private String alipayNotifyUrl;

    public String getAlipayReturnUrl() {
        return alipayReturnUrl;
    }

    public void setAlipayReturnUrl(String alipayReturnUrl) {
        this.alipayReturnUrl = alipayReturnUrl;
    }

    public String getAlipayNotifyUrl() {
        return alipayNotifyUrl;
    }

    public void setAlipayNotifyUrl(String alipayNotifyUrl) {
        this.alipayNotifyUrl = alipayNotifyUrl;
    }

    public String getAlipayGatewayUrl() {
        return alipayGatewayUrl;
    }

    public void setAlipayGatewayUrl(String alipayGatewayUrl) {
        this.alipayGatewayUrl = alipayGatewayUrl;
    }

    public String getAlpayAppid() {
        return alpayAppid;
    }

    public void setAlpayAppid(String alpayAppid) {
        this.alpayAppid = alpayAppid;
    }

    public String getAlipayMerchantPrivateKey() {
        return alipayMerchantPrivateKey;
    }

    public void setAlipayMerchantPrivateKey(String alipayMerchantPrivateKey) {
        this.alipayMerchantPrivateKey = alipayMerchantPrivateKey;
    }

    public String getAlipayFormat() {
        return alipayFormat;
    }

    public void setAlipayFormat(String alipayFormat) {
        this.alipayFormat = alipayFormat;
    }

    public String getAlipayCharset() {
        return alipayCharset;
    }

    public void setAlipayCharset(String alipayCharset) {
        this.alipayCharset = alipayCharset;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAlipaySignType() {
        return alipaySignType;
    }

    public void setAlipaySignType(String alipaySignType) {
        this.alipaySignType = alipaySignType;
    }
}
