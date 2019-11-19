package com.zc.web;

import com.alibaba.fastjson.JSONObject;
import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.common.util.DateUtils;
import com.zc.p2p.common.util.HttpCilentUtils;
import com.zc.p2p.model.loan.RechargeRecord;
import com.zc.p2p.model.user.User;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.service.loan.OnlyNumberService;
import com.zc.p2p.service.loan.RechargeRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:RechargeController
 * Package:com.zc.web
 * Description:
 *
 * @date:2019/11/10 16:05
 * @author:youxiang@163.com
 */
@Controller
public class RechargeController {

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private OnlyNumberService onlyNumberService;

    @RequestMapping("/loan/myRecharge")
    public String myRecharge(HttpServletRequest request, Model model,
                             @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        if (null == currentPage) {
            currentPage = 1;
        }

        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("uid", sessionUser.getId());
        Integer pageSize = 5;
        paramMap.put("pageSize", pageSize);
        paramMap.put("currentPage", (currentPage - 1) * pageSize);

        PageinatinoVO<RechargeRecord> pageinatinoVO = rechargeRecordService.queryRechargeByPage(paramMap);

        //总页数
        int totalPage = pageinatinoVO.getTotal().intValue() / pageSize;

        if ((pageinatinoVO.getTotal().intValue() % pageSize) != 0) {
            totalPage = totalPage + 1;
        }

        //总页数
        model.addAttribute("totalPage", totalPage);
        //当前页码
        model.addAttribute("currentPage", currentPage);
        //总条数
        model.addAttribute("totalRows", pageinatinoVO.getTotal());
        //数据
        model.addAttribute("rechargeRecordList", pageinatinoVO.getDataList());

        return "myRecharge";
    }

    @RequestMapping(value = "/loan/toAlipayRecharge")
    public String toAlipayRecharge(HttpServletRequest request, Model model,
                                   @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {
        //从session获取用户的信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //生成一个全局唯一充值订单号 = 时间戳 + redis全局唯一数字
        String rechargeNo = DateUtils.getTimeStamp() + onlyNumberService.getOnylNumber();

        //生成充值记录（状态为充值中）
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeDesc("支付宝充值");

        int addRechargeCount = rechargeRecordService.addRechargeRecord(rechargeRecord);

        if (addRechargeCount > 0){
            //向pay工程的支付方式传递参数
            model.addAttribute("p2p_pay_alipay_url","http://39.107.108.113:9090/pay/ali/alipay");
            model.addAttribute("rechargeNo",rechargeNo);//充值订单号
            model.addAttribute("rechargeMoney",rechargeMoney);//充值金额
            model.addAttribute("subject","支付宝充值");

        } else {
            model.addAttribute("trade_msg","充值人数过多，请稍后重试");
            return "toRechargeBack";
        }

        return "toAlipay";
    }

    @RequestMapping(value = "/loan/alipayBack")
    public String alipayBack(HttpServletRequest request,Model model,
                             @RequestParam(value = "signVerified",required = true) String signVerified,
                             @RequestParam(value = "out_trade_no",required = true) String out_trade_no,
                             @RequestParam(value = "total_amount",required = true) String total_amount,
                             @RequestParam(value = "trade_no",required = true) String trade_no){
        //判断验证是否成功
        if (StringUtils.equals(Constants.SUCCESS,signVerified)){
            //成功

            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("out_trade_no",out_trade_no);

            //调用pay工程的订单查询接口  ->  返回订单的状态
            String jsonString = HttpCilentUtils.doPost("http://39.107.108.113:9090/pay/ali/alipayQuery",paramMap);

            //使用fastjson解析json字符串,解析成json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);

            JSONObject tradeQueryResponse = jsonObject.getJSONObject("alipay_trade_query_response");

            String code = tradeQueryResponse.getString("code");

            if (StringUtils.equals("10000",code)){
                //通信成功
                //获取业务处理结果
                String tradeStatus = tradeQueryResponse.getString("trade_status");
                /*交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、
                TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
                TRADE_SUCCESS（交易支付成功）、
                TRADE_FINISHED（交易结束，不可退款）*/
                if ("TRADE_CLOSED".equals(tradeStatus)){
                    RechargeRecord updateRechargeRecord = new RechargeRecord();
                    updateRechargeRecord.setRechargeNo(out_trade_no);
                    updateRechargeRecord.setRechargeStatus("2");
                    int modifyRechargeCount = rechargeRecordService.modifyRechargeRecordByRechargeNo(updateRechargeRecord);

                    if (modifyRechargeCount<0){
                        //失败
                        model.addAttribute("trade_msg","当前人数过多，请稍后重试");
                        return "toRechargeBack";
                    }
                }

                if ("TRADE_SUCCESS".equals(tradeStatus)){
                    User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
                    paramMap.put("uid",user.getId());
                    paramMap.put("rechargeNo",out_trade_no);
                    paramMap.put("rechargeMoney",total_amount);

                    //支付宝扣款成功，给用户充值
                    int rechargeCount = rechargeRecordService.recharge(paramMap);

                    if (rechargeCount<0){
                        model.addAttribute("trade_msg","当前人数过多，请稍后重试");
                        return "toRechargeBack";
                    }
                }

            } else {
                //通信失败
                model.addAttribute("trade_msg","通信失败，请稍后重试");
                return "toRechargeBack";
            }

        } else {
            model.addAttribute("trade_msg","当前人数过多，请稍后重试");
            return "toRechargeBack";
        }
        return "redirect:/loan/myCenter";
    }

    public String toWxpayRecharge(HttpServletRequest request, Model model,
                                  @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {

        return "";
    }

}
