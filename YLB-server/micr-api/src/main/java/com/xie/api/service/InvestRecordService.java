package com.xie.api.service;

import com.xie.api.vo.InvestRecordInfo;

import java.util.List;

public interface InvestRecordService {
    //查询某个产品的详细投资记录
    List<InvestRecordInfo> queryById(Integer productId,Integer pageNo,Integer pageSize);

}
