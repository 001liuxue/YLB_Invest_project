package com.xie.common.constants;

public class RedisConstants {
    //投资排行榜前缀
    public static final String KEY_INVEST_RANK = "Invest:Rank";

    //注册时发送验证码手机号前缀
    public static final String KEY_SMS_CODE_REG = "SMS:REG";

    //登录时发送验证码手机号前缀
    public static final String KEY_SMS_CODE_LOG = "SMS:LOG";

    //生成唯一id
    public static final String KEY_ORDER_ID = "ORDERID:SEQ";

    //redis存入订单号
    public static final String KEY_ORDER_ID_SET = "ORDERID:SET";
}
