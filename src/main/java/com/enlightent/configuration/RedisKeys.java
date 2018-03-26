package com.enlightent.configuration;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianglei on 17-6-16.
 */
@Component
public class RedisKeys {
    public static final String ONE_MINUTE = "one_minute";
    public static final Long ONE_MINUTE_SECOND = 60L;

    public static final String TWO_MINUTE = "two_minute";
    public static final Long TWO_MINUTE_SECOND = 120L;


    private Map<String, Long> expiresMap = null;

    @PostConstruct
    public void init(){
        expiresMap = new HashMap<>();
        expiresMap.put(ONE_MINUTE, ONE_MINUTE_SECOND);
        expiresMap.put(TWO_MINUTE, TWO_MINUTE_SECOND);
    }

    public Map<String, Long> getExpiresMap(){
        return  this.expiresMap;
    }

}
