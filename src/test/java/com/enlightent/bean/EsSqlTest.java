package com.enlightent.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enlightent.util.CalendarUtil;
import com.enlightent.util.HttpUtil;
import com.enlightent.util.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsSqlTest {

    @Test
    public void testSetFields() {
       String title ="yunhe";
       String context = "## 我就是\n我, 是不一样的烟火";
    	String url = "https://oapi.dingtalk.com/robot/send?access_token=6cfb325b8d63a209a41574418a6d25dccaa50ba597eda2b682667cd42f1f322f";
		String content = "{\"msgtype\":\"markdown\",\"markdown\": {\"title\": \"" + title + "\",\"text\": \"" + context + "\"}}";
		System.out.println(content);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HttpUtil.sendPostJson(url, content);
    }
    
    @Autowired
    private EntityManager entityManager;
    @Test
    public void getTvMonitors() {
		StringBuilder sqlString = new StringBuilder(200);
		sqlString.append("select m.videoName,m.channelType,c.channel from monitor_channel c,monitor m,enlightent_daily.video_basic_info v where " + "c.monitor_id = m.id and m.status = 1 and (m.channelType = 0) and v.name=m.videoName and v.channelType = \"tv\" and "
				+ "((v.offLineTime is null) or (v.offLineTime >= :date )) and v.editedFlag != -2 group by m.videoName,c.channel");

		Query query = entityManager.createNativeQuery(sqlString.toString());
		query.setParameter("date", new Date(CalendarUtil.getRangeDate("30")));
		List resultList = query.getResultList();
		Map<String, List<String>> map = new HashMap<>();
		for (Object object : resultList) {
			Object[] objects = (Object[]) object;
			String name = (String) objects[0];
			String channelType = (String) objects[1];
			String channel = (String) objects[2];
			List<String> list = map.get(name + "_" + channelType);
			if (list == null) {
				list = new ArrayList<>();
				list.add(channel);
				map.put(name + "_" + channelType, list);
			} else {
				list.add(channel);
			}
		}
		System.out.println(map.size());
	}
    
    public static void main(String[] args) {
    	String url = "http://es-cluster.enlightent.com:9200/_sql";
//    	String url = "http://es-test.enlightent.com:9200/_sql";
		//String sql = "SELECT count(*) as count FROM redis-realtime where _type = 'names:5m' group by name.raw";
//		String sql = "SELECT * FROM crawler-workrate-* where @timestamp >= 'now-24h'";
//		String sql = "SELECT version.raw,count(*) as c, percentiles(rate) FROM crawler-workrate-* where @timestamp >= 'now-24h' group by version.raw";
//		String sql = "SELECT home_url.raw FROM crawler-adnew-* where channelType = 'tv' and homeurl.raw <> '' and homeurl <> 'title'  and  @timestamp > 'now-24h' group by homeurl.raw";
		String sql = "SELECT count(*) as count FROM redis-realtime where _type = 'names:5m' group by channelType.raw, name.raw";
		String sendPostJson = HttpUtil.sendPostJson(url, sql);
//		List<Map> listMap = JsonUtil.analyzeAggs(sendPostJson);
		List<Map> listMap = JsonUtil.analyzeEsResult(sendPostJson);
		System.out.println(listMap);
	}
}
