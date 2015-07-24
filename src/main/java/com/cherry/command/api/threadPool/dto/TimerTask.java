package com.cherry.command.api.threadPool.dto;


import java.util.concurrent.TimeUnit;

/**
 * 定期任务bean
 * @description:
 * @author:Cherry
 * @version:1.0
 * @date:Jan 24, 2013
 */
public class TimerTask {
	/**
	 * 执行的任务
	 */
	private Runnable command;
	/**
	 * 首次执行的延迟时间（多长时间后开始执行任务）
	 */
	private long initialDelay;
	/**
	 * 连续执行之间的周期（每多长时间执行一次）
	 */
	private long period;
	/**
	 * initialDelay 和 period 参数的时间单位，默认是秒
	 */
	private TimeUnit unit = TimeUnit.SECONDS;

	
	public TimerTask() {
		super();
	}
	
	/**
	 * 创建一个TimeUnit.SECONDS的没有延时执行的定时任务
	 * @param command
	 * @param period
	 */
	public TimerTask(Runnable command,long period) {
		this(command,0,period,TimeUnit.SECONDS);
	}
	
	public TimerTask(Runnable command,long period,TimeUnit unit) {
		this(command,0,period,unit);
	}
	
	public TimerTask(Runnable command,long initialDelay,long period) {
		this(command,initialDelay,period,TimeUnit.SECONDS);
	}

	public TimerTask(Runnable command, long initialDelay, long period,
			TimeUnit unit) {
		super();
		if(command == null){
			 throw new NullPointerException("定期任务不能为null");
		}
		if(period <= 0){
			 throw new IllegalArgumentException("执行的周期不能 <= 0");
		}
		this.command = command;
		this.initialDelay = initialDelay;
		this.period = period;
		setUnit(unit);
	}

	public Runnable getCommand() {
		return command;
	}

	public void setCommand(Runnable command) {
		if(command == null){
			 throw new NullPointerException("定期任务不能为null");
		}
		this.command = command;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		if(period <= 0){
			 throw new IllegalArgumentException("执行的周期不能 <= 0");
		}
		this.period = period;
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
