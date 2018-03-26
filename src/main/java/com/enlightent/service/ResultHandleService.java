package com.enlightent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.enlightent.been.PrintResult;
import com.enlightent.entity.ScheduleTask;
import com.enlightent.util.CalendarUtil;
import com.enlightent.util.ConfUtil;
import com.enlightent.util.DateUtil;
import com.enlightent.util.HttpUtil;
import com.enlightent.util.JsonUtil;
import com.mongodb.util.JSON;

@Service
public class ResultHandleService {

	@Autowired
	private HiveService hiveService;

	@Autowired
	private MongoService mongoService;

	@Autowired
	private MysqlService mysqlService;

	@Autowired
	private PrintResultService printResultService;

	public List<Map> executeSql(String sql, String source, StringBuilder exLog) {
		sql = DateUtil.replaceDateReg(sql);
		if (source.equals("mysqlService") || source.equals("mysql") || source.equals("mysql-test")) {
			return mysqlService.getRestult(source, sql);
		} else if (source.equals("esService") || source.equals("es") || source.equals("es-test")) {
			String esSql = ConfUtil.ES_SQL_URL;
			if (source.equals("es-test")) {
				esSql = ConfUtil.ES_TEST_SQL_URL;
			}
			String sendPostJson = HttpUtil.sendPostJson(esSql, sql);
			List list = JsonUtil.analyzeEsResult(sendPostJson);
			return list;
		} else if (source.equals("mongoService") || source.equals("mongo") || source.equals("mongo-test")) {
			return mongoService.parseMongo(sql, source);
		} else if (source.equals("url")) {
			return parseUrl(sql);
		} else if (source.equals("hive")) {
			return hiveService.executeQuery(sql);
		} else {
			return null;
		}
	}

	private static List<Map> parseUrl(String url) {
		if (!url.startsWith("http")) {
			url = ConfUtil.DATA_SUPPORT_ADDRESS + url;
		}

		List<NameValuePair> nvps = null;
		int indexOf = url.indexOf("?");
		if (indexOf > -1) {
			String paramStr = url.substring(indexOf + 1, url.length());
			url = url.substring(0, indexOf);
			String[] split = paramStr.split("&");
			nvps = new ArrayList<>(split.length);
			for (String string : split) {
				String[] split2 = string.split("=");
				NameValuePair e = new BasicNameValuePair(split2[0], split2[1]);
				nvps.add(e);
			}
		}
		String sendPost = HttpUtil.sendPost(url, nvps);
		if (sendPost != null && !sendPost.startsWith("[")) {
			if (!sendPost.startsWith("{")) {
				Map<String, String> map = new HashMap<>(1);
				map.put("value", sendPost.trim());
				sendPost = JsonUtil.getGson().toJson(map);
			}
			sendPost = "[" + sendPost + "]";
		}
		List fromJson = (List) JSON.parse(sendPost);
		return fromJson;
	}

	public void saveEsLog(Object param, ScheduleTask scheduleTask, List list, String dataSource, String sqLanguage, String message) {
		String url = ConfUtil.ES_BASE_URL + "schedule-log-" + CalendarUtil.getYear() + "." + CalendarUtil.getMonth() + "/";
		if (ConfUtil.SCHEDULE_VERSION.equals("测试环境")) {
			url = url + "schedule_pre";
		} else {
			url = url + "schedule_prod";
		}

		// mysql查询会出现list中数object[]情况
		List<String> objectList = null;
		if (list != null && list.size() > 0) {
			Object obj = list.get(0);
			if (obj instanceof Object[]) {
				objectList = new ArrayList<String>();
				for (Object object : list) {
					String string = Arrays.asList((Object[]) object).toString();
					objectList.add(string);
				}
				list = objectList;
			}
		}

		// param可能为object[]情况
		if (param != null) {
			if (param instanceof Object[]) {
				param = Arrays.toString((Object[]) param);
			} else if (param instanceof Map) {
				param = Arrays.toString(((Map) param).values().toArray());
			}
			param = param.toString();
		} else {
			param = "";
		}

		String title = scheduleTask.getTitle();
		int length = message.length();

		Map<String, Object> params = new HashMap<>();
		params.put("@timestamp", new Date().getTime());
		params.put("id", scheduleTask.getId());
		params.put("title", title);
		params.put("type", length > 1 ? "1" : "0");
		params.put("message", message);
		params.put("dataSource", dataSource);
		params.put("sqLanguage", sqLanguage);
		params.put("cronExpression", scheduleTask.getCronExpression());
		params.put("lastFinishedDate", scheduleTask.getLastFinishedDate());
		params.put("note", scheduleTask.getNote());
		params.put("result", list == null ? "" : list.toString());
		params.put("param", param);
		String jsonString = JSONObject.toJSONString(params);
		HttpUtil.sendPostJson(url, jsonString);
	}

	public String sendMessage(ScheduleTask scheduleTask, PrintResult printResult) {
		Integer send = scheduleTask.getSend();
		boolean isSend = true;
		if (send.equals(0)) {// 任何情况不发送消息
			isSend = false;
		} else if (send.equals(2)) {// errTotal > 0 发送消息
			if (scheduleTask.getErrTotal() == 0) {
				isSend = false;
			}
		}

		if (isSend) {
			printResultService.sendMessage(printResult, scheduleTask);
		}
		return null;
	}

}
