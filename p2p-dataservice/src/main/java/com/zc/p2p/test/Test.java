package com.zc.p2p.test;

import com.zc.p2p.service.loan.IncomeRecordService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName:Test
 * Package:com.zc.p2p.test
 * Description:
 *
 * @date:2019/10/30 18:59
 * @author:youxiang@163.com
 */
public class Test {
    public static void main(String[] args) {

//        获取spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        IncomeRecordService incomeRecordService = (IncomeRecordService) applicationContext.getBean("incomeRecordServiceImpl");
        incomeRecordService.generateIncomeBack();
        

       /* IncomeRecordService incomeRecordService = (IncomeRecordService) applicationContext.getBean("incomeRecordServiceImpl");
        LoanInfoService loanInfoService = (LoanInfoService) applicationContext.getBean("loanInfoServiceImpl");

        List<LoanInfo> loanInfoList = loanInfoService.queryLoanInfoListByStatus(1);

        ResultObject resultObject = incomeRecordService.generateIncome(loanInfoList);*/

       /* //获取指定的bean
        BidInfoService bidInfoService = (BidInfoService) applicationContext.getBean("bidInfoServiceImpl");

        //创建一个固定的线程
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",1);
        paramMap.put("loanId",3);
        paramMap.put("bidMoney",1);

        for (int i =0 ; i<1000;i++) {
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return bidInfoService.invest(paramMap);
                }
            });
        }

        executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return bidInfoService.invest(paramMap);
            }
        });

        executorService.shutdown();*/

    }
}
