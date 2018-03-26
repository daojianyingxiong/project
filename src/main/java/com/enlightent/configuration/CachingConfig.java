package com.enlightent.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 17-6-16.
 */
/*@Configuration
@EnableCaching*/
public class CachingConfig extends CachingConfigurerSupport{

    @Override
    public KeyGenerator keyGenerator(){
        return new SimpleKeyGenerator(){

            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(".").append(method.getName());

                StringBuilder paramsSb = new StringBuilder();
                for (Object param : params) {
                    // 如果不指定，默认生成包含到键值中
                    if (param != null) {
                        paramsSb.append('_');
                        paramsSb.append(param.toString());
                    }
                }

                if (paramsSb.length() > 0) {
                    sb.append(paramsSb);
                }
                return sb.toString();
            }
        };
    }
    @Bean
    @Autowired
    public CacheManager cacheManager(@Qualifier("jedisCacheTemplate") RedisTemplate redisTemplate, RedisKeys redisKeys){
        RedisCacheManager rmc = new RedisCacheManager(redisTemplate);
        rmc.setDefaultExpiration(1800);

        rmc.setExpires(redisKeys.getExpiresMap());
        List<String> chcheNames = new ArrayList<>(redisKeys.getExpiresMap().keySet());
        rmc.setCacheNames(chcheNames);

        //过期value的设置
        return rmc;
    }
}
