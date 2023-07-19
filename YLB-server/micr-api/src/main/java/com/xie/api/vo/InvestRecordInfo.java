package com.xie.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class InvestRecordInfo implements Serializable {
    private Integer id;
    private String phone;
    private BigDecimal bidMoney;
    private String bidTime;

    public InvestRecordInfo(Integer id, String phone, BigDecimal bidMoney, String bidTime) {
        this.id = id;
        this.phone = phone;
        this.bidMoney = bidMoney;
        this.bidTime = bidTime;
    }

    public InvestRecordInfo() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(BigDecimal bidMoney) {
        this.bidMoney = bidMoney;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }
}
