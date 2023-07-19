package com.xie.front.interceptors;

import com.alibaba.fastjson.JSON;
import com.xie.common.enums.Rcode;
import com.xie.common.utils.JwtUtils;
import com.xie.front.vo.RespResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {

    private String secret = "";

    public TokenInterceptor(String secret){
        this.secret = secret;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果访问的方法是OPTIONS,则放行
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }

        boolean requestSend = false;
        try{
            //取出请求头中的token
            String headerUid = request.getHeader("uid");
            String headerToken = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(headerToken)) {
                String jwt = headerToken.substring(7);

                //读取jwt
                JwtUtils jwtUtils = new JwtUtils(secret);
                Claims claims = jwtUtils.readJwt(jwt);

                Integer uid = claims.get("uid",Integer.class);
                if(headerUid.equals(String.valueOf(uid))){
                    //token中的uid和请求头中uid相同，可以处理
                    requestSend = true;
                }
            }
        }catch (Exception e){
            requestSend = false;
            e.printStackTrace();
        }

        if(requestSend == false){
            RespResult respResult = RespResult.fail();
            respResult.setRcode(Rcode.TOKEN_INVALID);

            //使用HttpServletResponse输出json
            String respJson = JSON.toJSONString(respResult);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(respJson);
            out.flush();
            out.close();
        }


        return requestSend;
    }
}
