package com.xie.task;


import com.xie.api.service.IncomeService;
import com.xie.common.utils.HttpClientUtils;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component("taskManager")
public class TaskManager {

//    @Scheduled(cron = "0/5 * * * * ?")
//    public void test(){
//        System.out.println("当前时间是："+new Date());
//    }

    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    private IncomeService incomeService;

//    @Scheduled(cron = "0 0 1 * * ?")
//    public void invokeGenerateIncomePlan(){
//        incomeService.generateIncomePlan();
//    }


    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeBackIncome(){
        incomeService.backIncome();
    }


    //补单接口
    @Scheduled(cron = "0 0/20 * * * ?")
    public void invokeQuery(){
        try{
            String url = "http://localhost:9000/pay/kq/get/notify";
            HttpClientUtils.doGet(url);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
