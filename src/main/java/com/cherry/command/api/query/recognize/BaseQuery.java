package com.cherry.command.api.query.recognize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseQuery extends AbstractHbaseCommand{
	
	protected BaseQuery(){
		
	}
	public BaseQuery(String tableName) {
		super(tableName);
	}
	/**
	 * 相当于传统RDBMS表中列名称，可借由此字段实现用户自定义字段功能
	 */
	protected Set<String> columnNames = new HashSet<String>();
	/**
	 * 是否需要RowKey
	 */
	protected boolean isNeedRowKey;
	/**
	 * 最大返回的条数(最多返回多少条记录)，如果此参数不填或者是0，则全部返回
	 */
	protected long maxSize;
	
	/**
	 * 开始时间：和结束时间必须成对出现才有效
	 */
	protected long beginTime;
	/**
	 * 结束时间：和开始时间必须成对出现才有效
	 */
	protected long endTime;
	
	public abstract int getCommandId();
	
	public BaseQuery addColumnName(String columnName){
		if(columnName != null && !"".equals(columnName))
			this.columnNames.add(columnName);
		return this;
	}
	public BaseQuery addColumnNames(List<String>  columnNames){
		if(columnNames != null && !columnNames.isEmpty())
			this.columnNames.addAll(columnNames);
		return this;
	}
	
	public BaseQuery addTimeRange(long beginTime,long endTime){
		if(beginTime >0 && endTime >0 && beginTime < endTime){
			this.beginTime = beginTime;
			this.endTime = endTime;
		}
		return this;
	}
	
	
	public Set<String> getColumnNames() {
		return columnNames;
	}
	public boolean isNeedRowKey() {
		return isNeedRowKey;
	}
	public void setNeedRowKey(boolean isNeedRowKey) {
		this.isNeedRowKey = isNeedRowKey;
	}
	public BaseQuery addMaxSize(long maxSize){
		if(maxSize>0)
			this.maxSize = maxSize;
		return this;
	}
	public long getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
	public void setColumnNames(Set<String> columnNames) {
		this.columnNames = columnNames;
	}
	public long getBeginTime() {
		return beginTime;
	}
	public long getEndTime() {
		return endTime;
	}
	
	

}
