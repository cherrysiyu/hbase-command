package com.cherry.command.api.query.result;

import java.util.List;
import java.util.Map;

import com.cherry.command.api.http.QueryConstans;
import com.cherry.command.api.utils.CommonMethod;

/**
 * 
 	返回结果类,此类支持链式编程，推荐使用链式编程，例如new BaseResult().addStatusCode(xx).addMsg(xxx);
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午11:07:12
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public  class CommandResult {

	/**
	 * 状态码，用来标示成功和失败的标识信息，不用boolean来定义数据类型是因为可能以后会扩展状态码
	 * -1标示失败，1标示成功
	 */
	private int statusCode=QueryConstans.ERROR_STATUS_CODE;
	/**
	 * 信息，当成功时msg是空,失败时是失败的信息
	 */
	private String msg="";
	
	/**
	 * 返回的类型值,1标示list类型，2标示string类型，3标示map类型
	 */
	private int valueType=1;
	/**
	 * 字符串类型返回值
	 */
	private String strData;
	
	/**
	 * list类型返回值，一般返回是{@code List<Map<String,String>>}
	 */
	private List<?> listData;
	
	/**
	 * map类型返回值
	 */
	private Map<String,?> mapData;
	/**
	 * 扩展字段
	 */
	private Object ext;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		if(CommonMethod.isNotEmpty(msg)){
			this.msg = msg;
			this.valueType = ValueTypeEnum.STRINGDATA.getValueType();
		}
	}
	
	public CommandResult addMsg(String msg){
		setMsg(msg);
		return this;
	}
	
	public CommandResult addStatusCode(int statusCode){
		setStatusCode(statusCode);
		return this;
	}
	public CommandResult addSuccessStatusCode(){
		setStatusCode(QueryConstans.SUCCESS_STATUS_CODE);
		return this;
	}

	public CommandResult addExt(Object ext){
		setExt(ext);
		return this;
	}
	
	public Object getExt() {
		return ext;
	}

	public void setExt(Object ext) {
		this.ext = ext;
	}

	public int getValueType() {
		return valueType;
	}
	
	public List<?> getListData() {
		return listData;
	}

	public void setListData(List<?> listData) {
		if(listData != null && !listData.isEmpty()){
			this.valueType = ValueTypeEnum.LISTDATA.getValueType();
			this.listData = listData;
		}
	}
	public CommandResult addListData(List<?> listData){
		setListData(listData);
		return this;
	}
	
	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		if(strData != null){
			this.valueType = ValueTypeEnum.STRINGDATA.getValueType();
			this.strData = strData;
			
		}
	}
	public CommandResult addStrData(String strData){
		setStrData(strData);
		return this;
	}
	public Map<String, ?> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, ?> mapData) {
		if(mapData != null && !mapData.isEmpty()){
			this.valueType = ValueTypeEnum.MAPDATA.getValueType();
			this.mapData = mapData;
		}
	}
	public CommandResult addMapData(Map<String, ?> mapData){
		setMapData(mapData);
		return this;
	}
	
	
}
