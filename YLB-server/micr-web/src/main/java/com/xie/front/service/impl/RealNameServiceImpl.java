package com.xie.front.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xie.api.service.UserService;
import com.xie.common.constants.RedisConstants;
import com.xie.front.config.RealNameConfiguration;
import com.xie.front.service.RealNameService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RealNameServiceImpl implements RealNameService {

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;

    @Autowired
    private RealNameConfiguration realNameConfiguration;


    @Override
    public boolean checkRealName(String name, String idCard, String phone) {
        //设置默认为false
        boolean flag = false;

        //创建HttpClient
        CloseableHttpClient client = HttpClients.createDefault();

        //拼接url
        String url = realNameConfiguration.getUrl() + "?idcard=" + idCard + "&name=" + name;
        String authorization = realNameConfiguration.getAuthorization();

        HttpPost post = new HttpPost(url);

        //添加头
        post.addHeader("Authorization",authorization);

        try{
            CloseableHttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String text = EntityUtils.toString(response.getEntity());
//            String text = "{\n" +
//                    "  \"ReturnStatus\": \"Success\",\n" +
//                    "  \"Message\": \"ok\",\n" +
//                    "  \"RemainPoint\": 420842,\n" +
//                    "  \"TaskID\": 18424321,\n" +
//                    "  \"SuccessCounts\": 1\n" +
//                    "}";
                if (StringUtils.isNotBlank(text)) {
                    JSONObject jsonObject = JSON.parseObject(text);
                    //返回值为0，则证明返回成功
                    if("0".equals(jsonObject.getString("code"))){
                        flag = true;

                        //更新数据库
                        flag = userService.modifyRealName(name,idCard,phone);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }
}
