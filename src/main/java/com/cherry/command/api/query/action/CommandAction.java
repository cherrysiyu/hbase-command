package com.cherry.command.api.query.action;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.result.CommandResult;

/**
 * 
 服务处理接口
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 下午4:30:35
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public interface CommandAction {
	/**
	 * 服务处理接口方法
	 * @param cmd 命令
	 * @return
	 * @throws Exception
	 */
	public CommandResult processCommand(AbstractHbaseCommand cmd) throws Exception;
}
