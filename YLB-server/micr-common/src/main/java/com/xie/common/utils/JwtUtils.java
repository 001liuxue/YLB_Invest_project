package com.xie.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtUtils {
    //密钥
    private String key;

    public JwtUtils(String key) {
        this.key = key;
    }

    //创建jwt，生成token
    public String createJwt(Map<String,Object> data, Integer minute) throws Exception{
        //当前时间
        Date curDate = new Date();

        //生成密钥
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        //生成jwt
        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(curDate, minute))
                .setIssuedAt(curDate)
                .setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase())
                .addClaims(data)
                .compact();

        return jwt;
    }

    //读取jwt
    public Claims readJwt(String jwt) throws Exception{
        //取出密钥
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        //读取jwt
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                                .parseClaimsJws(jwt).getBody();

        return claims;
    }
}
