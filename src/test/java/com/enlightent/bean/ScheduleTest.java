package com.enlightent.bean;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enlightent.entity.ScheduleTask;
import com.enlightent.repository.ScheduleTaskRepository;
import com.enlightent.service.ResultHandleService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTest {

	@Resource
	private ScheduleTaskRepository scheduleTaskRepository;
	
	@Resource
	private ResultHandleService resultHandleService;
	
    @Test
    public void test1() {
    	ScheduleTask findOne = scheduleTaskRepository.findOne(68L);
    	findOne.setAtMobiles("aaaa");
    	findOne.setErrTotal(1234);
    	scheduleTaskRepository.save(findOne);
    }
    
   
}
