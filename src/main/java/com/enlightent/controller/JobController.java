package com.enlightent.controller;

import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job/")
public class JobController {
	
	@RequestMapping("runJobNow")
	public void runAJobNow(Long id) throws SchedulerException{
	}
	
}
