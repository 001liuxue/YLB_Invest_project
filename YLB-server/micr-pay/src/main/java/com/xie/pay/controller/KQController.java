package com.xie.pay.controller;

import com.xie.api.model.User;
import com.xie.pay.service.KQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class KQController {

    @Autowired
    private KQService kqService;

    //接收来自前端传入的参数
    @GetMapping("/get/recharge")
    public String getFrontRechargeKQq(Integer uid, BigDecimal rechargeMoney, Model model){
        //初始默认页面是错误页面
        String view = "err";
        //判断uid和rechargeMoney是否符合
        try {
            if(uid != null && uid > 0 && rechargeMoney.doubleValue() > 0 && rechargeMoney != null){
                //根据uid查询用户是否存在
                User user = kqService.queryUserById(uid);
                if (user != null) {
                    Map<String,String> data = kqService.generateFormData(uid, user.getPhone(), rechargeMoney);
                    model.addAllAttributes(data);
                    //创建充值记录
                    kqService.addRecharge(uid,rechargeMoney,data.get("orderId"));

                    //将充值单号存入到redis
                    kqService.addOrderIdToRedis(data.get("orderId"));

                    view = "kqForm";
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }


    //处理异步通知
    @GetMapping("/get/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request){
        kqService.handleNotify(request);
        return "<result>1</result><redirecturl>http://localhost:8080/</redirecturl>";
    }

    //从定时任务查询未处理订单
    @GetMapping("/get/query")
    @ResponseBody
    public String queryKQOrder(HttpServletRequest request){
        kqService.handleOrder(request);
        return "调用了查询接口";
    }



}
