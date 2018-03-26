package com.enlightent.service;
 
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.enlightent.been.PrintResult;
import com.enlightent.been.ding.AtMobile;
import com.enlightent.been.ding.Markdown;
import com.enlightent.entity.ScheduleTask;
import com.enlightent.entity.ScheduleTaskInfo;
import com.enlightent.repository.ScheduleTaskInfoRepository;
import com.enlightent.util.ConfUtil;
import com.enlightent.util.DingdingUtils;
import com.enlightent.util.JsonUtil;
import com.google.gson.Gson;
 
@Service
public class PrintResultService {
	
	private static final String paramTitle = "paramTitle";
	private static final String valueTitle = "valueTitle";
	
	@Resource
    private ScheduleTaskInfoRepository scheduleTaskInfoRepository;

	public void sendMessage(PrintResult printResult, ScheduleTask scheduleTask){
		
		String buildMessageTable = buildMessageTable(printResult);
		
		StringBuilder sBuilder = new StringBuilder(150);
		sBuilder.append("### ").append(scheduleTask.getId()).append(" : ").append(scheduleTask.getTitle()).append(DingdingUtils.NEW_LINE);

		sBuilder.append(buildMessageSummary(printResult, scheduleTask));
		
		if (printResult.isPrintSql()) {
			sBuilder.append("**SQL： **").append(printResult.getSql()).append(DingdingUtils.NEW_LINE);
		}
		sBuilder.append(DingdingUtils.NEW_LINE);
		
		sBuilder.append(buildMessageTable);
		
		sBuilder.append(DingdingUtils.NEW_LINE);
		
		if (printResult.isPrintDetailUrl()) {
			String date = "";
			try {
				date = URLEncoder.encode(scheduleTask.getLastFinishedDate(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String host = "www";
			if ("测试环境".equals(ConfUtil.SCHEDULE_VERSION)) {
				host = "pre";
			}
			sBuilder.append("**详细： **").append("http://").append(host).append(".enlightent.com/management/quartz/esLog?id=")
			.append(scheduleTask.getId()).append("&date=").append(date).append(DingdingUtils.NEW_LINE);
		}
		
		Integer errTotal = scheduleTask.getErrTotal();
		sendMarkdownMsg(scheduleTask, sBuilder.toString(), errTotal > 0);
	}
	
	private String buildMessageSummary(PrintResult printResult, ScheduleTask scheduleTask){
		StringBuilder summary = new StringBuilder(30);
		summary.append("** 执行, 异常, 输出, 执行时间  **").append(DingdingUtils.NEW_LINE);
		int previous = printResult.getPrevious();
		if (previous > 0) {
			Pageable pageable = new PageRequest(0, previous, new Sort(Direction.DESC, "id"));
			List<ScheduleTaskInfo> list = scheduleTaskInfoRepository.findByTaskId(scheduleTask.getId(), pageable);
			for (int i = list.size() - 1; i >= 0; i--) {
				ScheduleTaskInfo scheduleTaskInfo = list.get(i);
				Integer printCount = scheduleTaskInfo.getPrintCount();
				summary.append(scheduleTaskInfo.getFindTotal()).append(", ")
				.append(scheduleTaskInfo.getErrTotal()).append(", ")
				.append(printCount == null ? "-" : printCount).append(", ")
				.append(scheduleTaskInfo.getTaskFinishDate()).append(DingdingUtils.NEW_LINE);
			}
			summary.append("**>**");
		}
		summary.append(scheduleTask.getFindTotal()).append(", ")
		.append(scheduleTask.getErrTotal()).append(", ")
		.append(printResult.getTotal()).append(", ")
		.append(scheduleTask.getLastFinishedDate()).append(DingdingUtils.NEW_LINE);
		return summary.toString();
	}
	
	private String buildMessageTable(PrintResult printResult){
		List<Map> result = printResult.getResult();
		Map<String, Set<String>> titleMap = this.getTitleSet(printResult);
		StringBuilder titleRow = new StringBuilder(30);
		int size = titleMap.size();
		if (size > 0) {
			StringBuilder sBuilder = new StringBuilder(300);
			titleRow.append("**");
			Set<String> paramSet = titleMap.get(paramTitle);
			if (paramSet != null) {
				for (String string : paramSet) {
					titleRow.append(string).append(" | ");
				}
			}
			Set<String> valueSet = titleMap.get(valueTitle);
			if (valueSet != null) {
				for (String string : valueSet) {
					titleRow.append(string).append(" | ");
				}
			}
			titleRow.delete(titleRow.length() - 3, titleRow.length());
			titleRow.append("** ").append(DingdingUtils.NEW_LINE);
			
			
			List<List> valueList = this.getValueList(result, titleMap);
			printResult.setTotal(valueList.size());
			int statementLength = 600;
			if (!printResult.isPrintSql()) {
				statementLength = 900;
			}
			
			for (List list : valueList) {
				for (Object object : list) {
					sBuilder.append(object).append(" | ");
				}
				int length = sBuilder.length();
				if (length > statementLength) {
					sBuilder.delete(statementLength, length);
					sBuilder.append("...").append(DingdingUtils.NEW_LINE);
					break ;
				} else {
					sBuilder.delete(length - 3, length);
					sBuilder.append(DingdingUtils.NEW_LINE);
				}
			}
			titleRow.append(sBuilder);
		}
		
		return titleRow.toString();
	}
	
	public static void main(String[] args) {
		PrintResultService service = new PrintResultService();
		PrintResult printResult = new PrintResult();
		Set<String> excepts = printResult.getExcepts();
		excepts.add("t1");
		printResult.setExcepts(excepts);
		
		List<Map> result = new ArrayList<>();
		Map<Map, List<Map>> map1 = new LinkedHashMap<>();
		Map key1 = new LinkedHashMap<>();
		key1.put("t1", "123");
		key1.put("t2", "456");
		List<Map> value = new ArrayList<>();
		
		Map value1 = new LinkedHashMap<>();
		value1.put("vk1", "123");
		value1.put("vk2", "456");
		value.add(value1);
		
		Map value2 = new LinkedHashMap<>();
		value2.put("vk1", "aa");
		value2.put("vk2", "vv");
		value.add(value2);
		map1.put(key1, value);
		result.add(map1);
		
		Map<Map, List<Map>> map2 = new LinkedHashMap<>();
		Map key2 = new LinkedHashMap<>();
		key2.put("t1", "123e");
		key2.put("t2", "456e");
		List<Map> value4 = new ArrayList<>();
		
		Map value5 = new LinkedHashMap<>();
		value5.put("vk1", "123w");
		value5.put("vk2", "456w");
		value4.add(value5);
		
		Map value6 = new LinkedHashMap<>();
		value6.put("vk1", "aaq");
		value6.put("vk2", "vvq");
		value4.add(value6);
		map2.put(key2, value4);
		
		result.add(map2);
		
		
		printResult.setResult(result);
		
		printResult.setPrintParam(true);
		printResult.setPrintValue(true);
		System.out.println(service.buildMessageTable(printResult));
	}

	private List<List> getValueList(List<Map> result, Map<String, Set<String>> titleMap) {
		int size = titleMap.size();
		if (size > 0) {
			Set<String> paramSet = titleMap.get(paramTitle);
			Set<String> valueSet = titleMap.get(valueTitle);
			List<List> valueList = new ArrayList<>();
			for (Map map : result) {
				Set<Entry<Map, List<Map>>> entrySet = map.entrySet();
				
				for (Entry<Map, List<Map>> entry : entrySet) {
					List paramList = new ArrayList<>();
					if (paramSet != null) {
						Map key = entry.getKey();
						for (String string : paramSet) {
							paramList.add(key.get(string));
						}
					}
					
					if (valueSet != null) {
						List<Map> value = entry.getValue();
						for (Map map2 : value) {
							List row = new ArrayList<>();
							if (paramList.size() > 0) {
								row.addAll(paramList);
							}
							for (String string : valueSet) {
								Object object = map2.get(string);
								row.add(map2.get(string));
							}
							valueList.add(row);
						}
					} else {
						valueList.add(paramList);
					}
				}
			}
			return valueList;
		}
		return Collections.emptyList();
	}
	
	private Map<String, Set<String>> getTitleSet(PrintResult printResult) {
		Map<String, Set<String>> titleMap = new HashMap<>();
		List<Map> result = printResult.getResult();
		if (result.size() > 0) {
			Set<String> excepts = printResult.getExcepts();
			Map firstMap = result.get(0);
			Set<Entry<Map, List<Map>>> entrySet = firstMap.entrySet();
			for (Entry<Map, List<Map>> entry : entrySet) {
				
				if (printResult.isPrintParam()) {
					Map key = entry.getKey();
					Set keySet = key.keySet();
					if (!keySet.isEmpty()) {
						keySet.removeAll(excepts);
						titleMap.put(paramTitle, keySet);
					}
				}
				
				if (printResult.isPrintValue()) {
					List<Map> value = entry.getValue();
					if (value.size() > 0) {
						Map map = value.get(0);
						Set keySet = map.keySet();
						if (!keySet.isEmpty()) {
							titleMap.put(valueTitle, map.keySet());
						}
					}
				}
			}
		}
		return titleMap;
	}
	
	private void sendMarkdownMsg(ScheduleTask scheduleTask, String text, boolean err) {
		Markdown markdown = new Markdown();
		markdown.setTitle(scheduleTask.getTitle());
		markdown.setText(text);
		AtMobile atMobile = null;
		String atMobiles = scheduleTask.getAtMobiles();
		if (StringUtils.isNotBlank(atMobiles)) {
			atMobile = new AtMobile();
			Gson gson = JsonUtil.getGson();
			List<Map<String, String>> fromJson = gson.fromJson(atMobiles, List.class);
			List<String> phones = new ArrayList<>(fromJson.size());
			for (Map<String, String> map : fromJson) {
				phones.add(map.get("phone"));
			}
			atMobile.setAtMobiles(phones);
		}
		
		Integer send = scheduleTask.getSend();
		if (send.equals(3)) {
			if (!err) {
				atMobile = null;
			}
		}
		DingdingUtils.sendMessageToDing(scheduleTask.getRobot(), markdown, atMobile);
	}
}