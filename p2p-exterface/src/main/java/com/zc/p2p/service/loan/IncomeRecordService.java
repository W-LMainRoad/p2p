package com.zc.p2p.service.loan;

import com.zc.p2p.model.loan.IncomeRecord;
import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.model.vo.ResultObject;

import java.util.List;
import java.util.Map;

/**
 * ClassName:IncomeRecordService
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/11/7 14:39
 * @author:youxiang@163.com
 */
public interface IncomeRecordService {
    /**
     * 根据已满标产品信息列表生成收益记录
     * @param loanInfoList
     * @return
     */
    ResultObject generateIncome(List<LoanInfo> loanInfoList);

    /**
     * 生成收益回款
     * @return
     */
    ResultObject generateIncomeBack();

    /**
     * 根据uid查询最近收益
     * @param paramMap
     * @return
     */
    List<IncomeRecord> queryIncomeRecordListByUid(Map<String, Object> paramMap);

    /**
     * 分页查询最近收益
     * @param paramMap
     * @return
     */
    PageinatinoVO<IncomeRecord> queryIncomeRecordByPage(Map<String, Object> paramMap);
}
