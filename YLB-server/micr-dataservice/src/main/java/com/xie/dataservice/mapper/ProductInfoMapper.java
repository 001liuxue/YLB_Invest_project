package com.xie.dataservice.mapper;

import com.xie.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductInfoMapper {

    //查询产品总数
    Integer queryProductSumByType(@Param("pType") Integer pType);

    //分页查询产品信息
    List<ProductInfo> queryByTypeLimit(@Param("pType") Integer pType,
                                       @Param("offset") Integer offset,
                                       @Param("rows") Integer rows);

    //查询产品平均利率
    BigDecimal queryAveRate();

    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);

    int updateById(@Param("productId") Integer productId,@Param("money")  BigDecimal money);

    int updateSelled(@Param("productId")Integer productId);

    List<ProductInfo> selectByTime(@Param("beginTime")Date beginTime,@Param("endTime") Date endTime);

    int updateByProductId(@Param("id")Integer id);
}