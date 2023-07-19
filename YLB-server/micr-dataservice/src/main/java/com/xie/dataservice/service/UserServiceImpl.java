package com.xie.dataservice.service;

import com.xie.api.model.FinanceAccount;
import com.xie.api.model.User;
import com.xie.api.service.UserService;
import com.xie.api.vo.UserAccountInfo;
import com.xie.common.utils.Utils;
import com.xie.dataservice.mapper.FinanceAccountMapper;
import com.xie.dataservice.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Value("${md5.config.salt}")
    private String salt;

    @Override
    public User queryUserExist(String phone) {
        User user = userMapper.selectUserByPhone(phone);
        return user;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int register(String phone, String pwd) {
        int result = 0;//默认0为注册失败，1为注册成功，2为已经注册
        if(Utils.checkPhone(phone)){
            //判断用户是否在数据库
            User queryUser = userMapper.selectUserByPhone(phone);
            if(queryUser == null){
                //不存在，可以注册
                //密码加盐，二次加密
                String newPassword = DigestUtils.md5Hex(pwd + salt);
                User user = new User();
                user.setPhone(phone);
                user.setLoginPassword(newPassword);
                user.setAddTime(new Date());

                userMapper.insertUserAndFinance(user);

                //获取用户主键id:user.getId
                FinanceAccount account = new FinanceAccount();
                account.setUid(user.getId());
                account.setAvailableMoney(new BigDecimal("0"));

                //插入财务数据
                financeAccountMapper.insertSelective(account);

                result = 1;
            }else {
                //用户已经存在，无法注册
                result = 2;
            }

        }

        return result;
    }

    @Override
    public User checkUser(String phone, String pwd) {

        User user = null;

        //对密码进行二次加盐
        String newPassword = DigestUtils.md5Hex(pwd + salt);
        user = userMapper.checkUser(phone,newPassword);



        if (user != null) {
            //更新最近登陆时间
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        }

        return user;
    }

    @Override
    public boolean modifyRealName(String name, String idCard, String phone) {
        int rows = 0;
        rows = userMapper.updateByPhone(name,idCard,phone);
        return rows > 0;
    }

    @Override
    public UserAccountInfo queryUserInfo(Integer uid) {
        UserAccountInfo userAccountInfo = null;
        if (uid != null && uid > 0){
            userAccountInfo = userMapper.selectUserAccountById(uid);
        }
        return userAccountInfo;
    }

    @Override
    public User queryById(Integer uid) {
        User user = null;
        if (uid != null && uid > 0) {
            user = userMapper.selectByPrimaryKey(uid);
        }
        return user;
    }
}
