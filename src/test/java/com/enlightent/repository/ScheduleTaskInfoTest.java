package com.enlightent.repository;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.enlightent.entity.ScheduleTaskInfo;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTaskInfoTest {

	@Resource
    private ScheduleTaskInfoRepository scheduleTaskInfoRepository;
    
	@Test
	public void test2() {
		Pageable pageable = new PageRequest(0, 5, new Sort(Direction.DESC, "id"));
		List<ScheduleTaskInfo> list = scheduleTaskInfoRepository.findByTaskId(71L, pageable);
		for (int i = list.size() - 1; i >= 0; i--) {
			System.out.println(list.get(i).getId());
		}
		
	}
    
}
