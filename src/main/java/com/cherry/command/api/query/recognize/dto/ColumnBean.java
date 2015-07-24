package com.cherry.command.api.query.recognize.dto;

/**
 * 
 
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 上午11:55:30
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class ColumnBean {
	/**
	 * 主键rowKey
	 */
	private String rowKey;
	/**
	 * 列族名称
	 */
	private String familyName;
	/**
	 * 字段名称
	 */
	private String columnName;
	/**
	 * 字段值
	 */
	private String columnValue;
	
	//不允许直接实例化不带参数，但是json需要反射用
	@SuppressWarnings("unused")
	private ColumnBean(){
		
	}
	
	public ColumnBean(String rowKey, String familyName, String columnName) {
		super();
		this.rowKey = rowKey;
		this.familyName = familyName;
		this.columnName = columnName;
	}
	public ColumnBean(String rowKey, String familyName, String columnName,
			String columnValue) {
		super();
		this.rowKey = rowKey;
		this.familyName = familyName;
		this.columnName = columnName;
		this.columnValue = columnValue;
	}
	public String getRowKey() {
		return rowKey;
	}
	public String getFamilyName() {
		return familyName;
	}
	public String getColumnName() {
		return columnName;
	}
	public String getColumnValue() {
		return columnValue;
	}

	
}
