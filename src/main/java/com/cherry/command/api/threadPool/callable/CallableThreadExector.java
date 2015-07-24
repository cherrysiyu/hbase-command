package com.cherry.command.api.threadPool.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.cherry.command.api.threadPool.ThreadExector;
import com.cherry.command.api.utils.CommonMethod;



public class CallableThreadExector extends ThreadExector{
	
	/**
	 * 创建的线程池的大小是CPU的核数+1
	 */
	public CallableThreadExector(){
		super();
	}
	/**
	 * 创建指定大小的线程池，注意：当传入的poolSize > CPU核数 * DEFAULT_INITIAL_CAPACITY(默认写的是8，通过配置文件读取)时，会把poolSize 设置成CPU核数 * DEFAULT_INITIAL_CAPACITY
	 * @param poolSize
	 */
	public CallableThreadExector(int poolSize){
		super(poolSize);
	}
	/**
	 * 指定线程池执行器
	 * @param executorService
	 */
	public CallableThreadExector(ExecutorService executorService){
		super(executorService);
	}
	
	@Override
	public <T> void excuteCallableTasks(List<Callable<T>> taskThreads) {
		if(CommonMethod.isCollectionNotEmpty(taskThreads)){
			CompletionService<T> completionService = new ExecutorCompletionService<T>(executorService);
			logger.info("本次共"+taskThreads.size()+"个任务，开始执行时间:"+System.currentTimeMillis());
			try{
				for (Callable<T> task : taskThreads) {
					completionService.submit(task);
				}
			}finally{
				colseThreadPool();
			}
		}
	}

	@Override
	public void excuteRunnableTasks(List<? extends Runnable> taskThreads) {
		
		if(CommonMethod.isCollectionNotEmpty(taskThreads)){
			logger.info("本次共"+taskThreads.size()+"个任务，开始执行时间:"+System.currentTimeMillis());
			try{
				for (Runnable command : taskThreads) {
					executorService.execute(command);
				}
			}finally{
				colseThreadPool();
			}
		}
	}

	@Override
	public <T> List<T> excuteTasksAndWaitForResult(List<Callable<T>> taskThreads) {
		return excuteTasksAndWaitForResult(taskThreads,true,0,null);
	}

	@Override
	public <T> boolean excuteAndWaitForAllTasksFinish(List<Callable<T>> taskThreads) {
		if(!CommonMethod.isCollectionNotEmpty(taskThreads))
			 throw new IllegalArgumentException("Illegal taskThreads: "+ taskThreads +",任务集合不能为空");
			
		excuteTasksAndWaitForResult(taskThreads);
		return true;
	}
	@Override
	public <T> List<T> excuteTasksAndWaitForResult(List<Callable<T>> taskThreads, long timeOut, TimeUnit timeUnit) {
		return excuteTasksAndWaitForResult(taskThreads,false,timeOut,timeUnit);
	}
	
	private <T> List<T> excuteTasksAndWaitForResult(List<Callable<T>> taskThreads,boolean isWaitForTaskFinish, long timeOut, TimeUnit timeUnit) {
		List<T> list  = null;
		if(CommonMethod.isCollectionNotEmpty(taskThreads)){
			
			CompletionService<T> completionService = new ExecutorCompletionService<T>(executorService);
			
			int taskSize = taskThreads.size();
			list = new ArrayList<T>(taskSize);
			List<Future<T>> futures = new ArrayList<Future<T>>(taskSize);
			long beginTime = System.currentTimeMillis();
			logger.info("本次共"+taskSize+"个任务，开始执行时间:"+beginTime);
			
			//提交任务
			for (Callable<T> callable : taskThreads) {
				futures.add(completionService.submit(callable));
			}
			try{
				//取得结果
				for (int i = 0; i < taskSize; i++) {
		        	 try {
		        		 Future<T> future = null;
		        		 future = completionService.take();
		        		 if(future != null){
		        			 T result = null;
		        			 if(isWaitForTaskFinish)
		        				 result =  future.get();
							else{
								try {
									result =  future.get(timeOut,timeUnit);
								} catch (TimeoutException e) {
									logger.error("e",e);
								}
							}
		        			if(result != null)
		        				list.add(result);
		        		 }
		     		} catch (Exception e) {
		     			//如果出错，不要影响其他的任务，取消出错的这个任务
		     			futures.get(i).cancel(true);
		     			logger.error("e",e);
		     		}
				}
			}finally{
				colseThreadPool();
			}
			logger.info("完成所有任务耗时:"+(System.currentTimeMillis() - beginTime)+"ms");
		}
		return list == null?new ArrayList<T>():list;
	}
	
}
