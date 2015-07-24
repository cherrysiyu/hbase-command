package com.cherry.command.api.http.process;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.dto.query.ProcessorBean;
import com.cherry.command.api.dto.query.QueryBean;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.ClassUtil;
import com.cherry.command.api.utils.CommonMethod;
import com.cherry.command.api.utils.JacksonUtils;
import com.cherry.command.api.utils.ReflectUtils;

/**
 * 
  抽象处理器类
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午10:13:16
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public abstract class AbstractProcessor {
	protected static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
	protected JacksonUtils jacksonUtils = JacksonUtils.getInstance();
	private static final String commandPackage = "com.cplatform.surf.news.classify.api.query.recognize.command";
	private static final String processorPackage = "com.cplatform.surf.news.classify.api.query.action.process";
	private static final String getCommandIdMethodName ="getCommandId";
	private static  ConcurrentMap<String,ProcessorBean> realProcessors = new ConcurrentHashMap<String, ProcessorBean>();
	static{
		try{
			List<Class<?>> classes = ClassUtil.getClasses(commandPackage);  
			for (Class<?> classz : classes) {
					String commandName = classz.getSimpleName();
					Constructor<?> constructor=classz.getDeclaredConstructor();
					constructor.setAccessible(true); 
					Object command = constructor.newInstance();
					int commandId =(Integer) MethodUtils.invokeMethod(command, getCommandIdMethodName,null);
					String procssorClassName = processorPackage+"."+commandName.replaceFirst("Command", "Processor");
					Class<?> processClass = Class.forName(procssorClassName); 
					Constructor<?> declaredConstructor = processClass.getDeclaredConstructor();
					declaredConstructor.setAccessible(true); 
					CommandAction instance = (CommandAction)declaredConstructor.newInstance();
					realProcessors.put(String.valueOf(commandId), new ProcessorBean(commandId, classz, instance));
			}
		}catch(Exception e){
			logger.error("init error System exit",e);
			System.exit(1);
		}
		logger.info("init system processors success !!!");
	}
	/**
	 * 真实处理方法
	 * @param queryBean 请求的queryBean
	 */
	public abstract CommandResult realProcess(QueryBean queryBean)throws Exception;
	
	/**
	 * 获取处理类
	 * @param commandId
	 * @return
	 */
	public ProcessorBean getRealProcessor(String commandId){
		return realProcessors.get(commandId);
	}
	
	/**
	 * 获取正确支持的CommondIds
	 * @return
	 */
	public Set<String> getCorrectCommandIds(){
		return realProcessors.keySet();
	}
	/**
	 * 构建错误信息结果
	 * @param errorMsg 错误信息
	 * @return
	 */
	public CommandResult buildFaildResult(String errorMsg){
		return new CommandResult().addMsg(errorMsg);
	}
	/**
	 * 构建异常信息结果
	 * @param ex 异常
	 * @return
	 */
	public CommandResult buildFaildResult(Exception ex){
		return new CommandResult().addMsg(CommonMethod.getStackTraceMessage(ex));
	}
	
	
}
