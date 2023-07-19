package com.xie.front.vo;
import com.xie.common.enums.Rcode;

import java.io.Serializable;
import java.util.List;

public class RespResult implements Serializable {
    //状态码
    private int code;
    //返回信息
    private String msg;
    //数据
    private Object data;
    //数据集合
    private List list;
    //分页记录
    private PageInfo pageInfo;
    //jwtToken
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }


    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    //请求成功
    public static RespResult ok(){
        RespResult respResult = new RespResult();
        respResult.setRcode(Rcode.SUCCESS);
        return respResult;
    }

    //请求失败
    public static RespResult fail(){
        RespResult respResult = new RespResult();
        respResult.setRcode(Rcode.UNKNOW);
        return respResult;
    }

    public void setRcode(Rcode rcode){
        this.code = rcode.getCode();
        this.msg = rcode.getMsg();
    }

    public RespResult() {

    }


    public RespResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

