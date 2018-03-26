package com.enlightent.util;

import com.enlightent.been.StaticString;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static String hideNumbers(String phone){
		StringBuilder sBuilder = new StringBuilder(phone);
		StringBuilder replace = sBuilder.replace(3, 7, "****");
		return replace.toString();
	}
	
	public static Map<String, Long> playTimesStages(Long playTimes) {
		int length = playTimes.toString().length();
		if (length > 10) {
			return playTimesStages2(playTimes);
		}
		return playTimesStages1(playTimes);
	}
	
	private static Map<String, Long> playTimesStages1(Long playTimes) {
		int length = playTimes.toString().length();
		
		Long p = (long) Math.pow(10, length - 1);
		Long s1 = p / 10;
		
		Long s4 = playTimes / p * p;
		
		Long s3 = p * 5;
		Long s2 = p * 2;
		if (s3 >= s4) {
			s3 = p * 2;
			s2 = p;
		}
		
		if (s3.equals(s4)) {
			s3 = p;
			s2 = p / 2;
		}
		
		if (s2 >= s4) {
			s3 = s2 / 2;
			s2 = 2 * (long) Math.pow(10, s3.toString().length() - 1);
		}
		
		Long s5 = s4 + p;
		Map<String, Long> map = new HashMap<>();
		map.put("s1", s1);
		map.put("s2", s2);
		map.put("s3", s3);
		map.put("s4", s4);
		map.put("s5", s5);
		return map;
	}
	
	private static Map<String, Long> playTimesStages2(Long playTimes) {
		int length = playTimes.toString().length();
		
		Long p = (long) Math.pow(10, length - 2);
		Long s1 = p;
		
		Long s4 = playTimes / p * p;
		Long denominator = 5000000000L;
		Long count = s4 / denominator;
		Long s3 = count * denominator;
		if (s3.equals(s4)) {
			count --;
			s3 = count * denominator;
		}
		Long s2 = (count - 1) * denominator;
		if (count < 2) {
			s2 = 2000000000L;
		}
		
		Long s5 = s4 + p;
		Map<String, Long> map = new HashMap<>();
		map.put("s1", s1);
		map.put("s2", s2);
		map.put("s3", s3);
		map.put("s4", s4);
		map.put("s5", s5);
		return map;
	}
	
	public static void main(String[] args) {
//		playTimesStages(5076477537L);
		Map<String, Long> playTimesStages2 = playTimesStages(192000000L);
		System.out.println(playTimesStages2);
	}
	
	public static String filterEmoji(String source) {
		if (source != null) {
			source = filterEmoji2(source);
			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
					Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				source = emojiMatcher.replaceAll(" ");
				return source;
			}
			return source;
		}
		return source;
	}
	
	private static boolean isNotEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji2(String source) {
		int len = source.length();
		StringBuilder buf = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isNotEmojiCharacter(codePoint)) {
				buf.append(codePoint);
			} else {
				buf.append(" ");
			}
		}
		return buf.toString();
	}
	
	public static String removeParams(String url) {
		if (url.contains("?")) {
			url = url.substring(0, url.indexOf("?"));
		}
		return url;
	}
	
	public static boolean judgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			for (String mobileAgent : mobileAgents) {
				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
	

	public static Integer StringToInteger(String s) {
		int parseInt = 0;
		try {
			parseInt = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return parseInt;
	}
	
	public static Double StringToDouble(String s) {
		Double parseDouble = 0.0;
		try {
			parseDouble = Double.parseDouble(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return parseDouble;
	}

	/**
	 * 获取 形式 如"剧名 (类型)" name 实际的剧名
	 * 如 "夏至未至 (电视剧)" 返回 "夏至未至"
	 * @param name "剧名 (类型)" 如 "夏至未至 (电视剧)"
	 * @return 实际的剧名
	 */
	public static  String realName(String name) {
		if (name.endsWith(StaticString.MOVIE)) {
			name = name.substring(0, name.length() - StaticString.MOVIE.length());
		} else if (name.endsWith(StaticString.TV)) {
			name = name.substring(0, name.length() - StaticString.TV.length());
		} else if (name.endsWith(StaticString.HUA_XU)) {
			name = name.substring(0, name.length() - StaticString.HUA_XU.length());
		} else if (name.endsWith(StaticString.ART)) {
			name = name.substring(0, name.length() - StaticString.ART.length());
		} else if (name.endsWith(StaticString.ANIMATION)) {
			name = name.substring(0, name.length() - StaticString.ANIMATION.length());
		}
		return name;
	}

	/**
	 * 获取 "剧名 (类型)"的英文key
	 * 如 "夏至未至 (电视剧)" 返回 "tv"
	 *
	 * @param name "剧名 (类型)" 如 "夏至未至 (电视剧)"
	 * @return 类型对应的英文key
	 */
	public static String getFirstKey(String name) {
		String firstKey = "";
		if (name.endsWith(StaticString.MOVIE)) {
			firstKey = "movie";
		} else if (name.endsWith(StaticString.TV)) {
			firstKey = "tv";
		} else if (name.endsWith(StaticString.HUA_XU)) {
			firstKey = "tv:hua_xu";
		} else if (name.endsWith(StaticString.ART)) {
			firstKey = "art";
		} else if (name.endsWith(StaticString.ANIMATION)) {
			firstKey = "animation";
		}
		return firstKey;
	}

	public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
		if (coll == null || coll.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	public static String collectionToDelimitedString(Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	public static String collectionToCammaDelimitedString(Collection<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}


}
