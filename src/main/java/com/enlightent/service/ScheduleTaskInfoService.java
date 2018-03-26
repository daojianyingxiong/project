package com.enlightent.service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enlightent.entity.ScheduleTask;
import com.enlightent.entity.ScheduleTaskInfo;
import com.enlightent.repository.ScheduleTaskInfoRepository;
 
@Service
public class ScheduleTaskInfoService {
	
	@Autowired
	private ScheduleTaskInfoRepository scheduleTaskInfoRepository;
	
	public void save(ScheduleTask scheduleTask, StringBuilder exLog, Integer printCount) {
		Integer errTotal = scheduleTask.getErrTotal();
		Integer findTotal = scheduleTask.getFindTotal();
		ScheduleTaskInfo info = new ScheduleTaskInfo();
		
		int percent = 0;
		if (scheduleTask.isFailed()) {
			percent = 101;
			info.setStatus(5);
		} else {
			if (errTotal > 0) {
				info.setStatus(1);
				percent = (int) (errTotal.doubleValue() / findTotal * 100);
			} else {
				info.setStatus(2);
			}
		}
		
		info.setAtMobiles(scheduleTask.getAtMobiles());
		info.setTaskFinishDate(scheduleTask.getLastFinishedDate());
		
		info.setFindTotal(findTotal);
		info.setErrTotal(errTotal);
		info.setTitle(scheduleTask.getTitle());
		info.setTaskId(scheduleTask.getId());
		info.setPercent(percent);
		info.setMessage(scheduleTask.getMessage());
		
		info.setPrintCount(printCount);
		
		if (exLog != null) {
			info.setDebugInfo(exLog.toString());
		}
		
		try {
			scheduleTaskInfoRepository.save(info);
		} catch (Exception e) {
			e.printStackTrace();
			info.setDebugInfo(e.getMessage());
			scheduleTaskInfoRepository.save(info);
		}
	}
}