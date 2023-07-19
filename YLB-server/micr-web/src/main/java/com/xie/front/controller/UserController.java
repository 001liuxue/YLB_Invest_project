package com.xie.front.controller;
import com.xie.api.model.User;

import com.xie.api.vo.UserAccountInfo;
import com.xie.common.enums.Rcode;
import com.xie.common.utils.JwtUtils;
import com.xie.common.utils.Utils;
import com.xie.front.pojo.RealName;
import com.xie.front.service.RealNameService;
import com.xie.front.service.SmsService;
import com.xie.front.vo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.management.monitor.StringMonitor;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户功能")
@RestController
@RequestMapping("/v1")
public class UserController extends BaseController{

    @Autowired
    @Qualifier(value = "smsServiceImpl")
    private SmsService smsService;

    @Autowired
    @Qualifier(value = "loginSmsServiceImpl")
    private SmsService loginSmsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RealNameService realNameService;


    @ApiOperation(value = "用户注册")
    @PostMapping("/user/register")
    public RespResult userRegister(@RequestParam("phone") String phone,
                                   @RequestParam("pwd") String pwd,
                                   @RequestParam("code") String code){

        RespResult respResult = RespResult.fail();
        //1.判断手机号格式是否正确
        if(Utils.checkPhone(phone)){
            //1.1 格式正确
            if(pwd != null && pwd.length() == 32){
                //2 条件符合，比较验证码是否正确
                if (smsService.checkCode(phone,code)) {
                    //2.1 进行注册
                    int result = userService.register(phone,pwd);
                    if(result == 1){
                        //验证码正确
                        respResult = RespResult.ok();
                    }else if (result == 2){
                        //该手机号已经注册
                        respResult.setRcode(Rcode.PHONE_EXIST);
                    }else {
                        //请求参数不正确
                        respResult.setRcode(Rcode.REQUEST_PARAMS_ERR);
                    }
                }else{
                    //2.2 验证码错误，返回验证码错误
                    respResult.setRcode(Rcode.CODE_IS_ERR);
                }
            }else{
                //3 条件不符合，返回参数有误
                respResult.setRcode(Rcode.REQUEST_PARAMS_ERR);
            }
        }else {
            // 1.2 格式不正确
            respResult.setRcode(Rcode.PHONE_FORMAT_ERR);
        }
        return respResult;
    }

    @ApiOperation(value = "查询用户是否存在")
    @GetMapping("/user/exist")
    public RespResult queryUserExist(@RequestParam("phone") String phone){
        RespResult respResult = new RespResult();

        //判断手机号格式是否正确
        if (Utils.checkPhone(phone)) {
            //首先判断用户是否存在或者是否已经注册
            User user = userService.queryUserExist(phone);
            //user为空，表示该手机号没有注册
            if(user == null){
                respResult = RespResult.ok();
            }else {
                respResult.setRcode(Rcode.PHONE_EXIST);
            }

        }else{
            //不正确
            respResult.setRcode(Rcode.PHONE_FORMAT_ERR);
        }

        return respResult;
    }


    @ApiOperation(value = "用户登录")
    @PostMapping("/user/login")
    public RespResult userLogin(@RequestParam("phone") String phone,
                                   @RequestParam("pwd") String pwd,
                                   @RequestParam("code") String code) throws Exception {
        RespResult respResult = RespResult.fail();

        //检验手机号、密码
        if(Utils.checkPhone(phone) && pwd != null && pwd.length() == 32){
            //判断验证码是否正确
            if (loginSmsService.checkCode(phone,code)) {
                //从数据库查询用户是否存在
                User user = userService.checkUser(phone,pwd);
                if (user != null) {
                    //用户存在，生成token
                    respResult = RespResult.ok();

                    //存放用户id
                    Map<String, Object> data = new HashMap<>();
                    data.put("uid",user.getId());

                    //生成jwtToken
                    String jwtToken = jwtUtils.createJwt(data, 120);

                    //存放到respResult
                    respResult.setJwtToken(jwtToken);

                    Map<String,Object> mapInfo = new HashMap<>();
                    mapInfo.put("uid",user.getId());
                    mapInfo.put("phone",phone);
                    mapInfo.put("name",user.getName());

                    respResult.setData(mapInfo);

                }else {
                    respResult.setRcode(Rcode.PHONE_PASSWORD_ERR);
                }
            }else {
                respResult.setRcode(Rcode.CODE_IS_ERR);
            }
        }else {
            respResult.setRcode(Rcode.REQUEST_PARAMS_ERR);
        }

        return respResult;
    }


    @ApiOperation(value = "用户实名认证")
    @PostMapping("/user/realname")
    public RespResult realNameCheck(@RequestBody RealName realName){
        RespResult respResult = RespResult.fail();

        //判断手机号格式是否正确
        if (Utils.checkPhone(realName.getPhone())) {
            if(StringUtils.isNotBlank(realName.getName()) && StringUtils.isNotBlank(realName.getIdCard())){
                boolean flag = realNameService.checkRealName(realName.getName(),realName.getIdCard(),realName.getPhone());
                if(flag == true){
                    respResult = RespResult.ok();
                }else {
                    respResult.setRcode(Rcode.REALNAME_ERR);
                }
            }else {
                respResult.setRcode(Rcode.PRODUCT_PTYPE_ERR);
            }
        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERR);
        }
        return respResult;
    }


    @ApiOperation(value = "用户中心")
    @GetMapping("/user/center")
    public RespResult queryUserInfo(@RequestHeader("uid") Integer uid){
        RespResult respResult = RespResult.fail();

        //判断uid是否为空，并且大于0
        if (uid != null && uid > 0) {
            UserAccountInfo userAccountInfo = userService.queryUserInfo(uid);
            if (userAccountInfo != null) {
                respResult = RespResult.ok();

                //不能给用户返回所有数据，要存放在map中，返回需要的数据
                Map<String, Object> map = new HashMap<>();
                map.put("name",userAccountInfo.getName());
                map.put("phone",userAccountInfo.getPhone());
                map.put("headerUrl",userAccountInfo.getHeaderImage());
                map.put("money",userAccountInfo.getAvailableMoney());
                if(userAccountInfo.getLastLoginTime() != null){
                    map.put("loginTime", DateFormatUtils.format(userAccountInfo.getLastLoginTime(),"yyyy-MM-dd HH:mm:ss"));
                }else {
                    map.put("loginTime", "-");
                }

                respResult.setData(map);
            }
        }


        return respResult;
    }



}
