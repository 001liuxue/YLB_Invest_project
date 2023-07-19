package com.xie.front.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsConfiguration {
    private String url;
    private String Authorization;
    private String content;
    private String loginContent;

    public SmsConfiguration() {
    }

    public SmsConfiguration(String url, String authorization, String content, String loginContent) {
        this.url = url;
        Authorization = authorization;
        this.content = content;
        this.loginContent = loginContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLoginContent() {
        return loginContent;
    }

    public void setLoginContent(String loginContent) {
        this.loginContent = loginContent;
    }
}
