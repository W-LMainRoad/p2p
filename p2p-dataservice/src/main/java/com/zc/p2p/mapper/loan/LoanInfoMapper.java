package com.zc.p2p.mapper.loan;

import com.zc.p2p.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKeyWithBLOBs(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 历史平均年化收益率
     * @return
     */
    Double selectHistoryAverageRate();

    /**
     * 分页查询产品信息列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> selectLoanInfoByPage(Map<String, Object> paramMap);

    /**
     * 根据产品类型获取产品的总记录数
     * @param paramMap
     * @return
     */
    Long selectTotal(Map<String, Object> paramMap);

    /**
     * 更新产品的剩余可投金额
     * @param paramMap
     * @return
     */
    int updateLeftProductMoneyByLoanId(Map<String, Object> paramMap);

    /**
     * 查询投标已满的投标信息列表
     * @param i
     * @return
     */
    List<LoanInfo> selectLoanInfoByStatus(int i);

    /**
     * 更改已生成收益记录投资信息列表产品状态
     * @param loanInfoList
     * @return
     */
    int updateStatusBatchByPrimaryKeyList(List<LoanInfo> loanInfoList);
}