package com.pmqin.simplequartz;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pmqin.service.IOrderService;

public class extendsQuartzJobBean extends QuartzJobBean {

	public static void main(String[] args) {
		// http://www.xuebuyuan.com/1412159.html
		// http://wenku.baidu.com/link?url=gKMLxyOz57JNh5DcsymUG0nSF4wXshqopdIV8On39yxgKW_X7elJ37hx5PcaFvAGKBUFykjcNnMLPuA2M6eccnQb7DXi-K7dHKXCn3-9rlC
		// 在web.xml中添加：
		//
		// <context-param>
		// <param-name>contextConfigLocation</param-name>
		// <param-value>/WEB-INF/applicationContext.xml,/WEB-INF/quartz.xml</param-value>
		// </context-param>

	}

	private IOrderService orderService;

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println((new Date()) + "****start: 执行拒签已退货超过三天数据转为拒签待外呼");
		orderService.changeOrderStatusWithWaitCall();
		System.out.println((new Date()) + "****end: 执行拒签已退货超过三天数据转为拒签待外呼");
	}

}
