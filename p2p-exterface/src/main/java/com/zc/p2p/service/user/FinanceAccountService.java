package com.zc.p2p.service.user;

import com.zc.p2p.model.user.FinanceAccount;

/**
 * ClassName:FinanceAccountService
 * Package:com.zc.p2p.service.user
 * Description:
 *
 * @date:2019/11/4 16:23
 * @author:youxiang@163.com
 */
public interface FinanceAccountService {
    /**
     * 根据用户表示获取账户信息
     * @param id
     * @return
     */
    FinanceAccount queryFinanceAccountByUid(Integer id);
}
