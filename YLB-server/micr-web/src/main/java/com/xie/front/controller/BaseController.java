package com.xie.front.controller;

import com.xie.api.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

//公共Controller接口
public class BaseController {

    //声明公共的方法，属性等
    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    @DubboReference(interfaceClass = BaseInfoService.class,version = "1.0")
    protected BaseInfoService baseInfoService;

    @DubboReference(interfaceClass = ProductInfoService.class, version = "1.0")
    protected ProductInfoService productInfoService;

    @DubboReference(interfaceClass = InvestRecordService.class, version = "1.0")
    protected InvestRecordService investRecordService;

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;

    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    protected RechargeService rechargeService;

    @DubboReference(interfaceClass = InvestService.class,version = "1.0")
    protected InvestService investService;



}
