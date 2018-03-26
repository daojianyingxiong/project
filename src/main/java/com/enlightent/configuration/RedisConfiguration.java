package com.enlightent.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2017/5/17.
 */
@Configuration
public class RedisConfiguration{
    @Bean(name = "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(30);
        config.setMaxTotal(30);
        config.setMinIdle(2);

        return config;
    }

    @Bean("connectionFactory")
    @Scope("prototype")
    @Autowired
    public JedisConnectionFactory connectionFactory(@Value("${redis.host}") String host,
                                                    @Value("${redis.port}") Integer port,
                                                    @Qualifier("jedis.pool.config") JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisPoolConfig);
        factory.setUsePool(true);
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword("redis601");

        return factory;
    }


    @Bean("jedisTemplate")
    @Autowired
    public RedisTemplate jedisTemplate(@Qualifier("connectionFactory") JedisConnectionFactory connectionFactory) {
        connectionFactory.setDatabase(0);
        RedisTemplate redisTemplate = new RedisTemplate();
        StringRedisSerializer s = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(s);
        redisTemplate.setValueSerializer(s);
        redisTemplate.setHashKeySerializer(s);
        redisTemplate.setHashValueSerializer(s);

        return redisTemplate;
    }

    @Bean("monitorJedisTemplate")
    @Autowired
    public RedisTemplate monitorJedisTemplate(@Qualifier("connectionFactory") JedisConnectionFactory connectionFactory) {
        connectionFactory.setDatabase(9);
        RedisTemplate redisTemplate = new RedisTemplate();
        StringRedisSerializer s = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(s);
        redisTemplate.setValueSerializer(s);
        redisTemplate.setHashKeySerializer(s);
        redisTemplate.setHashValueSerializer(s);

        return redisTemplate;
    }

   /* @Bean("jedisCacheTemplate")
    @Autowired
    public RedisTemplate jedisCacheTemplate(@Qualifier("connectionFactory") JedisConnectionFactory connectionFactory) {
    	connectionFactory.setDatabase(6);
        RedisTemplate redisTemplate = new RedisTemplate();
        StringRedisSerializer s = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    	
    }*/




}
