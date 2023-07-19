package com.xie.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class BaseInfo implements Serializable {
    private BigDecimal avgRate;//平均利率
    private BigDecimal sumBidMoney;//累计金额
    private Integer userCount;//用户数


    public BaseInfo() {
    }

    public BaseInfo(BigDecimal avgRate, BigDecimal sumBidMoney, Integer userCount) {
        this.avgRate = avgRate;
        this.sumBidMoney = sumBidMoney;
        this.userCount = userCount;
    }


    public BigDecimal getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(BigDecimal avgRate) {
        this.avgRate = avgRate;
    }

    public BigDecimal getSumBidMoney() {
        return sumBidMoney;
    }

    public void setSumBidMoney(BigDecimal sumBidMoney) {
        this.sumBidMoney = sumBidMoney;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
