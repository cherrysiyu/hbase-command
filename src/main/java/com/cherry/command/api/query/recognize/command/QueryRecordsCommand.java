package com.cherry.command.api.query.recognize.command;

import java.util.ArrayList;
import java.util.List;

import com.cherry.command.api.query.recognize.BaseQuery;

/**
 * 
 注意：如果rowKeys为空则查询表中的所有数据,如果columnNames是空则返回所有表中的字段
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 下午1:19:20
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class QueryRecordsCommand extends BaseQuery{
	/**
	 * rowKey,相当于传统RDBMS表中的主键
	 */
	private List<String> rowKeys = new ArrayList<String>();

	@SuppressWarnings("unused")
	private QueryRecordsCommand(){
		
	}
	
	public QueryRecordsCommand(String tableName) {
		super(tableName);
	}

	@Override
	public int getCommandId() {
		return 8;
	}

	public List<String> getRowKeys() {
		return rowKeys;
	}

	public QueryRecordsCommand addRowKey(String rowKey){
		if(rowKey != null && !"".equals(rowKey))
			this.rowKeys.add(rowKey);
		return this;
	}
	public QueryRecordsCommand addRowKeys(List<String>  rowKeys){
		if(rowKeys != null && !rowKeys.isEmpty())
			this.rowKeys.addAll(rowKeys);
		return this;
	}
}
