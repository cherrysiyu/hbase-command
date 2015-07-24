package com.cherry.command.api.query.recognize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 HBase操作命名抽象类
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 上午11:41:18
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public abstract class AbstractHbaseCommand {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * Hbase的表名称
	 */
	protected String tableName;
	/**
	 * 命令id
	 */
	protected int commondId;
	
	
	protected AbstractHbaseCommand(){
		
	}
	
	public AbstractHbaseCommand(String tableName) {
		if(tableName == null || "".equals(tableName)){
			throw new IllegalArgumentException("tableName is null or empty,please check it and then try again");
		}
		this.tableName = tableName;
	}


	/**
	 * 每一种命令需要一种命令id,命令id不能重复
	 * @return
	 */
	public abstract int getCommandId();
	
	
	public String getTableName() {
		return tableName;
	}
	
	
}
