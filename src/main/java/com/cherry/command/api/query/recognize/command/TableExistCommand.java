package com.cherry.command.api.query.recognize.command;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;

/**
 * 
 HBASE表名称是否存在
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 上午11:44:46
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class TableExistCommand extends AbstractHbaseCommand{

	@SuppressWarnings("unused")
	private TableExistCommand(){
		
	}
	public TableExistCommand(String tableName) {
		super(tableName);
	}

	@Override
	public int getCommandId() {
		return 3;
	}

}
