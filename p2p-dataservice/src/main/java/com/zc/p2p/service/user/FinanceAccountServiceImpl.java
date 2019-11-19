package com.zc.p2p.service.user;

import com.zc.p2p.mapper.user.FinanceAccountMapper;
import com.zc.p2p.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName:FinanceAccountServiceImpl
 * Package:com.zc.p2p.service.user
 * Description:
 *
 * @date:2019/11/4 16:39
 * @author:youxiang@163.com
 */
@Service("financeAccountServiceImpl")
public class FinanceAccountServiceImpl implements FinanceAccountService{
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer uid) {

        return financeAccountMapper.selectFinanceAccountByUid(uid);
    }
}
