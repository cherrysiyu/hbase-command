package com.cherry.command.api.dto.query;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;

/**
 * 
基类 查询Bean
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 下午1:38:30
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class QueryBean {
	
	/**
	 * json字符串查询日条件
	 */
	private String jonsCondition;
	
	/**
	 * 请求日志
	 */
	private StringBuilder requestLog ;
	
	/**
	 * 日志级别
	 */
	private boolean isLogDebug;
	/**
	 * 主要为了后面还有参数可以设置
	 */
	private HttpServletRequest request;

	public QueryBean() {
		super();
		this.isLogDebug = LoggerFactory.getLogger(getClass()).isDebugEnabled();
		if(!isLogDebug){
			requestLog = new StringBuilder();
		}
	}

	public String getJonsCondition() {
		return jonsCondition;
	}

	public void setJonsCondition(String jonsCondition) {
		this.jonsCondition = jonsCondition;
	}

	public StringBuilder getRequestLog() {
		return requestLog;
	}

	public QueryBean addJonsCondition(String jonsCondition){
		setJonsCondition(jonsCondition);
		return this;
	}

	public boolean isLogDebug() {
		return isLogDebug;
	}

	public void setLogDebug(boolean isLogDebug) {
		this.isLogDebug = isLogDebug;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public QueryBean addRequest(HttpServletRequest request){
		setRequest(request);
		return this;
	}
	
	

}
