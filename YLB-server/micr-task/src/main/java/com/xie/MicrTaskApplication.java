package com.xie;

import com.xie.task.TaskManager;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


// Generated by https://start.springboot.io
// 优质的 spring/boot/data/security/cloud 框架中文文档尽在 => https://springdoc.cn


@EnableDubbo
@EnableScheduling
@SpringBootApplication
public class MicrTaskApplication {

    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(MicrTaskApplication.class, args);
        TaskManager taskManager = (TaskManager) ac.getBean("taskManager");

        taskManager.invokeBackIncome();
    }

}
