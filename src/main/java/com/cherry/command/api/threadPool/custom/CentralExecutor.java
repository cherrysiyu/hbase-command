package com.cherry.command.api.threadPool.custom;


import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class CentralExecutor implements Executor {
	private static final String CLASS_NAME = CentralExecutor.class.getSimpleName();

	public final ExecutorService service;// 线程池
	private final Policy policy;// 策略
	private final Map<Class<? extends Runnable>, Submitter> quotas;// 任务类型和配额使用的关系
	private final int threadSize;// 最大线程数

	private int reserved;

	public CentralExecutor(final int threadSize, Policy policy) {
		this.threadSize = threadSize;
		this.policy = policy;
		this.service = newFixedThreadPool(threadSize, new DebugableThreadFactory(CLASS_NAME));
		this.quotas = new ConcurrentHashMap<Class<? extends Runnable>, Submitter>();
	}

	public CentralExecutor(int threadSize) {
		this(threadSize, Policy.PESSIMISM);
	}

	/** @see ExecutorService#shutdownNow() */
	public List<Runnable> shutdownNow() {
		return service.shutdownNow();
	}

	/** @see ExecutorService#shutdown() */
	public void shutdown() {
		service.shutdown();
	}

	/** @see ExecutorService#isShutdown() */
	public boolean isShutdown() {
		return service.isShutdown();
	}

	/** @see ExecutorService#isTerminated() */
	public boolean isTerminated() {
		return service.isTerminated();
	}

	/** @see ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit) */
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return service.awaitTermination(timeout, unit);
	}
	
	public Map<Class<? extends Runnable>, Submitter> getQuotas() {
		return quotas;
	}

	/**
	 * 根据对应任务类型配置来执行线程
	 */
	@Override
	public void execute(Runnable task) {
		final Submitter submitter = quotas.get(task.getClass());
		if (submitter != null)
			submitter.submit(task, this);
		else
			policy.defaultSubmitter().submit(task, this);
	}

	/** @return 预留配额. */
	public static Quota reserve(int value) {
		return new Quota(value);
	}

	/** @return 弹性配额. */
	public static Quota elastic(int value) {
		return new Quota(value);
	}

	/** @return 零配额. */
	public static Quota nil() {
		return new Quota(0);
	}

	/**
	 * 设定taskClass的保留和限制配额.
	 * 
	 * @param taskClass
	 * @param reserve
	 * @param elastic
	 * 
	 * @throws IllegalArgumentException
	 */
	public void quota(Class<? extends Runnable> taskClass, Quota reserve, Quota elastic) {

		synchronized (this) {
			if (reserve.value > threadSize - reserved)
				throw new IllegalArgumentException("No resource for reserve");
			reserved += reserve.value;
		}

		quotas.put(taskClass, policy.submitter(reserve, elastic));
	}

	public synchronized boolean hasUnreserved() {
		return threadSize > reserved;
	}

}
