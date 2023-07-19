package com.xie.common.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

//工具类
public class Utils {

    //当开始值设为空或者小于1时，将启用默认值为1
    public static int defaultPageNo(Integer offset){
        int pageNo = offset;
        if(offset == null || offset < 1){
            pageNo = 1;
        }
        return pageNo;
    }

    //当每页大小值设为空或者小于1时，将启用默认值为1
    public static int defaultPageSize(Integer rows){
        int pageSize = rows;
        if(rows == null || rows < 1){
            pageSize = 1;
        }
        return pageSize;
    }

    //手机号脱敏
    public static String  desensitizePhone(String phone){
        String desPhone = "***********";
        if(phone != null && phone.length() == 11){
            desPhone = phone.substring(0,3) + "******" + phone.substring(9,11);
        }
        return desPhone;
    }

    //手机号格式
    public static boolean checkPhone(String phone){
        boolean matches = false;
        if(phone != null && phone.length() == 11){
            //手机号格式第一位数为1，第二位是以1-9的数字，其余是9位是0-9的随机数字
            matches = Pattern.matches("^1[1-9]\\d{9}$", phone);
        }
        return matches;
    }

    //BigDecimal 类型比较大小
    public static boolean ge(BigDecimal a,BigDecimal b){
        if(a == null || b == null){
            throw new RuntimeException("参数BigDecimal为null");
        }
        return a.compareTo(b) >= 0;
    }

}
