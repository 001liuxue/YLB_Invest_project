package com.xie.dataservice.mapper;

import com.xie.api.model.User;
import com.xie.api.vo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int countUser();

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUserByPhone(String phone);

    int insertUserAndFinance(User user);

    User checkUser(String phone, String newPassword);

    int updateByPhone(@Param("name") String name,@Param("idCard") String idCard,@Param("phone") String phone);

    UserAccountInfo selectUserAccountById(@Param("uid") Integer uid);
}