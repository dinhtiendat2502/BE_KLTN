package com.app.toeic.config;

import com.app.toeic.external.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPooled;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RedisConfig {
    SystemConfigService systemConfigService;
    private static final String REDIS_USERNAME = "default";

    @Lazy
    @Bean
    public JedisPooled jedisPooled() {
        var host = systemConfigService.getConfigValue("REDIS_HOST");
        var port = Integer.parseInt(systemConfigService.getConfigValue("REDIS_PORT"));
        var password = systemConfigService.getConfigValue("REDIS_PASSWORD");
        return new JedisPooled(new HostAndPort(host, port),
                DefaultJedisClientConfig.builder().user(REDIS_USERNAME).password(password).ssl(true).build());
    }
}
