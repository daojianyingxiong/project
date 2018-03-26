package com.enlightent.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.enlightent.been.ding.AtMobile;
import com.enlightent.been.ding.Link;
import com.enlightent.been.ding.Markdown;
import com.enlightent.been.ding.MsgType;
import com.mongodb.util.JSON;

public class DingdingUtils {
	
	public static final String NEW_LINE = "  \r\n";
	
	private static String dingUrl = null;
	
	static {
		if (ConfUtil.SCHEDULE_VERSION.equals("测试环境")) {
			dingUrl = "https://oapi.dingtalk.com/robot/send?access_token=adab6ad1ccb2118b0800e4e8fd248a55bf410e20faafc789202a93e50672348d";
		} else {
			dingUrl = "https://oapi.dingtalk.com/robot/send?access_token=77496e2dbee13037b8c7c514564325b8e6c04f248a5e22702de27fc6ec3b6e30";
		}
	}
	
	
	public static void sendToDing(String title, String context) {
		
		String content = "{\"msgtype\":\"markdown\",\"markdown\": {\"title\": \"" + title + "\",\"text\": \"" + context + "\"}}";
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String sendPostJson = HttpUtil.sendPostJson(dingUrl, content);
		Map fromJson = (Map) JSON.parse(sendPostJson);
		sendErrMsg(title, sendPostJson, fromJson);
	}
	
	public static void sendMessageToDing(String robot, MsgType msgType, AtMobile atMobile) {
		Map<String, Object> map = new HashMap<>();
		String type = msgType.getType();
		map.put("msgtype", type);
		map.put(type, msgType);
		if (atMobile != null && !"link".equals(type)) {
			map.put("at", atMobile);
			List<String> atMobiles = atMobile.getAtMobiles();
			String text = msgType.getText();
			text += NEW_LINE;
			StringBuilder sBuilder = new StringBuilder(text);
			for (String phone : atMobiles) {
				sBuilder.append("@").append(phone).append(" ");
			}
			msgType.setText(sBuilder.toString());
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String json = JsonUtil.getGson().toJson(map);
		if (StringUtils.isBlank(robot)) {
			robot = dingUrl;
		}
		String sendPostJson = HttpUtil.sendPostJson(robot, json);
		Map fromJson = (Map) JSON.parse(sendPostJson);
		sendErrMsg(msgType.getTitle(), sendPostJson, fromJson);
	}

	public static void main(String[] args) {
		Link link = new Link();
		link.setMessageUrl("http://www.baidu.com");
		String text = "test link" + NEW_LINE + "2" + NEW_LINE + "3";
		link.setText(text);
		link.setTitle("aaaaaaaaaaa");
		
		Markdown markdown = new Markdown();
		
		String sqlOne = " sql "; 
		String content = "标题:  aaaa " + "异常  \r\n" + "SQL语句: " + sqlOne + "	无数据无法执行SqlTwo  \r\n";
		markdown.setText(content);
		markdown.setTitle("bbbbbbbbbbb");
		
		AtMobile atMobile = new AtMobile();
		
		List<String> atMobiles = new ArrayList<>();
		atMobiles.add("13051665356");
		atMobiles.add("15731333689");
		atMobile.setAtMobiles(atMobiles);
		sendMessageToDing(null, markdown, null);
	}

	private static void sendErrMsg(String title, String sendPostJson, Map fromJson) {
		String content;
		if(!((String)fromJson.get("errmsg")).equals("ok")){
			content = "{\"msgtype\":\"markdown\",\"markdown\": {\"title\": \"" + title + "\",\"text\": \"消息发送失败!!!" + NEW_LINE + sendPostJson + "\"}}";
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			HttpUtil.sendPostJson(dingUrl, content);
		}
	}
	
}
