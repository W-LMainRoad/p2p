package com.zc.web;

import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.service.loan.BidInfoService;
import com.zc.p2p.service.loan.LoanInfoService;
import com.zc.p2p.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:IndexController
 * Package:com.zc.web
 * Description:
 *
 * @date:2019/10/28 19:05
 * @author:youxiang@163.com
 */
@Controller
public class IndexController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model){
        //获取历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);

        //获取平台注册总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT,allUserCount);

        //获取平台累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY,allBidMoney);

        //将以下查询看成是一个分页，实际功能，根据产品类型查询产品信息显示前几个
        //数据持久层用的是limit函数，limit起始下标，截取长度
        //loanInfoService.queryLoanInfoListByProductType(产品类型，页码，每页显示条数)

        Map<String,Object> paramMap = new HashMap<>();

        //页码，起始下标
        paramMap.put("currentPage",0);


        //获取新手包产品:产品类型：0 显示第1页 每页显示1个
        //产品类型
        paramMap.put("productType",Constants.PRODUCT_TYPE_X);
        //条数
        paramMap.put("pageSize",1);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //获取优选产品：产品类型：1 显示第1页 每页显示4个
        //产品类型
        paramMap.put("productType",Constants.PRODUCT_TYPE_U);
        //条数
        paramMap.put("pageSize",4);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //获取散标产品：产品类型：2 显示第1页，每页显示8个
        //产品类型
        paramMap.put("productType",Constants.PRODUCT_TYPE_S);
        //条数
        paramMap.put("pageSize",8);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        model.addAttribute("sLoanInfoList",sLoanInfoList);
        model.addAttribute("uLoanInfoList",uLoanInfoList);
        model.addAttribute("xLoanInfoList",xLoanInfoList);

        return "index";
    }

}
