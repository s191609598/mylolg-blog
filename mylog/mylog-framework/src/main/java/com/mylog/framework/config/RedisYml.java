package com.mylog.framework.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author pss
 * @date 2025/3/26
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisYml {

    @Value("${spring.redis.database}")
    private String database;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

}
