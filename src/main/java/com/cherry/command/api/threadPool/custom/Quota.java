package com.cherry.command.api.threadPool.custom;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  配额管理
 * @author Administrator
 *
 */
public final class Quota {
	private final AtomicInteger state;
	public final int value;

	public Quota(int value) {
		if (value < 0)
			throw new IllegalArgumentException("Quota should not less than 0.");
		this.value = value;
		this.state = new AtomicInteger(value);
	}

	/** @return 当前剩余配额. */
	public int state() {
		return state.get();
	}

	/**
	 * 占据一个配额.
	 * 
	 * @return false 表示预留的配额已用完, 反之为true.
	 */
	public boolean acquire() {
		if (state() == 0)
			return false;
		if (state.decrementAndGet() >= 0)
			return true;
		state.incrementAndGet();
		return false;
	}

	/**
	 * 释放一个配额.
	 * 
	 * @return false 表示无效的释放, 正常情况下不应出现, 反之为true.
	 */
	public boolean release() {
		if (state() == value)
			return false;
		if (state.incrementAndGet() <= value)
			return true;
		state.decrementAndGet();
		return false;
	}
}

