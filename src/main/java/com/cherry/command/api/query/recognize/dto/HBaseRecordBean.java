package com.cherry.command.api.query.recognize.dto;

import java.util.Map;

import com.cherry.command.api.utils.CommonMethod;

/**
 * 
 	插入HBase中需要的最基础的元素
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 上午10:32:56
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class HBaseRecordBean {
	/**
	 * 主键rowKey
	 */
	private String rowKey=String.valueOf((Long.MAX_VALUE-System.currentTimeMillis()));
	/**
	 * 列族名称
	 */
	private String familyName="infos";
	/**
	 * 数据
	 */
	private Map<String,String> columnData;
	
	public HBaseRecordBean() {
		super();
	}
	public HBaseRecordBean(String rowKey, String familyName,
			Map<String, String> columnData) {
		super();
		this.rowKey = rowKey;
		this.familyName = familyName;
		this.columnData = columnData;
	}
	public String getRowKey() {
		return rowKey;
	}
	public void setRowKey(String rowKey) {
		if(CommonMethod.isNotEmpty(rowKey))
			this.rowKey = rowKey;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		if(CommonMethod.isNotEmpty(familyName))
			this.familyName = familyName;
	}
	public Map<String, String> getColumnData() {
		return columnData;
	}
	public void setColumnData(Map<String, String> columnData) {
		this.columnData = columnData;
	}
	public HBaseRecordBean addRowKey(String rowKey){
		setRowKey(rowKey);
		return this;
	}
	public HBaseRecordBean addFamilyName(String familyName){
		setFamilyName(familyName);
		return this;
	}
	public HBaseRecordBean addColumnData(Map<String, String> columnData){
		setColumnData(columnData);
		return this;
	}
}
