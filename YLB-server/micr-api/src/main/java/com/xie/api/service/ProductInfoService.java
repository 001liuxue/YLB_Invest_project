package com.xie.api.service;

import com.xie.api.model.ProductInfo;
import com.xie.api.vo.MultProductInfo;

import java.util.List;

public interface ProductInfoService {

    //分页查询
    List<ProductInfo> queryByTypeLimit(Integer pType,Integer pageNo,Integer pageSize);

    //查询首页产品信息
    MultProductInfo queryIndexPageProducts();

    //查询产品总数
    Integer queryProductSumByType(Integer pType);

    //查询产品的详细信息
    ProductInfo queryById(Integer productId);
}
