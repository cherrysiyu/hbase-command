package com.cherry.command.api.http.common;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.query.result.CommandResult;
/**
 * 
 	基类Servlet，需要考虑到以后的扩展，基类中提供一些公用的方法
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月21日 下午6:04:23
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class BaseServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6341072294312556128L;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 输出返回错误消息
	 * @param errorMsg 错误信息
	 * @param response
	 */
	public void printFaildResult(String errorMsg,HttpServletResponse response){
		printResult(new CommandResult().addMsg(errorMsg),response);
	}
	/**
	 * 输出返回结果消息
	 * @param result
	 * @param response
	 */
	public void printResult(CommandResult result,HttpServletResponse response){
		try {
			ResponseCommon.printJSON(response, result);
		} catch (Exception e) {
			logger.error("ResponseCommon.printJSON error",e);
		}
	}
	
	
	/**
	 * 从request中获得类型为字符串的参数值
	 * @param param_name 参数名
	 * @param default_value 参数值为NULL时的默认值
	 * @return 用法如: String endTime = getStrParam("endTime","");
	 */
	public String getStrParam(HttpServletRequest request,String param_name,String default_value){
		return request.getParameter(param_name)!=null?request.getParameter(param_name):default_value;
	}
	
	/**
	 * 从context.request中获得类型为长整型的参数值
	 * @param param_name 参数名
	 * @param default_value 参数值为NULL时的默认值
	 * @return 
	 */
	public long getLongParam(HttpServletRequest request,String param_name,long default_value){
		return request.getParameter(param_name)!=null&&!request.getParameter(param_name).equals("")?Long.parseLong(request.getParameter(param_name)):default_value;
	}
	
	/**
	 * 从context.request中获得类型为整型的参数值
	 * @param param_name 参数名
	 * @param default_value 参数值为NULL时的默认值
	 * @return
	 */
	public int getIntParams(HttpServletRequest request,String param_name,int default_value){
		return request.getParameter(param_name)!=null&&!request.getParameter(param_name).equals("")?Integer.parseInt(request.getParameter(param_name)):default_value;
	}
	
}
