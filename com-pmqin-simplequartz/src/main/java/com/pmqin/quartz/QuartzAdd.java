package com.pmqin.quartz;

import java.util.Date;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.pmqin.simplequartz.ExtendJob;

public class QuartzAdd {

	public static void main(String[] args) throws Exception {
		System.out.println(quartzAdd("group"," name"));
	}

	public static SchedulerFactory schedFact = new StdSchedulerFactory();

	public static Scheduler sched;

	public static void startSched() throws SchedulerException {
		try {
			QuartzAdd.sched = QuartzAdd.schedFact.getScheduler();
			QuartzAdd.sched.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean quartzAdd(String group, String name) throws Exception {
		try {
			// 若sched未赋值或者未启动，则先在全局中启动它
			if (QuartzAdd.sched == null || !QuartzAdd.sched.isStarted()) {
				QuartzAdd.startSched();
			}
			// 定时规则,跟普通crontable的差不多
			String rule = "0/5  * * * * ?";
			// 设置组名，和任务名
			String quartz_name = name;
			String quartz_group = group;
			// 创建jobDetail实例，指定job名以及所属组
			JobDetail jobDetail = JobBuilder.newJob(ExtendJob.class).withIdentity(quartz_name, quartz_group).build();
			jobDetail.getJobDataMap().put("taskId", name);
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(quartz_name, quartz_group)
			.withSchedule(CronScheduleBuilder.cronSchedule(rule)).startNow().build();

			QuartzAdd.sched.scheduleJob(jobDetail, trigger);
			// logger.info("[已添加定时获取进度任务, taskID:" + taskId + ", type:" + type +
			// "]");
			return true;
		} catch (Exception e) {
			// logger.error("[添加定时任务出错,任务号:" + taskId + "]");
			// logger.error(e.toString());
			return false;
		}
	}

}
