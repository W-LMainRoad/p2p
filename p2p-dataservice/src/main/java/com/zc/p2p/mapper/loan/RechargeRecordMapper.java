package com.zc.p2p.mapper.loan;

import com.zc.p2p.model.loan.RechargeRecord;

import java.util.List;
import java.util.Map;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    /**
     * 根据uid查询最近充值记录
     * @param paramMap
     * @return
     */
    List<RechargeRecord> selectRechargeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 查询用户总充值记录
     * @param paramMap
     * @return
     */
    Long selectTotal(Map<String, Object> paramMap);

    /**
     * 根据充值订单号更新充值记录
     * @param rechargeRecord
     * @return
     */
    int updateRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);
}