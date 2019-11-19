package com.zc.p2p.mapper.loan;

import com.zc.p2p.model.loan.IncomeRecord;
import com.zc.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    /**
     * 根据产品信息列表插入收益记录
     * @param loanInfoList
     * @return
     */
    int insertByLoanInfoList(List<LoanInfo> loanInfoList);

    /**
     * 查询符合收益返还的收益记录
     * @param
     * @return
     */
    List<IncomeRecord> selectByDateAndStatus(int status);

    /**
     * 将收益状态改为1
     * @return
     */
    int updateStatusByIncomeRecordList(List<IncomeRecord> incomeRecordList);

    /**
     * 分页查询最近收益
     * @param paramMap
     * @return
     */
    List<IncomeRecord> selectIncomeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 查询用户收益总条数
     * @param paramMap
     * @return
     */
    Long selectTotal(Map<String, Object> paramMap);
}