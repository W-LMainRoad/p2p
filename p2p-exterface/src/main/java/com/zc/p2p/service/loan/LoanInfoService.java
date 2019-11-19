package com.zc.p2p.service.loan;

import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.model.vo.PageinatinoVO;

import java.util.List;
import java.util.Map;

/**
 * ClassName:LoanInfoService
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/10/28 19:57
 * @author:youxiang@163.com
 */
public interface LoanInfoService {
    /**
     * 获取平台历史平均年化收益率
     * @return
     */
    Double queryHistoryAverageRate();

    /**
     * 根据产品类型获取产品信息列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    /**
     * 分页查询产品信息列表
     * @param paramMap
     * @return
     */
    PageinatinoVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap);

    /**
     * 根据id查询产品详情
     * @return
     * @param id
     */
    LoanInfo queryLoanInfoById(Integer id);

    /**
     * 定期根据产品状态查询产品信息列表
     * @param i
     * @return
     */
    List<LoanInfo> queryLoanInfoListByStatus(int i);
}
