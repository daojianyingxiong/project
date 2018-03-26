package com.enlightent.repository;

import com.enlightent.entity.ScheduleTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jianglei
 * @since 2018/1/11
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTaskTest {

    private ScheduleTaskRepository scheduleTaskRepository;

    @Test
    public void testSave() {
        ScheduleTask task = new ScheduleTask();
        task.setTitle("jianglei");
        scheduleTaskRepository.save(task);

    }


    @Autowired
    public void setScheduleTaskRepository(ScheduleTaskRepository scheduleTaskRepository) {
        this.scheduleTaskRepository = scheduleTaskRepository;
    }
}
