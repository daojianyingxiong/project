package com.enlightent.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.enlightent.been.Paging;
import com.enlightent.entity.JobAndTrigger;
import com.enlightent.entity.ScheduleTask;
import com.enlightent.quartz.ScheduleTaskJobService;
import com.enlightent.repository.ScheduleTaskRepository;
import com.enlightent.util.BeanUtils;

@RestController
@RequestMapping("/job/")
public class JobController {
	@Autowired
	private ScheduleTaskJobService scheduleTaskJobService;
	
	@Autowired
	private ScheduleTaskRepository scheduleTaskRepository;
	
	@RequestMapping("runJobNow")
	public void runAJobNow(Long id) throws SchedulerException{
		ScheduleTask task = scheduleTaskRepository.findOne(id);
		scheduleTaskJobService.runAJobNow(task);
	}
	
	
	@RequestMapping("addJob")
	public void addJob(Long id) throws SchedulerException{
		ScheduleTask task = scheduleTaskRepository.findOne(id);
		task.setStatus(1);
		scheduleTaskJobService.addJob(task);
		scheduleTaskRepository.save(task);
	}


	@RequestMapping("pauseJob")
	public void pauseJob(Long id) throws SchedulerException{
		ScheduleTask task = scheduleTaskRepository.findOne(id);
		scheduleTaskJobService.deleteJob(task);
		task.setStatus(0);
		scheduleTaskRepository.save(task);
	}


    /**
     * 关闭一个job
     */
	@RequestMapping("resumeJob")
	public void resumeJob(Long id) throws SchedulerException{
		ScheduleTask task = scheduleTaskRepository.findOne(id);
		scheduleTaskJobService.resumeJob(task);
		task.setStatus(1);
		scheduleTaskRepository.save(task);
	}

    /**
     * 删除一个job, 从数据库里删除
     */
	@RequestMapping("deleteJob")
	public void deleteJob(Long id) throws SchedulerException{
		ScheduleTask task = scheduleTaskRepository.findOne(id);
		scheduleTaskJobService.deleteJob(task);
		scheduleTaskRepository.delete(task);
	}
	
	@RequestMapping("updateJobCron")
	public void updateJobCron(Long id) throws SchedulerException{
		ScheduleTask task = scheduleTaskRepository.findOne(id);
		scheduleTaskJobService.updateJobCron(task);
	}

    /**
     * 在数据库中添加或更新job，如果job的status为1则调度该job
     */
	@RequestMapping("saveJob")
	public Long saveJob(String fromJson) throws SchedulerException {
		ScheduleTask parse = JSONObject.parseObject(fromJson, new TypeReference<ScheduleTask>(){});
        if (parse.getId() != null) {
            ScheduleTask findOne = scheduleTaskRepository.findOne(parse.getId());
            if (findOne != null) {
                BeanUtils.copyProperties(parse, findOne, null , null);
                parse = findOne;
            }
        } else {
            Date now = new Date();
            parse.setCreateDate(now);
            parse.setStatus(1);
        }
        
        ScheduleTask save = scheduleTaskRepository.save(parse);
        
        if (Objects.equals(1, save.getStatus())) {
			scheduleTaskJobService.deleteJob(save);
			scheduleTaskJobService.addJob(save);
        }

		return save.getId();
	}


	@RequestMapping("get")
    public ScheduleTask get(Long id) {
        return scheduleTaskRepository.findOne(id);
    }

    @RequestMapping("changeStatus")
    public Boolean changeStatus(Long id, Integer status) throws SchedulerException {
        ScheduleTask one = scheduleTaskRepository.findOne(id);
        if (one != null) {
            one.setStatus(status);
            one = scheduleTaskRepository.save(one);
            if (Objects.equals(1, status)) {
                scheduleTaskJobService.addJob(one);
            } else if (Objects.equals(0, status)) {
                scheduleTaskJobService.deleteJob(one);
            }
            return true;
        }

        return false;
    }
    
    
    @RequestMapping("queryjob")
    public Paging<JobAndTrigger> getJobAndTriggerDetails(String jobName, int pageNum, int pageSize, String sort, String order) throws Exception { 
    	return scheduleTaskJobService.getJobAndTriggerDetails(jobName, pageNum, pageSize, sort, order);
    }
    
}
