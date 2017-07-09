package com.pmqin.simplequartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Quartzjob {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("quartz-config.xml");
		//如果配置文件中将startQuertz bean的lazy-init设置为false 则不用实例化
		//context.getBean("startQuertz");
		System.out.print("Test end..");
		
	}
    
	public Quartzjob()
	{
		System.out.println("Quartzjob的任务调度是不是每次运行都实例化？看到这句就是");
	}
	public void work() {
		String nowdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("Quartzjob的任务调度！！！"+nowdate);
	}

}
