package com.enlightent.repository;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.derby.tools.sysinfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enlightent.service.MongoService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoServiceTest {

	@Resource
	private MongoService mongoService;

	@Test
	public void test1() {
		String sql = "{\"database\":\"portrait\",\"table\":\"weiboArtFans\",\"sql\":\"{'data_type':'taskComplete','200':{'$exists':true}}\"}";
		String source = "mongo-test";
		List<Map> restult = mongoService.parseMongo(sql, source);
		System.out.println(restult);
	}

	@Test
	public void test2() {
		String sql = "{\"database\":\"portrait\",\"table\":\"weiboArtFans\",\"sql\":\"{'data_type':'taskComplete','200':{'$exists':true}}\"}";
		String source = "mongo";
		List<Map> restult = mongoService.parseMongo(sql, source);
		System.out.println(restult);
	}

	@Test
	public void test3() {
		String sql = "{\"database\":\"crawler\",\"table\":\"weiboArtFans\",\"sql\":\"{'data_type':'taskComplete','200':{'$exists':true}}\"}";
		String source = "";
		List<Map> restult = mongoService.parseMongo(sql, source);
		System.out.println(restult);
	}

	@Test
	public void test4() {
		
		for (int i = 0; i < 30; i++) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					String sql = "{\"database\":\"portrait\",\"table\":\"weiboArtFans\",\"sql\":\"{'data_type':'taskComplete','200':{'$exists':true}}\"}";
					String source = "mongo";
					List<Map> restult = mongoService.parseMongo(sql, source);
					System.out.println(restult);
				}
				
			});
			System.out.println(t);

			t.start();
		}
		
		try {
			Thread.currentThread().sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
