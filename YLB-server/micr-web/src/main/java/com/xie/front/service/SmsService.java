package com.xie.front.service;

public interface SmsService {
    //注册手机号是否发送成功
    boolean sendSms(String phone);

    //比较验证码是否正确
    boolean checkCode(String phone,String code);
}
