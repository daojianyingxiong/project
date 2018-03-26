package com.enlightent.util;

import java.util.*;

public class ChannelUtil {
	public static final Set<String> BRAND_VIDEO;
	public static final Set<String> SHORT_VIDEO;
	static {
		Set<String> _brandVideo = new HashSet<>();
		_brandVideo.add("qq");
		_brandVideo.add("iqiyi");
		_brandVideo.add("youku");
		_brandVideo.add("sohu");
		_brandVideo.add("pptv");
		_brandVideo.add("letv");
		_brandVideo.add("mangguo");
		BRAND_VIDEO = Collections.unmodifiableSet(_brandVideo);
		
		
		Set<String> _shortVideo = new HashSet<>();
		_shortVideo.add("meipai");
		_shortVideo.add("miaopai");
		_shortVideo.add("new_tudou");
		_shortVideo.add("pearvideo");
		_shortVideo.add("toutiao");
		_shortVideo.add("kwai");
		SHORT_VIDEO = Collections.unmodifiableSet(_shortVideo);
	}
	
    private static Map<String, Object> brandVideoChannelFactory(String channel){
		Map<String, Object> map = new HashMap<>();
		map.put("key", channel);
		map.put("doc_count", 0);
		Map<String, Integer> num = new HashMap<>();
		num.put("value", 0);
		map.put("number", num);
		return map;
	}

	public static Set<String> getCorrespondChannel(String type){
		Set<String> set = new HashSet<>();
		if("BRAND_VIDEO".equalsIgnoreCase(type)) {
			set = BRAND_VIDEO;
		}else if ("SHORT_VIDEO".equalsIgnoreCase(type)) {
			set = SHORT_VIDEO;
		}

		return new HashSet<>(set);
	}

	public static void brandVideoRemainChannelList(List brandVideoList, String type){
		Set<String> set = getCorrespondChannel(type);
		Iterator it = brandVideoList.iterator();
		while (it.hasNext()){
			Map map = (Map) it.next();
			Object key = map.get("key");
			if(set.contains(key)){
				set.remove(key);
			}else{
				it.remove();
			}
		}
		for(String channel : set){
			brandVideoList.add(brandVideoChannelFactory(channel));
		}

	}

}
