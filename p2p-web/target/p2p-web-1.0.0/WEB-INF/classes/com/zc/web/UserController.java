package com.zc.web;

import com.alibaba.fastjson.JSONObject;
import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.model.config.Config;
import com.zc.p2p.model.loan.BidInfo;
import com.zc.p2p.model.loan.IncomeRecord;
import com.zc.p2p.model.loan.RechargeRecord;
import com.zc.p2p.model.user.FinanceAccount;
import com.zc.p2p.model.user.User;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.model.vo.ResultObject;
import com.zc.p2p.service.loan.BidInfoService;
import com.zc.p2p.service.loan.IncomeRecordService;
import com.zc.p2p.service.loan.LoanInfoService;
import com.zc.p2p.service.loan.RechargeRecordService;
import com.zc.p2p.service.user.FinanceAccountService;
import com.zc.p2p.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ClassName:UserController
 * Package:com.zc.web
 * Description:
 *
 * @date:2019/10/30 19:55
 * @author:youxiang@163.com
 */

//等同于@controller + 类中每个方法响应的都是json对象@responseBody
//前提是这个类中全部返回数据都是json对象
//@RestController
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private IncomeRecordService incomeRecordService;

    @Autowired
    private Config config;

    @RequestMapping(value = "/loan/checkPhone")
    @ResponseBody
    public Object checkPhone(HttpServletRequest request,
                                            @RequestParam(value = "phone",required = true) String phone) {
        Map<String,Object> retMap = new HashMap<>();

        //查询手机号是否重复  -->  返回boolean|User|int
        User user = userService.queryUserByPhone(phone);

        //判断用户是否存在
        if (null != user) {
            retMap.put(Constants.ERROR_MESSAGE,"该手机号码已被注册，请更换手机号");
            return retMap;
        }

        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);

        return retMap;
    }

    @PostMapping(value = "/loan/checkCaptcha")
    @ResponseBody
    public Map<String,Object> checkCaptcha(HttpServletRequest request,
                                             @RequestParam(value = "captcha",required = true) String captcha){
        Map<String,Object> retMap = new HashMap<>();

        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.CAPTCHA);

        //查看验证码是否相同
        if (!StringUtils.equalsIgnoreCase(captcha,sessionCaptcha)){
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的图形验证码");
            return retMap;
        }

        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        return retMap;
    }

    @RequestMapping("/loan/register")
    @ResponseBody
    public Object register(HttpServletRequest request,
                           @RequestParam(value = "phone",required = true) String phone,
                           @RequestParam(value = "loginPassword",required = true) String loginPassword,
                           @RequestParam(value = "replayLoginPassword",required = true) String replayLoginPassword){
        Map<String,Object> retMap = new HashMap<>();

        //验证参数
        if (!Pattern.matches("^1[1-9]\\d{9}$",phone)){
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的手机号码");
            return retMap;
        }

        //验证两次密码
        if (!StringUtils.equals(loginPassword,replayLoginPassword)){
            retMap.put(Constants.ERROR_MESSAGE,"两次密码输入不一致");
            return retMap;
        }

        //用户的注册(手机号，登录的密码)
        ResultObject resultObject = userService.register(phone,loginPassword);

        //判断是否注册成功
        if (!StringUtils.equals(Constants.SUCCESS,resultObject.getErrorCode())){
            retMap.put(Constants.ERROR_MESSAGE,"注册失败，请稍后重试...");
            return retMap;
        }

        //将用户的信息存放到session中
        request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(phone));
        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        return retMap;
    }

    @RequestMapping("/loan/verifyRealName")
    @ResponseBody
    public Object verifyRealName(HttpServletRequest request,
                                 @RequestParam(value = "realName",required = true) String realName,
                                 @RequestParam(value = "idCard",required = true) String idCard,
                                 @RequestParam(value = "replayIdCard",required = true) String replayIdCard){
        Map<String,Object> retMap = new HashMap<>();

        //验证参数
        if (!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$",realName)){
            retMap.put(Constants.ERROR_MESSAGE,"真实姓名只支持中文");
            return retMap;
        }

        if (!Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)",idCard)){
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的身份证号码");
            return retMap;
        }

        if (!StringUtils.equals(idCard,replayIdCard)){
            retMap.put(Constants.ERROR_MESSAGE,"两次输入身份证号码不一致");
            return retMap;
        }

        //准备实名认证的参数
        Map<String,Object> paramMap = new HashMap<>();

        //实名认证appkey
        paramMap.put("appkey",config.getRealNameAppkey());

        //真实姓名
        paramMap.put("realName",realName);

        //身份证号码
        paramMap.put("cardNo",idCard);

        //发送请求去验证用户真实信息  -->  返回json格式的字符串
        //String jsonString = HttpCilentUtils.doPost(config.getRealNameUrl(), paramMap);
        String jsonString = "{\"code\":\"10000\",\"charge\":false,\"remain\":1305,\"msg\":\"查询成功\",\"result\":{\"error_code\":0,\"reason\":\"成功\",\"result\":{\"realname\":\"乐天磊\",\"idcard\":\"350721197702134399\",\"isok\":true}}}";


        //解析json格式的字符串,使用fastjson
        //将json格式的字符串转换为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取指定key所对应的value,获取code通信标识
        String code = jsonObject.getString("code");

        //判断通信是否成功
        if(StringUtils.equals("10000",code)){
            //说明通信成功

            //说明isok是否匹配
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");

            //判断真实姓名与身份证号码是否一致
            if (isok) {
                //更新用户的信息
                User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

                User user = new User();
                user.setId(sessionUser.getId());
                user.setName(realName);
                user.setIdCard(idCard);

                int modifyUserConunt = userService.modifyUserById(user);
                if (modifyUserConunt > 0){
                    //更新session中用户的信息
                    request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(sessionUser.getPhone()));
                    retMap.put(Constants.ERROR_MESSAGE,Constants.OK);

                } else {
                    retMap.put(Constants.ERROR_MESSAGE,"系统繁忙请稍后重试");
                    return retMap;
                }
            } else {
                retMap.put(Constants.ERROR_MESSAGE,"真实姓名和身份证号码不匹配");
                return retMap;
            }

        } else {
            //通信失败
            retMap.put(Constants.ERROR_MESSAGE,"通信失败，请稍后重试");
            return retMap;
        }


        return retMap;
    }

    @RequestMapping("/loan/myFinanceAccount")
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(user.getId());
        return financeAccount;
    }

    @RequestMapping("/loan/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
                        @RequestParam(value = "phone",required = true) String phone,
                        @RequestParam(value = "loginPassword",required = true) String loginPassword){
        Map<String,Object> retMap = new HashMap<>();

        //验证参数
        if (!Pattern.matches("^1[1-9]\\d{9}$",phone)){
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的手机号码");
            return retMap;
        }

        //用户的登录（用户的手机号，密码）
        User user = userService.login(phone,loginPassword);

        //判断是否登录成功
        if (null == user){
            retMap.put(Constants.ERROR_MESSAGE,"用户名或密码输入有误，请重新输入");
            return retMap;
        }

        //将用户信息放入到session当中
        request.getSession().setAttribute(Constants.SESSION_USER,user);

        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);

        return retMap;
    }

    @RequestMapping("/loan/logout")
    public String logout(HttpServletRequest request){
        //让session失效
        request.getSession().invalidate();
        //重定向到首页
        return "redirect:/index";
    }

    @RequestMapping("/loan/loginInfo")
    public String loginInfo(HttpServletRequest request, Model model){

        //获取历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);

        //获取平台注册总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT,allUserCount);

        //获取平台累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY,allBidMoney);

        return "login";
    }

    @RequestMapping("/loan/myCenter")
    public String myCenter(HttpServletRequest request,Model model){

        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(user.getId());

        //将用户对应资金信息放入model中
        model.addAttribute("financeAccount",financeAccount);

        Map<String,Object> paramMap = new HashMap<>();
        //起始页下标
        paramMap.put("currentPage",0);
        //用户id
        paramMap.put("uid",user.getId());
        //条数
        paramMap.put("pageSize",5);
        //最近投资(用户id,每页显示5个)
        List<BidInfo> bidInfoList = bidInfoService.queryInvestListByUid(paramMap);

        //最近充值
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryRechargeListByUid(paramMap);

        //最近收益
        List<IncomeRecord> incomeRecordList = incomeRecordService.queryIncomeRecordListByUid(paramMap);

        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("incomeRecordList",incomeRecordList);

        return "myCenter";
    }

    @RequestMapping("/loan/myIncome")
    public String myIncome(HttpServletRequest request,Model model,
                           @RequestParam(value = "currentPage",required = false) Integer currentPage){
        if (null == currentPage) {
            currentPage = 1;
        }

        Map<String ,Object> paramMap = new HashMap<>();
        int pageSize = 5;
        paramMap.put("pageSize",pageSize);
        paramMap.put("currentPage",(currentPage - 1)* pageSize);
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        paramMap.put("uid",sessionUser.getId());

        PageinatinoVO<IncomeRecord> pageinatinoVO = incomeRecordService.queryIncomeRecordByPage(paramMap);

        //总页数
        int totalPage = pageinatinoVO.getTotal().intValue() / pageSize;

        if ((pageinatinoVO.getTotal().intValue() % pageSize)!=0){
            totalPage = totalPage + 1;
        }

        model.addAttribute("currentPage",currentPage);
        model.addAttribute("incomeRecordList",pageinatinoVO.getDataList());
        model.addAttribute("totalRows",pageinatinoVO.getTotal());
        model.addAttribute("totalPage",totalPage);

        return "myIncome";
    }

}
