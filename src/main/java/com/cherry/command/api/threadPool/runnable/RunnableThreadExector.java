package com.cherry.command.api.threadPool.runnable;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.cherry.command.api.threadPool.ThreadExector;
import com.cherry.command.api.utils.CommonMethod;


public class RunnableThreadExector extends ThreadExector{
	
	/**
	 * 创建的线程池的大小是CPU的核数+1
	 */
	public RunnableThreadExector(){
		super();
	}
	/**
	 * 创建指定大小的线程池，注意：当传入的poolSize > CPU核数 * DEFAULT_INITIAL_CAPACITY(默认写的是8，通过配置文件读取)时，会把poolSize 设置成CPU核数 * DEFAULT_INITIAL_CAPACITY
	 * @param poolSize
	 */
	public RunnableThreadExector(int poolSize){
		super(poolSize);
	}
	/**
	 * 指定线程池执行器
	 * @param executorService
	 */
	public RunnableThreadExector(ExecutorService executorService){
		super(executorService);
	}
	
	@Override
	public void excuteRunnableTasks(List<? extends Runnable> taskThreads) {
		if(CommonMethod.isCollectionNotEmpty(taskThreads)){
			long beginTime = System.currentTimeMillis();
			logger.info("本次共"+taskThreads.size()+"个任务，开始执行时间:"+beginTime);
			try{
				for (Runnable command : taskThreads) {
					executorService.execute(command);
				}
			}finally{
				colseThreadPool();
			}
		}
	}
	
	
	/**
	 * 等待结果完成最后返回结果
	 * @param <T>
	 * @param taskThreads
	 * @return
	 */
	public  <T> List<T> excuteTasksAndWaitForResult(List<Callable<T>> taskThreads){
		return excuteTasksAndWaitForResult(taskThreads,true,0,null);
	}
	
	@Override
	public <T> void excuteCallableTasks(List<Callable<T>> taskThreads) {
		if(CommonMethod.isCollectionNotEmpty(taskThreads)){
			long beginTime = System.currentTimeMillis();
			int size = taskThreads.size();
			logger.info("本次共"+size+"个任务，开始执行时间:"+beginTime);
				try {
					 for (Callable<T> callable : taskThreads) {
						 executorService.submit(callable);
					}
				}finally{
					colseThreadPool();
				}
		}
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
		return excuteTasksAndWaitForResult(taskThreads,false, timeOut, timeUnit);
	}
	
	private <T> List<T> excuteTasksAndWaitForResult(List<Callable<T>> taskThreads,boolean isWaitForTaskFinish, long timeOut, TimeUnit timeUnit) {
		if(CommonMethod.isCollectionNotEmpty(taskThreads)){
			long beginTime = System.currentTimeMillis();
			int taskSize = taskThreads.size();
			logger.info("本次共"+taskSize+"个任务，开始执行时间:"+beginTime);
			List<Future<T>> list = null;
			List<T> result = new ArrayList<T>(taskSize);
			try {
				if(isWaitForTaskFinish)
					list = executorService.invokeAll(taskThreads);
				else
					list = executorService.invokeAll(taskThreads,timeOut, timeUnit);
				if(CommonMethod.isCollectionNotEmpty(list)){
					for (int i = 0; i < taskSize; i++) {
						 Future<T> future = list.get(i);
						 if(future != null){
		        			 T t = null;
							try {
								t = future.get();
								if(t != null)
			        				result.add(t);
							} catch (Exception e) {
								future.cancel(true);
								logger.error("e",e);
							}
		        			
		        		 }
					}
				}
			} catch (Exception e) {
				logger.error("e",e);
			}finally{
				colseThreadPool();
			}
			logger.info("完成所有任务耗时:"+(System.currentTimeMillis() - beginTime)+"ms");
			return result;
		}
		return new ArrayList<T>();
		
	}
	
}
