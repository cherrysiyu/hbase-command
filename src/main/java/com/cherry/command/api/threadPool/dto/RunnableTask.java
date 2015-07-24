package com.cherry.command.api.threadPool.dto;

import java.util.concurrent.TimeUnit;

/**
 * 线程任务Bean
 * @description:
 * @author:Cherry
 * @version:1.0
 * @date:Jan 28, 2013
 */
public  class RunnableTask {
	
	/**
	 * Runnable任务
	 */
	private Runnable commond;
	
	/**
	 * 过期的时间
	 */
	private long timeOut;
	/**
	 * timeOut 参数的时间单位，默认是秒
	 */
	private TimeUnit unit = TimeUnit.SECONDS;
	
	/**
	 * 没有过期时间的任务
	 * @param commond
	 */
	public RunnableTask(Runnable commond) {
		super();
		this.commond = commond;
	}

	/**
	 * 带有过期时间的任务
	 * @param commond
	 * @param timeOut
	 * @param unit
	 */
	public RunnableTask(Runnable commond,long timeOut,TimeUnit unit) {
		if(timeOut <= 0){
			 throw new IllegalArgumentException("执行的过期时间不能 <= 0");
		}
		if(unit == null){
			throw new IllegalArgumentException("时间单位不能为null");
		}
		this.timeOut = timeOut;
		this.unit = unit;
		setCommond(commond);
	}
	
	


	public Runnable getCommond() {
		return commond;
	}



	public void setCommond(Runnable commond) {
		if(commond == null){
			 throw new NullPointerException("执行的任务不能为null");
		}
		this.commond = commond;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		if(timeOut <= 0){
			 throw new IllegalArgumentException("执行的过期时间不能 <= 0");
		}
		this.timeOut = timeOut;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		if(unit == null){
			 throw new IllegalArgumentException("时间单元不能为null");
		}
		this.unit = unit;
	}
	
}
