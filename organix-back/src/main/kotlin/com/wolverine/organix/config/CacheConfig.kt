package com.wolverine.organix.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory

@Configuration
@EnableCaching
class CacheConfig {
    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        return RedisCacheManager.builder(connectionFactory).build()
    }
}

