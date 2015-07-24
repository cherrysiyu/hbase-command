package com.cherry.command.api.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cherry.command.api.dto.query.QueryBean;
import com.cherry.command.api.http.common.BaseServlet;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.CommonMethod;


/**
 * 
 	查询请求Servlet，实现了所有的查询请求逻辑
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午9:14:25
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class QueryServlet extends BaseServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5570639208192121133L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long beginTime = System.currentTimeMillis();
		String serviceType = getStrParam(request,QueryConstans.PARAM_SERVICETYPE,QueryConstans.DEFAULT_SERVICETYPE);
		if(!ProcessorFactory.isCorrectServiceType(serviceType)){
			printFaildResult(new StringBuilder().append("serviceType is incorrect ,serviceType values ")
					.append(ProcessorFactory.getCorrectServiceTypes())
						.append(",please check it and try again").toString(), response);
		}else{
			try{
				String jsonCondtions = getStrParam(request, QueryConstans.PARAM_REQUESTINFO,null);
				QueryBean queryBean = new QueryBean().addJonsCondition(jsonCondtions).addRequest(request);
				CommandResult processor = ProcessorFactory.processor(serviceType,queryBean);
				printResult(processor,response);
				long betweenTime = System.currentTimeMillis() - beginTime;
				StringBuilder sb   = new StringBuilder("request params[serviceType=");
				sb.append(serviceType).append(",").append("jsonCondtions=").append(jsonCondtions).append("]\t");
				sb.append("request complite cost ").append(betweenTime).append("ms");
				if(!queryBean.isLogDebug()){//记录业务上的日志，必须分开否则会消耗系统的性能
					logger.info(sb.append("\t detail cost:").append(queryBean.getRequestLog()).toString());
				}else{//最基础的debug日志
					logger.debug(sb.toString());
				}
			}catch(Exception ex){
				String errorMsg = CommonMethod.getStackTraceMessage(ex);
				printFaildResult(errorMsg, response);
				logger.error(errorMsg);
			}
		}
	}
}
