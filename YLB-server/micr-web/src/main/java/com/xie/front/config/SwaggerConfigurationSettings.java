package com.xie.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurationSettings {

    //创建docket对象
    @Bean
    public Docket docket(){
        //1.创建docket对象
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        //2.创建api的信息
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("盈利宝")
                .version("1.0")
                .description("前后端理财项目")
                .build();

        //3.设置使用apiInfo
        docket = docket.apiInfo(apiInfo);

        //4.设置参与文档生成的包
        docket.select().apis(RequestHandlerSelectors.basePackage("com.xie.front.controller")).build();

        return docket;
    }

}
