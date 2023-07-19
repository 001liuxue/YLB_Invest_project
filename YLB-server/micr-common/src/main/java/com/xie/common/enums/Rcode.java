package com.xie.common.enums;

public enum Rcode {
    UNKNOW(0,"请稍后重试"),
    SUCCESS(200,"请求成功"),
    PRODUCT_PTYPE_ERR(202,"请求参数有误"),
    PRODUCT_OFFLINE(203,"产品已下架"),
    PHONE_FORMAT_ERR(204,"手机号格式不正确"),
    PHONE_EXIST(205,"该手机号已经注册过"),
    CODE_IS_SENT(206,"验证码已经发送，可以继续使用"),
    CODE_IS_ERR(207,"验证码错误"),
    REQUEST_PARAMS_ERR(208,"请求参数有误"),
    PHONE_PASSWORD_ERR(209,"手机号或密码不正确"),
    REALNAME_ERR(210,"实名认证失败"),


    TOKEN_INVALID(300,"toke无效")
    ;


    /*
    * 应答码：
    * 200请求成功
    * 200-300参数有误
    * 300-400服务器请求失败
    * 400-500dubbo调用失败
    * */
    private int code;
    private String msg;

    Rcode() {
    }

    Rcode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
