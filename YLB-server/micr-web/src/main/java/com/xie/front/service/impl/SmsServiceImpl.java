package com.xie.front.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xie.common.constants.RedisConstants;
import com.xie.front.config.SmsConfiguration;
import com.xie.front.service.SmsService;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(value = "smsServiceImpl")
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsConfiguration smsConfiguration;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean sendSms(String phone) {
        //判断是否发送成功
        boolean send = false;

        //生成随机短信验证码
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println("注册验证码为:"+code);

        //替换配置文件中的%s占位符
//        String content = String.format(smsConfiguration.getContent(), code);
        String content = smsConfiguration.getContent();

        //创建httpclient发送url
        CloseableHttpClient client = HttpClients.createDefault();

        //拼接url
        //get的url
        String url = smsConfiguration.getUrl() + "?mobile=" + phone + "&content=" + content;
        String authorization = smsConfiguration.getAuthorization();
        HttpPost post = new HttpPost(url);

        //添加头
        post.addHeader("Authorization",authorization);

        try{
//            CloseableHttpResponse response = client.execute(post);
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //得到返回数据 json格式
//                String text = EntityUtils.toString(response.getEntity());
            String text = "{\n" +
                            "  \"ReturnStatus\": \"Success\",\n" +
                            "  \"Message\": \"ok\",\n" +
                            "  \"RemainPoint\": 420842,\n" +
                            "  \"TaskID\": 18424321,\n" +
                            "  \"SuccessCounts\": 1\n" +
                            "}";
                if (StringUtils.isNotBlank(text)) {
                    JSONObject jsonObject = JSON.parseObject(text);
                    //返回值为sucess，则证明返回成功
                    if("Success".equals(jsonObject.getString("ReturnStatus"))){
                        send = true;

                        //将随机生成的验证码存储到redis中,有效期5分钟
                        String key = RedisConstants.KEY_SMS_CODE_REG + phone;
                        stringRedisTemplate.boundValueOps(key).set(code,5, TimeUnit.MINUTES);
                    }
                }
//            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return send;
    }

    //比较验证码是否正确
    @Override
    public boolean checkCode(String phone, String code) {
        String key = RedisConstants.KEY_SMS_CODE_REG + phone;
        //查询redis，得到验证码
        String redis_code = stringRedisTemplate.boundValueOps(key).get();

        if(code.equals(redis_code)){
            return true;
        }

        return false;
    }
}
