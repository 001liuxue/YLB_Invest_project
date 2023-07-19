package com.xie.dataservice.service;

import com.xie.api.model.ProductInfo;
import com.xie.api.service.ProductInfoService;
import com.xie.api.vo.MultProductInfo;
import com.xie.common.constants.YLBConstants;
import com.xie.common.utils.Utils;
import com.xie.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = ProductInfoService.class,version = "1.0")
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoMapper productInfoMapper;

    //分页查询产品信息
    @Override
    public List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize) {
        List<ProductInfo> productInfo = new ArrayList<>();
        if(pType == 0 || pType == 1 || pType == 2){
            pageNo = Utils.defaultPageNo(pageNo);
            pageSize = Utils.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            productInfo = productInfoMapper.queryByTypeLimit(pType, offset, pageSize);
            return productInfo;
        }else {
            return null;
        }
    }
    //多次查询页面产品信息
    @Override
    public MultProductInfo queryIndexPageProducts() {

        MultProductInfo multProductInfo = new MultProductInfo();
        List<ProductInfo> xinshoubao = productInfoMapper.queryByTypeLimit(YLBConstants.PRODUCT_TYPE_XINSHOUBAO, 0, 1);
        List<ProductInfo> youxuan = productInfoMapper.queryByTypeLimit(YLBConstants.PRODUCT_TYPE_YOUXUAN, 0, 3);
        List<ProductInfo> sanbiao = productInfoMapper.queryByTypeLimit(YLBConstants.PRODUCT_TYPE_SANBIAO, 0, 3);

        multProductInfo.setXinshoubao(xinshoubao);
        multProductInfo.setYouxuan(youxuan);
        multProductInfo.setSanbiao(sanbiao);

        return multProductInfo;
    }

    @Override
    public Integer queryProductSumByType(Integer pType) {
        int count = 0;

        //产品类型在三个之一
        if (pType == 0 || pType == 1 || pType == 2) {
            count = productInfoMapper.queryProductSumByType(pType);
        }
        return count;
    }

    @Override
    public ProductInfo queryById(Integer productId) {
        ProductInfo productInfo = null;
        if (productId != null){
            productInfo = productInfoMapper.selectByPrimaryKey(productId);
        }
        return productInfo;
    }


}
