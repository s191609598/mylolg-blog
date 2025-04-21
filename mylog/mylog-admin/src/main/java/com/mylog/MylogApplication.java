package com.mylog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author pss
 */
@SpringBootApplication
@MapperScan(
        {"com.baomidou.mybatisplus.samples.quickstart.mapper",
                "com.baomidou.mybatisplus.core.mapper",
                "com.mylog.system.dao"}
)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableScheduling
public class MylogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MylogApplication.class, args);
    }

}
