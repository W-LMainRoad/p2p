package com.zc.web;

import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.model.loan.BidInfo;
import com.zc.p2p.model.user.User;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.model.vo.ResultObject;
import com.zc.p2p.service.loan.BidInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:BidInfoController
 * Package:com.zc.web
 * Description:
 *
 * @date:2019/11/5 19:22
 * @author:youxiang@163.com
 */
@Controller
public class BidInfoController {

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping(value = "/loan/invest")
    public @ResponseBody Object invest(HttpServletRequest request,
                                       @RequestParam(value = "loanId",required = true) Integer loanId,
                                       @RequestParam(value = "bidMoney",required = true) Integer bidMoney) {
        Map<String,Object> retMap = new HashMap<>();

        //从session中获取用户的信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //准备请求参数
        Map<String,Object> paramMap = new HashMap<>();
        //用户标识
        paramMap.put("uid",sessionUser.getId());
        //产品标识
        paramMap.put("loanId",loanId);
        //投资金额
        paramMap.put("bidMoney",bidMoney);
        //手机号码
        paramMap.put("phone",sessionUser.getPhone());

        //用户投资（用户表示，产品表示，投资金额） -> 返回结果resultObject
        ResultObject resultObject = bidInfoService.invest(paramMap);

        //判断是否成功
        if (StringUtils.equals(resultObject.getErrorCode(),Constants.SUCCESS)){
            retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        } else {
            retMap.put(Constants.ERROR_MESSAGE,"当前投资人数过多，请稍后重试");
            return retMap;
        }

        return retMap;
    }

    @RequestMapping("/loan/myInvest")
    public String myInvest(HttpServletRequest request, Model model,
                            @RequestParam(value = "currentPage",required = false) Integer currentPage){
        //判断当前页码是否为空，为空默认为第一页
        if (null == currentPage){
            currentPage = 1;
        }

        //准备参数,页码、条数、uid
        Map<String,Object> paramMap = new HashMap<>();
        int pageSize = 5;
        paramMap.put("pageSize",pageSize);
        paramMap.put("currentPage",(currentPage - 1) * pageSize);
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        paramMap.put("uid",user.getId());

        //分页查询产品信息列表（uid，页码，每页显示几条）-> 返回分页模型对象（总记录条数，当前页要显示的数据）
        PageinatinoVO<BidInfo> pageinatinoVO = bidInfoService.queryBidInfoByPage(paramMap);

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
        model.addAttribute("bidInfoList",pageinatinoVO.getDataList());
        //当前页码
        model.addAttribute("currentPage",currentPage);


        return "myInvest";
    }
}
