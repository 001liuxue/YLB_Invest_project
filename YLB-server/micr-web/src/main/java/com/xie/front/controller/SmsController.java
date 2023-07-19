package com.xie.front.controller;

import com.xie.common.constants.RedisConstants;
import com.xie.common.enums.Rcode;
import com.xie.common.utils.Utils;
import com.xie.front.service.SmsService;
import com.xie.front.vo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "短信验证码发送功能")
@RestController
@RequestMapping("/v1")
public class SmsController extends BaseController{

    @Autowired
    @Qualifier(value = "smsServiceImpl")
    private SmsService smsService;

    @Autowired
    @Qualifier(value = "loginSmsServiceImpl")
    private SmsService loginSmsService;

    //注册时短信发送
    @ApiOperation(value = "注册时发送短信验证码")
    @GetMapping("/code/register")
    public RespResult sendRegisterMsg(@RequestParam("phone") String phone){
        RespResult respResult = RespResult.fail();

        //判断phone格式是否正确
        if (Utils.checkPhone(phone)) {
            //判断redis中是否具有该验证码，若没有则发送，若有说明之前发送过，还在有效期中，不需要再次发送，验证码仍然可以继续使用
            String key = RedisConstants.KEY_SMS_CODE_REG + phone;
            if (stringRedisTemplate.hasKey(key)) {
                respResult = RespResult.ok();
                respResult.setRcode(Rcode.CODE_IS_SENT);
            }else {
                //不存在发送
                boolean isSucess = smsService.sendSms(phone);
                //发送成功
                if (isSucess) {
                    respResult = RespResult.ok();
                }
            }

        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERR);
        }

        return respResult;
    }


    //登录时短信发送
    @ApiOperation(value = "登录时发送短信验证码")
    @GetMapping("/code/login")
    public RespResult sendLoginMsg(@RequestParam("phone") String phone){
        RespResult respResult = RespResult.fail();

        //判断phone格式是否正确
        if (Utils.checkPhone(phone)) {
            //判断redis中是否具有该验证码，若没有则发送，若有说明之前发送过，还在有效期中，不需要再次发送，验证码仍然可以继续使用
            String key = RedisConstants.KEY_SMS_CODE_LOG + phone;
            if (stringRedisTemplate.hasKey(key)) {
                respResult = RespResult.ok();
                respResult.setRcode(Rcode.CODE_IS_SENT);
            }else {
                //不存在发送
                boolean isSucess = loginSmsService.sendSms(phone);
                //发送成功
                if (isSucess) {
                    respResult = RespResult.ok();
                }
            }

        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERR);
        }

        return respResult;
    }
}























