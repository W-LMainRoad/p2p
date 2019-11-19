package com.zc.p2p.mapper.loan;

import com.zc.p2p.model.loan.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    /**
     * 获取平台累计投资金额
     * @return
     */
    Double selectAllBidMoney();

    /**
     * 根据产品标识获取产品的所有投资记录（包含用户的信息）
     * @param loanId
     * @return
     */
    List<BidInfo> selectBidInfoListByLoanId(Integer loanId);

    /**
     * 根据用户id获取最近投资列表
     * @param paramMap
     * @return
     */
    List<BidInfo> selectInvestListByUid(Map<String, Object> paramMap);

    /**
     * 获取用户投资总数
     * @param paramMap
     * @return
     */
    Long selectTotal(Map<String, Object> paramMap);
}