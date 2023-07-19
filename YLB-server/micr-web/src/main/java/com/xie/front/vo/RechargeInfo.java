package com.xie.front.vo;

import com.xie.api.model.RechargeRecord;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;

public class RechargeInfo {
    private Integer uid;
    private String rechargeStatus = "未知";
    private BigDecimal rechargeMoney;
    private String rechargeTime = "-";

    public RechargeInfo(RechargeRecord rechargeRecord) {
        this.uid = rechargeRecord.getUid();
        this.rechargeMoney = rechargeRecord.getRechargeMoney();
        if (rechargeRecord.getRechargeTime() != null){
            this.rechargeTime = DateFormatUtils.format(rechargeRecord.getRechargeTime(),"yyyy-MM-dd");
        }

        switch (rechargeRecord.getRechargeStatus()){
            case 0:
                rechargeStatus = "充值中";
                break;
            case 1:
                rechargeStatus = "充值成功";
                break;
            case 2:
                rechargeStatus = "充值失败";
                break;
        }
    }

    public Integer getUid() {
        return uid;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    public String getRechargeTime() {
        return rechargeTime;
    }
}
