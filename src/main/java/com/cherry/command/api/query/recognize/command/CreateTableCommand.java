package com.cherry.command.api.query.recognize.command;

import java.util.List;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;

/**
 * 
 创建一个HBase的表,family建议不要超过2个，多了会对性能有影响，因为HBase是面向列族的，所以一般一个family就可以了
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 上午11:41:50
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class CreateTableCommand extends AbstractHbaseCommand{
	/**
	 * 不要超过2个，多了会对性能有影响，因为HBase是面向列族的，所以一般一个family就可以了
	 */
	private List<String> family;
	@SuppressWarnings("unused")
	private CreateTableCommand(){
		
	}
	@Override
	public int getCommandId() {
		return 1;
	}

	public CreateTableCommand(String tableName,List<String> family) {
		super(tableName);
		if(family == null || family.isEmpty()){
			throw new IllegalArgumentException("family is null or empty,please check it and then try again");
		}
		this.family = family;
	}

	public List<String> getFamily() {
		return family;
	}
	
	
	
}
