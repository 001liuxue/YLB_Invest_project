package com.xie.front.vo;

import java.io.Serializable;

public class InvestRankInfo implements Serializable {
    private String phone;
    private Double money;

    public InvestRankInfo() {
    }

    public InvestRankInfo(String phone, Double money) {
        this.phone = phone;
        this.money = money;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
