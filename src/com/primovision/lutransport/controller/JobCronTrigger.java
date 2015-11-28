package com.primovision.lutransport.controller;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import com.primovision.lutransport.controller.JobScheduleController;

public class JobCronTrigger {
	
	public void triggerMethod (){
		/*try{
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
	
			JobDetail job = JobBuilder.newJob(JobScheduleController.class)
			.withIdentity("job1", "group1")
			.build();

			CronTrigger trigger = TriggerBuilder.newTrigger()
			.withIdentity("trigger1", "group1")
			.withSchedule(CronScheduleBuilder.cronSchedule("0 26 10 * * ? *"))
			.build();

			sched.scheduleJob(job, trigger);
			sched.start();
			sched.shutdown(true);
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}*/
}
}
