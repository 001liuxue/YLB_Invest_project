package com.xie.api.service;

import com.xie.api.model.FinanceAccount;

import java.math.BigDecimal;

public interface InvestService {

    int investProduct(Integer uid, Integer productId, BigDecimal money);
}
