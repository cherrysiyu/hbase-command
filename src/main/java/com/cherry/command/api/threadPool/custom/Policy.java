package com.cherry.command.api.threadPool.custom;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Policy {

	/** 乐观策略, 在存在为分配的配额情况下, 一旦出现闲置线程, 允许任务抢占, 抢占的优先级由提交的先后顺序决定. */
	OPTIMISM {

		/** 未定义配额的任务将直接进入等待队列, 但优先级低于所有定义了配额的任务. */
		private final Submitter defaultSubmitter = new Submitter() {
			@Override
			public void submit(Runnable task, CentralExecutor executor) {
				enqueue(new ComparableTask(task, Integer.MAX_VALUE));
			}

			@Override
			public boolean isHasQuota() {
				return false;
			}
		};

		@Override
		Submitter defaultSubmitter() {
			return defaultSubmitter;
		}

		@Override
		Submitter submitter(final Quota reserve, final Quota elastic) {
			return new Submitter() {
				@Override
				public void submit(final Runnable task, CentralExecutor executor) {
					if (reserve.acquire())
						doSubmit(task, executor, reserve);
					// 若存在为分配的预留配额, 则弹性配额进行争抢
					else if (executor.hasUnreserved() && elastic.acquire())
						doSubmit(task, executor, elastic);
					// 同悲观策略进入等待队列
					else
						enqueue(new ComparableTask(task, reserve.value));
				}

				@Override
				public boolean isHasQuota() {
					return reserve.state() > 0 || elastic.state() > 0;
				}
			};
		}
	},

	/** 悲观策略, 在所有线程都被预留的情况下, 即使当前预留之外的线程是空闲, 也不会被抢占, 即Elastic的设定将被忽略. */
	PESSIMISM {

		private final Submitter defaultSubmitter = new Submitter() {
			@Override
			public void submit(Runnable task, CentralExecutor executor) {
				throw new RejectedExecutionException("Unquotaed task can not be executed in pessimism.");
			}

			@Override
			public boolean isHasQuota() {
				return false;
			}
		};

		@Override
		Submitter defaultSubmitter() {
			return defaultSubmitter;
		}

		@Override
		Submitter submitter(final Quota reserve, final Quota elastic) {
			if (reserve.value == 0)
				throw new IllegalArgumentException("None-reserve task will never be executed in pessimism.");

			return new Submitter() {
				@Override
				public void submit(final Runnable task, CentralExecutor executor) {
					if (reserve.acquire())
						doSubmit(task, executor, reserve);
					// 耗尽预留配额后, 进入等待队列, 按预留额度大小排优先级, 大者优先.
					else
						enqueue(new ComparableTask(task, reserve.value));
				}

				@Override
				public boolean isHasQuota() {
					return reserve.state() > 0;
				}
			};
		}
	};

	/** 优先级等待队列. */
	private final PriorityBlockingQueue<ComparableTask> queue = new PriorityBlockingQueue<ComparableTask>();

	abstract Submitter submitter(Quota reserve, Quota elastic);

	abstract Submitter defaultSubmitter();

	/** 将任务入等待队列. */
	void enqueue(ComparableTask task) {
		queue.put(task);
		// LOGGER.debug("Enqueue {}", task.original);
	}

	/** 将任务出列重新提交给执行器. */
	void dequeueTo(CentralExecutor executor) {
		//try {
			final ComparableTask  task = queue.poll();
			// LOGGER.debug("Dequeue {}", task);
			if (task != null)
				executor.execute(task.original);
		//} catch (InterruptedException e) {
		//	Thread.currentThread().interrupt();
		//	logger.debug("Dequeue has been interrupted ", e);
		//}
	}

	void doSubmit(Runnable task, CentralExecutor executor, Quota quota) {
		executor.service.execute(new Decorator(task, quota, executor));
	}

	/** {@link ComparableTask} */
	static class ComparableTask implements Comparable<ComparableTask> {
		final Runnable original;
		private final int quota;

		public ComparableTask(Runnable task, int quota) {
			this.original = task;
			this.quota = quota;
		}

		@Override
		public int compareTo(ComparableTask o) {
			return -(quota - o.quota);
		}
	}

	/** {@link Decorator} */
	class Decorator implements Runnable {
		private Logger logger = LoggerFactory.getLogger(getClass());
		private final Runnable task;
		private final Quota quota;
		private final CentralExecutor executor;

		public Decorator(Runnable task, Quota quota, CentralExecutor executor) {
			this.task = task;
			this.quota = quota;
			this.executor = executor;
		}

		@Override
		public void run() {
			try {
				task.run();
			} catch (Throwable t) {
				logger.error("Unexpected Interruption cause by",t);
			} finally {
					ThreadPoolExecutor s = (ThreadPoolExecutor) executor.service;
					logger.debug("finally ThreadPoolExecutor ActiveCount:" + s.getActiveCount() + ";PoolSize:"
							+ s.getPoolSize() + ";CompletedTaskCount:" + s.getCompletedTaskCount());
				quota.release();
				dequeueTo(executor);
			}
		}
	}
}
