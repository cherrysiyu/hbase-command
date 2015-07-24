package com.cherry.command.api.threadPool;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 关闭之后，ExecutorService获取被取消的任务
  @description:
  @version:0.1
  @author:Cherry
  @date:Aug 8, 2013
 */
public class TrackingExecutor extends AbstractExecutorService{

	private final ExecutorService	executorService;
	private final Set<Runnable> tasksCancelledAtShutdown = Collections.synchronizedSet(new HashSet<Runnable>());
	
	public TrackingExecutor(ExecutorService executorService) {
		super();
		this.executorService = executorService;
	}
	
	
	public List<Runnable> getCancelledTasks(){
		if(!executorService.isTerminated())
			throw new IllegalSelectorException();
		return new ArrayList<Runnable>(tasksCancelledAtShutdown);
	}

	@Override
	public void shutdown() {
		executorService.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	@Override
	public void execute(final Runnable command) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try{
					command.run();
				}finally{
					if(isShutdown() && Thread.currentThread().isInterrupted())
						tasksCancelledAtShutdown.add(command);	
				}
			}
		});
	}

}

