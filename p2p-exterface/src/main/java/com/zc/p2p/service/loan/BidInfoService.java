package com.zc.p2p.service.loan;

import com.zc.p2p.model.loan.BidInfo;
import com.zc.p2p.model.vo.BidUserTop;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.model.vo.ResultObject;

import java.util.List;
import java.util.Map;

/**
 * ClassName:BidInfoService
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/10/29 18:57
 * @author:youxiang@163.com
 */
public interface BidInfoService {
    /**
     * 获取平台累计投资金额
     * @return
     */
    Double queryAllBidMoney();

    /**
     * 根据产品表示获得产品所有投资记录（包含用户的信息）
     * @param loanId
     * @return
     */
    List<BidInfo> queryBidInfoListByLoanId(Integer loanId);

    /**
     * 用户投资
     * @param paramMap
     * @return
     */
    ResultObject invest(Map<String, Object> paramMap);

    /**
     * 从redis缓存中获取用户投资排行榜
     * @return
     */
    List<BidUserTop> queryBidUserTop();

    /**
     * 根据用户id获取最近投资列表
     * @param paramMap
     * @return
     */
    List<BidInfo> queryInvestListByUid(Map<String, Object> paramMap);

    /**
     * 分页查询用户投资列表
     * @param paramMap
     * @return
     */
    PageinatinoVO<BidInfo> queryBidInfoByPage(Map<String, Object> paramMap);
}
