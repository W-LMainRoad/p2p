package com.zc.p2p.mapper.user;

import com.zc.p2p.model.user.FinanceAccount;

import java.util.Map;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /**
     * 根据用户表示获取账户信息
     * @param uid
     * @return
     */
    FinanceAccount selectFinanceAccountByUid(Integer uid);

    /**
     * 用户投资或收益到账，更新账户可用余额
     * @param paramMap
     * @return
     */
    int updateFinanceAccountByUid(Map<String, Object> paramMap);

}