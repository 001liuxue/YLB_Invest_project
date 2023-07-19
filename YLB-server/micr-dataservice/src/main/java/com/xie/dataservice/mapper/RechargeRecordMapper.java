package com.xie.dataservice.mapper;

import com.xie.api.model.RechargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);


    List<RechargeRecord> selectRechargeByUid(@Param("uid") Integer uid,
                                             @Param("offset")  Integer offset,
                                             @Param("rows") Integer rows);

    RechargeRecord selectRechargeByOrderId(@Param("orderId") String orderId);

    int updateStatusById(@Param("id")Integer id,@Param("newStatus")Integer newStatus);
}