package com.zc.p2p.service.loan;

import com.zc.p2p.model.loan.RechargeRecord;
import com.zc.p2p.model.vo.PageinatinoVO;

import java.util.List;
import java.util.Map;

/**
 * ClassName:RechargeRecordService
 * Package:com.zc.p2p.service.user
 * Description:
 *
 * @date:2019/11/10 13:37
 * @author:youxiang@163.com
 */
public interface RechargeRecordService {
    /**
     * 根据uid查询最近充值记录
     * @param paramMap
     * @return
     */
    List<RechargeRecord> queryRechargeListByUid(Map<String, Object> paramMap);

    /**
     * 分页查询最近充值
     * @param paramMap
     * @return
     */
    PageinatinoVO<RechargeRecord> queryRechargeByPage(Map<String, Object> paramMap);

    int addRechargeRecord(RechargeRecord rechargeRecord);

    /**
     * 根据充值订单号更新充值记录
     * @param updateRechargeRecord
     * @return
     */
    int modifyRechargeRecordByRechargeNo(RechargeRecord updateRechargeRecord);

    /**
     * 用户充值
     * @param paramMap
     * @return
     */
    int recharge(Map<String, Object> paramMap);
}
