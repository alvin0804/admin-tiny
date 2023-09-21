package com.alvin.admin.tiny.config;

import com.alvin.admin.tiny.common.config.BaseRedisConfig;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;


/**
 * Redis配置类
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {
}
