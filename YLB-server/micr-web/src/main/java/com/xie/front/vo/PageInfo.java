package com.xie.front.vo;

import java.io.Serializable;

public class PageInfo implements Serializable {
    //当前页码
    private Integer pageNo;
    //每页大小
    private Integer pageSize;
    //页数
    private Integer totalPage;
    //总记录数
    private Integer recordSum;

    public PageInfo() {
    }

    public PageInfo(Integer pageNo, Integer pageSize, Integer recordSum) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.recordSum = recordSum;

        if(recordSum % pageSize == 0){
            this.totalPage = recordSum / pageSize;
        }else {
            this.totalPage = recordSum / pageSize + 1;
        }
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getRecordSum() {
        return recordSum;
    }

    public void setRecordSum(Integer recordSum) {
        this.recordSum = recordSum;
    }
}
