package com.cherry.command.api.thread;

import java.util.concurrent.Callable;

import com.cherry.command.api.dto.query.ProcessorBean;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;

/**
 * 自定义测试线程
 * @author Cherry
 * @version 0.1
 * @Desc
 * 2014年10月27日 下午8:45:09
 */
public class CustomTestThread implements Callable<StringBuilder>{

	private int loopNum=10;
	private AbstractHbaseCommand command;
	private ProcessorBean realProcessor;
	
	public CustomTestThread(int loopNum, AbstractHbaseCommand command,ProcessorBean realProcessor) {
		super();
		this.loopNum = loopNum;
		this.command = command;
		this.realProcessor = realProcessor;
	}



	@Override
	public StringBuilder call() throws Exception {
		long beginTime = System.currentTimeMillis();
		for (int i = 0; i < loopNum; i++) {
			 realProcessor.getProcessor().processCommand(command);
		}
		long costTime = System.currentTimeMillis()-beginTime;
		StringBuilder sb = new StringBuilder();
		sb.append("线程").append(Thread.currentThread().getName()).append("--执行循环次数:")
			.append(loopNum).append("---总耗时:").append(costTime).append("ms").append("---平均每次耗时:")
			.append(costTime/loopNum).append("ms").append("                                                            ");
		return sb;
	}

}
