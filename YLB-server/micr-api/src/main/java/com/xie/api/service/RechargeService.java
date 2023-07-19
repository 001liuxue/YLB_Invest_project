package com.xie.api.service;

import com.xie.api.model.RechargeRecord;

import java.util.List;

public interface RechargeService {
    List<RechargeRecord> queryRechargeByUid(Integer uid,Integer pageNo,Integer pageSize);

    int addRechargeRecord(RechargeRecord record);

    int handleKQNotify(String orderId, String payAmount, String payResult);
}
