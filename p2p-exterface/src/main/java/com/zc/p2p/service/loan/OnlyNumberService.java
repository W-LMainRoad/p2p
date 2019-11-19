package com.zc.p2p.service.loan;

/**
 * ClassName:OnlyNumberService
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/11/12 14:18
 * @author:youxiang@163.com
 */
public interface OnlyNumberService {
    /**
     * 获取redis的全局唯一数字
     * @return
     */
    Long getOnylNumber();
}
