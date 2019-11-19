package com.zc.p2p.timer;

import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.model.vo.ResultObject;
import com.zc.p2p.service.loan.IncomeRecordService;
import com.zc.p2p.service.loan.LoanInfoService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName:TimerManager
 * Package:com.zc.p2p.timer
 * Description:
 *
 * @date:2019/11/6 18:40
 * @author:youxiang@163.com
 */
@Component
public class TimerManager {

    private Logger logger = LogManager.getLogger(TimerManager.class);

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private IncomeRecordService incomeRecordService;

    //生成收益计划
    @Scheduled(cron = "0/20 * * * * ?")
    public void generateIncomePlan(){
        logger.info("--------生成受益计划开始-------");

        //根据产品状态查询到对应的产品信息列表（LoanInfo包含用户投标信息）
        List<LoanInfo> loanInfoList = loanInfoService.queryLoanInfoListByStatus(1);

        //批量生成用户收益记录表
        ResultObject resultObject =  incomeRecordService.generateIncome(loanInfoList);
        logger.info(resultObject);
        logger.info("--------生成收益计划结束-------");
    }

    //生成收益回款
    @Scheduled(cron = "0/20 * * * * ?")
    public void generateIncomeBack(){
        logger.info("--------生成收益回款开始-------");
        ResultObject resultObject = incomeRecordService.generateIncomeBack();
        logger.info(resultObject);
        logger.info("--------生成收益回款结束-------");
    }

}
