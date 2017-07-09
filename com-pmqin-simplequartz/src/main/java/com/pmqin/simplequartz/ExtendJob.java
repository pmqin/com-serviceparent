package com.pmqin.simplequartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class ExtendJob implements Job  {

	
	public ExtendJob () {
		System.out.println("Quartzjob的任务调度是不是每次运行都实例化？看到这句就是");
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String nowdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		JobDataMap  map=context.getJobDetail().getJobDataMap();
		
		System.out.println("Quartzjob"+map.get("pmqin")+"的任务调度！！！"+nowdate);
	}

	public static void main(String[] args) throws InterruptedException {
		 //通过schedulerFactory获取一个调度器    
	       SchedulerFactory schedulerfactory = new StdSchedulerFactory();    
	       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS"); 
	       Scheduler scheduler=null;    
	       try{    
	           // 通过schedulerFactory获取一个调度器    
	           scheduler = schedulerfactory.getScheduler();    
	               
	            // 创建jobDetail实例，绑定Job实现类    
	            // 指明job的名称，所在组的名称，以及绑定job类    
	           JobDetail job = JobBuilder.newJob(ExtendJob.class).withIdentity("JobName", "JobGroupName").build();    
	           job.getJobDataMap().put("pmqin","getJobDataMap success");   
	               
	            // 定义调度触发规则    
	                           
	                   // SimpleTrigger   
//	                    Trigger trigger=TriggerBuilder.newTrigger().withIdentity("SimpleTrigger", "SimpleTriggerGroup")
	          // simpleTrigger.setRepeatInterval(5000);   
//	                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(3).withRepeatCount(6))    
//	                    .startNow().build();    
	             
	             //  job1 corn表达式  每五秒执行一次  
	              Trigger trigger=TriggerBuilder.newTrigger().withIdentity("CronTrigger1", "CronTriggerGroup")    
	              .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))    
	              .startNow().build();   
	              // 把作业和触发器注册到任务调度中    
	              Date ft = scheduler.scheduleJob(job, trigger);    
	           
	           
	           // job 2  corn表达式  每2秒执行一次  
	           job = JobBuilder.newJob(SimpleimplJob.class).withIdentity("job2", "group1").build(); 
	           trigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")).build(); 
	          // 把作业和触发器注册到任务调度中    
	           ft= scheduler.scheduleJob(job, trigger); 
	          System.out.println(job.getKey() + " 已被安排执行于: " + sdf.format(ft) + "，并且以规则重复执行: ");//trigger.getCronExpression()); 
	               
	           // 启动调度    
	           scheduler.start();    
	             
	           Thread.sleep(30000);  
	             
	           // 停止调度   定时器不再工作
	           scheduler.shutdown();  
	               
	       }catch(SchedulerException e){    
	           e.printStackTrace();    
	       }    

	}

}
