package com.cherry.command.api.threadPool.custom;

public interface Submitter {
	void submit(Runnable task, CentralExecutor executor);
	
	boolean isHasQuota();
}
