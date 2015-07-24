package com.cherry.command.api.threadPool;



import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 1.线程执行抽象类,通过线程池执行，如果不需要返回值的请调用excuteRunnableTasks(List&lt;? extends Runnable&gt; taskThreads)方法
 * 或者excuteCallableTasks(List&lt;Callable&lt;T&gt;&gt; taskThreads)方法.如果有返回值的请调用excuteTasksAndWaitForResult(List&lt;Callable&lt;T&gt;&gt; taskThreads)<br>
 * 
 * 2.如果执行的多线程是有按照顺序执行有依赖关系的话，推荐使用CallableThreadExector类,并且{@code ThreadExector threadExector = new CallableThreadExector(1)},  <span style="font:bold;color:red">一定要注意保持poolSize线程池的大小是1</span>;
 * 调用excuteCallableTasks(List&lt;Callable&lt;T&gt;&gt; taskThreads)方法
 * 或者excuteTasksAndWaitForResult(List&lt;Callable&lt;T&gt;&gt; taskThreads)，这2个方法会按照提交任务的顺
 * 
 * 3.如果需要执行多线程集合任务，并且必须等待所有任务都执行完毕后返回是否执行成功，可以调用excuteAndWaitForAllTasksFinish(List&lt;Callable&lt;T&gt;&gt; taskThreads)方法,
 * 如果是没有返回值的Runnable任务的话，可以转换成Callable&lt;Object&gt;&gt;任务，然后doWork();,最后返回new Object();,具体可以参考com.toft.object.test.ThreadPoolTest类的testWait()方法
 * @description:
 * @author:Cherry
 * @version:1.0
 * @date:Dec 20, 2012
 */
public abstract class ThreadExector {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected ExecutorService executorService = null;
	private static final int cpuSize = Runtime.getRuntime().availableProcessors();
	private static final int DEFAULT_INITIAL_CAPACITY = 8;
	
	public ThreadExector(){
		this(cpuSize+1);
	}
	/**
	 * 初始化线程池
	 * 如果poolSize的大小超过CPU的核数以后，同一时刻实际不需要那么多，cpu的核数大小有限，只有等于cpu核数时才有最好的性能
		如果可运行的线程数多有可用的处理器数线程将会空闲，大量空闲线程会占用更多的内存，给垃圾回收器带来压力，而且大量线程在竞争CPU的资源，还会产生其他的性能开销
		如果已经有了足够多的线程保持CPU忙碌，那么再创建更多的线程是有百害而无一利的
	 * @param poolSize
	 */
	public ThreadExector(int poolSize){
		initThreadPool(poolSize);
	}
	
	
	public ThreadExector(ExecutorService executorService){
		if(executorService != null){
			this.executorService = executorService;
		}else{
			logger.info("传入的ExecutorService为null，采用默认的线程池...");
			initThreadPool(cpuSize);
		}
	}
	
	/**
	 * 初始化线程池
	 * @param poolSize
	 */
	private void initThreadPool(int poolSize){
		logger.info("initThreadPool--->poolSize=" + poolSize);
		if(poolSize <= 0){
			 throw new IllegalArgumentException("Illegal poolSize: "+ poolSize +",poolSize must  > 0");
		}
		else if(poolSize == 1){
			executorService = Executors.newSingleThreadExecutor();
		}else{
			/*	如果poolSize的大小超过CPU的核数以后，同一时刻实际不需要那么多，cpu的核数大小有限，只有等于cpu核数时才有最好的性能
				如果可运行的线程数多有可用的处理器数线程将会空闲，大量空闲线程会占用更多的内存，给垃圾回收器带来压力，而且大量线程在竞争CPU的资源，还会产生其他的性能开销
				如果已经有了足够多的线程保持CPU忙碌，那么再创建更多的线程是有百害而无一利的
			*/
			if(poolSize > cpuSize * DEFAULT_INITIAL_CAPACITY){
				poolSize =  cpuSize * DEFAULT_INITIAL_CAPACITY;
				logger.info("poolSize的大小超过CPU核数的"+DEFAULT_INITIAL_CAPACITY+"倍，重新设置poolSize的大小=" + poolSize);
			}
			executorService = Executors.newFixedThreadPool(poolSize);
		}
	}
	

	/**
	 * 关闭线程池
	 */
	protected void colseThreadPool(){
		if(executorService != null){
			executorService.shutdown();
		}
	}
	
	/**
	 * <span style="font:bold">通过线程池直接执行，不等待返回结果，和传统意义上的多线程是一个意思,异步的</span>
	 * @param taskThreads Runnablel类型的线程任务集合
	 */
	public abstract  void excuteRunnableTasks(List<? extends Runnable> taskThreads);
	
	/**
	 * <span style="font:bold">通过线程池直接执行，不等待返回结果，和传统意义上的多线程是一个意思,异步的</span>
	 * @param <T> 
	 * @param taskThreads 待执行的Callable&lt;T&gt;任务集合
	 */
	public abstract <T> void excuteCallableTasks(List<Callable<T>> taskThreads);
	
	/**
	 * <span style="font:bold">执行所有任务，并且等待，所有任务执行完成后返回true</span>
	 * @param <T>
	 * @param taskThreads 待执行的Callable&lt;T&gt;任务集合
	 * @return 全部执行完成则返回true;
	 */
	public abstract <T>  boolean excuteAndWaitForAllTasksFinish(List<Callable<T>> taskThreads);
	
	/**
	 *<span style="font:bold">通过线程池直接执行，异步执行并且等待返回的结果</span>
	 * @param <T>
	 * @param taskThreads  待执行的Callable&lt;T&gt;任务集合
	 * @return @code{List&lt;T&gt;} 返回的结果
	 */
	public abstract <T> List<T> excuteTasksAndWaitForResult(List<Callable<T>> taskThreads);
	
	/**
	 *<span style="font:bold">通过线程池直接执行，异步执行并且等待返回的结果</span>
	 * @param <T>
	 * @param taskThreads  待执行的Callable&lt;T&gt;任务集合
	 * @param timeOut 超时时间
	 * @param timeUnit 时间单元
	 * @return @code{List&lt;T&gt;} 返回的结果
	 */
	public abstract <T> List<T> excuteTasksAndWaitForResult(List<Callable<T>> taskThreads,long timeOut,TimeUnit timeUnit);
	
	
}
