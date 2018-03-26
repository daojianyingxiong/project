package com.enlightent.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.enlightent.been.Paging;
import com.google.gson.Gson;
import com.mongodb.util.JSON;
@SuppressWarnings("all")
public class JsonUtil {
	
	private static Gson gson = new Gson();

	public static Gson getGson() {
		return gson;
	}

	private static Map analyzeAggregationsNoGroup(Map aggs) {
		Map resultMap = new HashMap(aggs.size());
		Set keySet = aggs.keySet();
		for (Object object : keySet) {
			Map object2 = (Map) aggs.get(object);
			Object value = object2.get("value");
			if (value instanceof Double) {
				if (value.toString().endsWith(".0")) {
					value = ((Double) value).longValue();
				}
			}
			resultMap.put(object, value);
		}
		return resultMap;
	}
	
	private static Paging analyzeHits(Map fromJson, boolean showId) {
		try {
			Paging paging = new Paging();
			Object object = fromJson.get("total");
			if (object != null) {
				Integer tInteger = (Integer)object;
				if (tInteger > 9990) {
					tInteger = 9990;
				}
				paging.setTotal(tInteger.longValue());
			}
			List<Map> list = (List<Map>) fromJson.get("hits");
			List<Map> sources = new ArrayList<>(list.size());
			for (Map map : list) {
				Map sourceMap = (Map)map.get("_source");
				if (showId) {
					sourceMap.put("_id", map.get("_id"));
					sourceMap.put("_index", map.get("_index"));
				}
				sources.add(sourceMap);
			}
			paging.setRows(sources);
			return paging;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static List<Map> analyzeAggs(Map aggregations, List<Map> results, Map part) {
		if (results == null) {
			results = new ArrayList<>();
		}
		Set<Entry<String, Map>> entrySet = aggregations.entrySet();
		for (Entry<String, Map> entry : entrySet) {
			
			String key = entry.getKey();
			Map value = entry.getValue();
			List<Map> buckets = (List<Map>) value.get("buckets");
			if (buckets != null) {
				for (Map<String, Object> entry2 : buckets) {
					Map result = new LinkedHashMap<>();
					if (part != null) {
						result.putAll(part);
						results.remove(part);
					}
					results.add(result);
					Set<Entry<String,Object>> entrySet2 = entry2.entrySet();
					for (Entry<String, Object> entry3 : entrySet2) {
						String key2 = entry3.getKey();
						Object value2 = entry3.getValue();
						if ("key".equals(key2)) {
							result.put(key, value2);
						} else if (!"doc_count".equals(key2)) {
							Map map = (Map) value2;
							Object object = map.get("value");
							Object values = map.get("values");
							if (object != null) {
								result.put(key2, object);
							} else if (values != null) {
								Map<String, Object> valuesMap = (Map<String, Object>) values;
								Set<Entry<String, Object>> entrySet3 = valuesMap.entrySet();
								for (Entry<String, Object> entry4 : entrySet3) {
									String key4 = key2 + "." + entry4.getKey();
									result.put(key4, entry4.getValue());
								}
							} else {
								Map<String, Map> aggMap = new HashMap<>(1);
								aggMap.put(key2, map);
								analyzeAggs(aggMap, results, result);
								
							}
						}
					}
				}
			}
		}
		return results;
	}
	
	public static List<Map> analyzeEsResult(String jsonData) {
		Map fromJson = (Map) JSON.parse(jsonData);
		Map aggregations = (Map) fromJson.get("aggregations");
		List<Map> results = null;
		if (aggregations != null) {
			results = analyzeAggs(aggregations);
		} else {
			fromJson = (Map) fromJson.get("hits");
			Paging analyzeHits = JsonUtil.analyzeHits(fromJson, false);
			results = analyzeHits.getRows();
		}
		return results;
	}
	
	private static List<Map> analyzeAggs(Map aggregations) {
		List<Map> results = new ArrayList<>();
		List<Map> analyzeAggs = analyzeAggs(aggregations, results, null);
		if (analyzeAggs.size() == 0) {
			Map analyzeAggregations = analyzeAggregationsNoGroup(aggregations);
			analyzeAggs.add(analyzeAggregations);
		}
		return results;
	}
}
