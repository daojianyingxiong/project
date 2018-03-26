package com.enlightent.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

public class ConfUtil {

	public static final String ES_RESTFUL_URL;
	public static final String ES_BASE_URL;
	public static final String ES_SQL_URL;
	public static final String ES_TEST_SQL_URL;
	
	
	public static final String DATA_SUPPORT_ADDRESS;
	public static final String SCHEDULE_VERSION;
	public static final String ANALYTICS_ADDRESS;
	
	public static final String MONGO_ADDRESS;

	public static final String MONGO_TEST_ADDRESS;

	public static final String MONGO_ADDRESS_CRAWLER;
	
	static {
		Properties pps = new Properties();
		try {
			pps.load(ConfUtil.class.getClassLoader().getResourceAsStream("config/application.properties"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		DATA_SUPPORT_ADDRESS = pps.getProperty("data-support-url");
		SCHEDULE_VERSION = pps.getProperty("schedule.version");
		ANALYTICS_ADDRESS = pps.getProperty("analytics-url");
		
		ES_RESTFUL_URL = pps.getProperty("es.restful.url");
		ES_BASE_URL = pps.getProperty("es.base.url");
		ES_SQL_URL = ES_BASE_URL + "_sql";
		ES_TEST_SQL_URL = pps.getProperty("es.test.url") + "_sql";
		MONGO_TEST_ADDRESS = pps.getProperty("mongo.test.address");
		MONGO_ADDRESS_CRAWLER = pps.getProperty("mongo.address.crawler");
		MONGO_ADDRESS = pps.getProperty("mongo.address");
	}

	public static void main(String[] args) {
		System.out.println(DATA_SUPPORT_ADDRESS);
	}
}
