package com.enlightent.configuration;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;  
  
@Configuration  
public class QuartzConfigration {
	
	private static final String QZ_URL = "org.quartz.dataSource.qzDS.URL";
	
	@Value("${spring.datasource.address}")
	private String datasourceAddress;
	
    @Bean(name = "schedulerFactoryBean")  
    public SchedulerFactoryBean schedulerFactory() throws IOException {  
    	SchedulerFactoryBean bean = new SchedulerFactoryBean();  
    	bean.setQuartzProperties(quartzProperties());
    	return bean;  
    }
    
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz_store.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        Properties object = propertiesFactoryBean.getObject();
        String property = object.getProperty(QZ_URL);
        property = property.replace("${spring.datasource.address}", datasourceAddress);
        object.put(QZ_URL, property);
        return object;
    }

    /*
     * quartz初始化监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
       return new QuartzInitializerListener();
    }

    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name="scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactory().getScheduler();
    }
}  
