package com.zc.p2p.service.loan;

import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.mapper.loan.IncomeRecordMapper;
import com.zc.p2p.mapper.loan.LoanInfoMapper;
import com.zc.p2p.mapper.user.FinanceAccountMapper;
import com.zc.p2p.model.loan.IncomeRecord;
import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:IncomeRecordServiceImpl
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/11/7 14:41
 * @author:youxiang@163.com
 */
@Service("incomeRecordServiceImpl")
public class IncomeRecordServiceImpl implements IncomeRecordService{

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public ResultObject generateIncome(List<LoanInfo> loanInfoList) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);

        if (loanInfoList.size()==0 || loanInfoList ==null){
            resultObject.setErrorCode(Constants.FAIL);
            return resultObject;
        }

        int insertCount = incomeRecordMapper.insertByLoanInfoList(loanInfoList);

        if (insertCount <= 0) {
            resultObject.setErrorCode(Constants.FAIL);
            return resultObject;
        }

        //如果插入收益记录成功，则将产品状态更改为2
        int updateStatusCount = loanInfoMapper.updateStatusBatchByPrimaryKeyList(loanInfoList);
        //如果修改不成功
        if (updateStatusCount<=0){
            resultObject.setErrorCode(Constants.FAIL);
            return resultObject;
        }

        return resultObject;
    }

    @Override
    public ResultObject generateIncomeBack() {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);
        //查询用户收益表中收益状态为0并且收益时间为当前事件的收益记录
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectByDateAndStatus(0);

        if (incomeRecordList.size()==0 || incomeRecordList==null){
            resultObject.setErrorCode(Constants.FAIL);
            return resultObject;
        }

        Map<String,Object> paramMap = new HashMap<>();
        for (IncomeRecord incomeRecord:incomeRecordList){
            paramMap.put("incomeMoney",incomeRecord.getIncomeMoney());
            paramMap.put("uid",incomeRecord.getUid());
            //如果有查询结果将收益返还
            financeAccountMapper.updateFinanceAccountByUid(paramMap);
        }

        //收益发放之后将收益状态改为1
        incomeRecordMapper.updateStatusByIncomeRecordList(incomeRecordList);

        return resultObject;
    }

    @Override
    public List<IncomeRecord> queryIncomeRecordListByUid(Map<String, Object> paramMap) {
        return incomeRecordMapper.selectIncomeRecordListByUid(paramMap);
    }

    @Override
    public PageinatinoVO<IncomeRecord> queryIncomeRecordByPage(Map<String, Object> paramMap) {
        PageinatinoVO<IncomeRecord> pageinatinoVO = new PageinatinoVO<>();

        Long total = incomeRecordMapper.selectTotal(paramMap);
        pageinatinoVO.setTotal(total);

        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordListByUid(paramMap);
        pageinatinoVO.setDataList(incomeRecordList);

        return pageinatinoVO;
    }
}
