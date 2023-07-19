package com.xie.api.service;

import com.xie.api.model.User;
import com.xie.api.vo.UserAccountInfo;

public interface UserService {
    //根据手机号判断用户是否存在
    User queryUserExist(String phone);

    int register(String phone, String pwd);

    User checkUser(String phone, String pwd);

    boolean modifyRealName(String name, String idCard, String phone);

    UserAccountInfo queryUserInfo(Integer uid);

    User queryById(Integer uid);
}
