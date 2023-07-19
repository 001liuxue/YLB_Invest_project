package com.xie.dataservice.mapper;

import com.xie.api.model.BidInfo;
import com.xie.api.vo.InvestRecordInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {

    BigDecimal querySumBidMoney();

    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    List<InvestRecordInfo> selectById(Integer productId, Integer offset, Integer rows);

    List<BidInfo> selectByProductId(@Param("pid") Integer pid);
}