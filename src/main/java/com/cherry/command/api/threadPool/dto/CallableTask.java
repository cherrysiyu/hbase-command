package com.cherry.command.api.threadPool.dto;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class CallableTask<T> {

	/***
	 * Callable任务
	 */
	private Callable<T> commond;
	
	
	/**
	 * 过期的时间
	 */
	private long timeOut;
	/**
	 * timeOut 参数的时间单位，默认是秒
	 */
	private TimeUnit unit = TimeUnit.SECONDS;
	
	/**
	 * 不需要过期时间的
	 * @param commond
	 */
	public CallableTask(Callable<T> commond) {
		super();
		this.commond = commond;
	}

	/**
	 * 需要过期时间
	 * @param commond
	 * @param timeOut
	 * @param unit
	 */
	public CallableTask(Callable<T> commond,long timeOut, TimeUnit unit) {
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




	public Callable<T> getCommond() {
		return commond;
	}



	public void setCommond(Callable<T> commond) {
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
