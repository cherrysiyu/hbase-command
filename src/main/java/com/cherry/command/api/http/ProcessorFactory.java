package com.cherry.command.api.http;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.cherry.command.api.dto.query.QueryBean;
import com.cherry.command.api.http.process.AbstractProcessor;
import com.cherry.command.api.query.result.CommandResult;

/**
 * 
 	处理器工厂，用来调用真正的处理方法
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午10:11:11
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class ProcessorFactory {
	/**
	 * 服务处理器缓存
	 */
	private static ConcurrentMap<String,AbstractProcessor> processors = new ConcurrentHashMap<String, AbstractProcessor>();
	
	private ProcessorFactory(){
		
	}
	/**
	 * 是否是合法正确的服务类型
	 * @param serviceType 服务类型
	 * @return
	 */
	public static boolean isCorrectServiceType(String serviceType){
		return processors.containsKey(serviceType);
	}
	/**
	 * 处理请求方法
	 * @param queryBean 查询请求的bean
	 * @return 返回结果
	 */
	public static CommandResult  processor(String serviceType, QueryBean queryBean)throws Exception{
		return processors.get(serviceType).realProcess(queryBean);
	}

	/**
	 * 初始化服务处理器
	 * @param processors
	 */
	public static void setProcessors(Map<String, AbstractProcessor> processors) {
		ProcessorFactory.processors.putAll(processors);
	}
	
	public static Set<String> getCorrectServiceTypes(){
		return processors.keySet();
	}
	
}
