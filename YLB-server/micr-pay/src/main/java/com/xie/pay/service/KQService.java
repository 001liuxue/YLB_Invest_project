package com.xie.pay.service;

import com.xie.api.model.User;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

public interface KQService {
    User queryUserById(Integer uid);

    Map<String, String> generateFormData(Integer uid,String phone, BigDecimal rechargeMoney);

    boolean addRecharge(Integer uid, BigDecimal rechargeMoney, String orderId);

    void addOrderIdToRedis(String orderId);

    void handleNotify(HttpServletRequest request);

    void handleOrder(HttpServletRequest request);
}
