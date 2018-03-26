package com.enlightent.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianglei on 17-6-27.
 */
public class ParamsUtil {
    public static Map<String, String> channelMap = new HashMap<>();
    static {
        Map<String, String> _channelMap = new HashMap<>(15);
        channelMap.put("iqiyi", "爱奇艺");
        channelMap.put("youku", "优酷");
        channelMap.put("tudou", "土豆");
        channelMap.put("letv", "乐视");
        channelMap.put("qq", "腾讯");
        channelMap.put("sohu", "搜狐");
        channelMap.put("mangguo", "芒果");
        channelMap.put("pptv", "pptv");
//		channelMap.put("acfun", "A站");
//		channelMap.put("bilibili", "B站");

        channelMap = Collections.unmodifiableMap(_channelMap);
    }
}
