package com.xie.dataservice.mapper;

import com.xie.api.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    FinanceAccount selectByUid(@Param("uid") Integer uid);

    int updateByUid(@Param("uid") Integer uid,@Param("money") BigDecimal money);

    int updateAccountByUid(@Param("uid") Integer uid,@Param("bidMoney")  BigDecimal bidMoney,@Param("incomeMoney")  BigDecimal incomeMoney);

    int updateRechargeAccount(@Param("uid")Integer uid, @Param("rechargeMoney")BigDecimal rechargeMoney);
}