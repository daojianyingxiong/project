package com.enlightent.repository;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enlightent.service.MysqlService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlServiceTest {

	@Resource
    private MysqlService mysqlService;

	@Test
	public void test2() {
		String sql = "select * from management.banner";
		List<Map> restult = mysqlService.getRestult("mysql-test", sql);
		System.out.println(restult);
	}
	
    @Test
    public void test1() {
    	String sql = "select * from banner";
		List<Map> restult = mysqlService.getRestult("mysql", sql);
		System.out.println(restult);
    }

}
