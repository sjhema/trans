package com.primovision.lutransport.controller;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

public class JobScheduleController implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    JobKey jobKey = context.getJobDetail().getKey();
	    System.out.println("This is just a test ");
	}
}
