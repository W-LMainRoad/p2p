package com.zc.p2p.service.loan;

import com.zc.p2p.mapper.loan.RechargeRecordMapper;
import com.zc.p2p.mapper.user.FinanceAccountMapper;
import com.zc.p2p.model.loan.RechargeRecord;
import com.zc.p2p.model.vo.PageinatinoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ClassName:RechargeRecordServiceImpl
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/11/10 13:39
 * @author:youxiang@163.com
 */
@Service("rechargeRecordServiceImpl")
public class RechargeRecordServiceImpl implements RechargeRecordService{

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<RechargeRecord> queryRechargeListByUid(Map<String, Object> paramMap) {
        return rechargeRecordMapper.selectRechargeRecordListByUid(paramMap);
    }

    @Override
    public PageinatinoVO<RechargeRecord> queryRechargeByPage(Map<String, Object> paramMap) {
        PageinatinoVO<RechargeRecord> pageinatinoVO = new PageinatinoVO<>();

        Long total = rechargeRecordMapper.selectTotal(paramMap);
        pageinatinoVO.setTotal(total);

        List<RechargeRecord> rechargeRecordList = rechargeRecordMapper.selectRechargeRecordListByUid(paramMap);
        pageinatinoVO.setDataList(rechargeRecordList);

        return pageinatinoVO;
    }

    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
    }

    @Override
    public int recharge(Map<String, Object> paramMap) {

        //更新账户可用余额
        int updateFinanceAccount = financeAccountMapper.updateFinanceAccountByUid(paramMap);

        if (updateFinanceAccount > 0){
            //更新充值记录状态
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRechargeNo((String) paramMap.get("rechargeNo"));
            rechargeRecord.setRechargeStatus("1");
            int updateRechargeNo = rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
            if(updateRechargeNo<0){
                return 0;
            }
        } else {
            return 0;
        }

        return 1;
    }
}
