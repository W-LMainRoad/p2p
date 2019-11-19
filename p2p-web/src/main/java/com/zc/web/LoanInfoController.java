package com.zc.web;

import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.model.loan.BidInfo;
import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.model.user.FinanceAccount;
import com.zc.p2p.model.user.User;
import com.zc.p2p.model.vo.BidUserTop;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.service.loan.BidInfoService;
import com.zc.p2p.service.loan.LoanInfoService;
import com.zc.p2p.service.user.FinanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:loanInfoController
 * Package:com.zc.web
 * Description:
 *
 * @date:2019/10/30 9:06
 * @author:youxiang@163.com
 */
@Controller
public class LoanInfoController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @RequestMapping("/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam(value = "ptype",required = false) Integer ptype,
                       @RequestParam(value = "currentPage",required = false) Integer currentPage){
        //判断当前页码是否为空，为空默认值为第一页
        if (null == currentPage) {
            //默认为第一页
            currentPage = 1;
        }
        //准备分页查询参数
        Map<String,Object> paramMap = new HashMap<>();
        if (null != ptype) {
            paramMap.put("productType",ptype);
        }

        int pageSize = 9;

        //起始下标
        paramMap.put("currentPage",(currentPage - 1) * pageSize);

        //截取长度
        paramMap.put("pageSize",pageSize);

        //分页查询产品信息列表（产品类型，页码，每页显示几条）-> 返回分页模型对象（总记录条数，当前页要显示的数据）
        PageinatinoVO<LoanInfo> pageinatinoVO = loanInfoService.queryLoanInfoByPage(paramMap);

        //计算总页数
        int totalPage = pageinatinoVO.getTotal().intValue() / pageSize;

        //再次求余
        int mod = pageinatinoVO.getTotal().intValue() % pageSize;
        if(mod > 0){
            totalPage = totalPage + 1;
        }

        //总记录数
        model.addAttribute("totalRows",pageinatinoVO.getTotal());
        //总页数
        model.addAttribute("totalPage",totalPage);
        //每页显示的数据
        model.addAttribute("loanInfoList",pageinatinoVO.getDataList());
        //当前页码
        model.addAttribute("currentPage",currentPage);

        //当前产品类型
        if (null != ptype) {
            model.addAttribute("ptype",ptype);
        }

        //用户投资排行榜
        List<BidUserTop> bidUserTopList = bidInfoService.queryBidUserTop();

        model.addAttribute("bidUserTopList",bidUserTopList);


        return "loan";
    }

    @RequestMapping("/loan/loanInfo")
    public String loanInfo(HttpServletRequest request,Model model,
                            @RequestParam(value = "id",required = true) Integer id){
        //根据产品表示获取产品的详情
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);

        //根据产品标识获取该产品的所有投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfoListByLoanId(id);

        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("bidInfoList",bidInfoList);

        //获取当前的账户可用余额

        //检查当前用户是否登录
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        if (user!=null){
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(user.getId());
            model.addAttribute("financeAccount",financeAccount);
        }

        return "loanInfo";
    }
}
