package com.xie.api.vo;

import com.xie.api.model.ProductInfo;

import java.io.Serializable;
import java.util.List;

public class MultProductInfo implements Serializable {
    private List<ProductInfo> xinshoubao;
    private List<ProductInfo> youxuan;
    private List<ProductInfo> sanbiao;

    public MultProductInfo() {
    }

    public MultProductInfo(List<ProductInfo> xinshoubao, List<ProductInfo> youxuan, List<ProductInfo> sanbiao) {
        this.xinshoubao = xinshoubao;
        this.youxuan = youxuan;
        this.sanbiao = sanbiao;
    }

    public List<ProductInfo> getXinshoubao() {
        return xinshoubao;
    }

    public void setXinshoubao(List<ProductInfo> xinshoubao) {
        this.xinshoubao = xinshoubao;
    }

    public List<ProductInfo> getYouxuan() {
        return youxuan;
    }

    public void setYouxuan(List<ProductInfo> youxuan) {
        this.youxuan = youxuan;
    }

    public List<ProductInfo> getSanbiao() {
        return sanbiao;
    }

    public void setSanbiao(List<ProductInfo> sanbiao) {
        this.sanbiao = sanbiao;
    }
}
