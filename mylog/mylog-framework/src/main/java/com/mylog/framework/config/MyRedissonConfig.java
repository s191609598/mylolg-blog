package com.mylog.framework.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author pss
 * @date 2025/3/26
 */
@Configuration
public class MyRedissonConfig {
    @Resource
    private RedisYml redisYml;

    /**
     * 所有对Redisson的使用都是通过RedissonClient对象
     *
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        // 创建配置 指定redis地址及节点信息
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisYml.getHost() + ":" + redisYml.getPort()).setPassword(redisYml.getPassword());
        // 根据config创建出RedissonClient实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
