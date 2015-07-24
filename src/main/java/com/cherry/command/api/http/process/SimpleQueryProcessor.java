package com.cherry.command.api.http.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import com.cherry.command.api.dto.query.ProcessorBean;
import com.cherry.command.api.dto.query.QueryBean;
import com.cherry.command.api.http.QueryConstans;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.thread.CustomTestThread;
import com.cherry.command.api.threadPool.ThreadExector;
import com.cherry.command.api.threadPool.ThreadPoolFactory;
import com.cherry.command.api.utils.CommonMethod;

/**
 * 
 	简单查询处理器
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午10:47:54
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class SimpleQueryProcessor extends AbstractProcessor {
	
	@Override
	public CommandResult realProcess(QueryBean queryBean)throws Exception{
		boolean logDebug = queryBean.isLogDebug();
		long beginTime = 0;
		if(!logDebug){
			beginTime = System.currentTimeMillis();
		}
		String jonsCondition = queryBean.getJonsCondition();
		if(CommonMethod.isEmpty(jonsCondition)){
			return buildFaildResult("jonsCondition is not null or empty,please check it and then try again");
		}
		Map<String, Object> jsonMap = jacksonUtils.readJSON2Map(jonsCondition);
		if(jsonMap == null || !jsonMap.containsKey(QueryConstans.PARAM_COMMANDID) || CommonMethod.isEmpty(jsonMap.get(QueryConstans.PARAM_COMMANDID).toString())){
			return buildFaildResult("commandId missing or  not support,commandId["+getCorrectCommandIds()+"],please check it and then try again");
		}
		ProcessorBean realProcessor = getRealProcessor(jsonMap.get(QueryConstans.PARAM_COMMANDID).toString());
		if(realProcessor == null){
			return buildFaildResult("commandId is not support,commondId["+getCorrectCommandIds()+"],please check it and then try again");
		}
		AbstractHbaseCommand command = (AbstractHbaseCommand)jacksonUtils.readJSON2Bean(realProcessor.getCommandClass(), queryBean.getJonsCondition());
		if(CommonMethod.isEmpty(command.getTableName())){
			return buildFaildResult("tableName or family is  empty,please check it and then try again");
		}
		
		HttpServletRequest request = queryBean.getRequest();
		int loopNum = request.getParameter(QueryConstans.LOOPNUM_PARAM)!=null&&!request.getParameter(QueryConstans.LOOPNUM_PARAM).equals("")?Integer.parseInt(request.getParameter(QueryConstans.LOOPNUM_PARAM)):1;
		int threadNum = request.getParameter(QueryConstans.THREADNUM_PARAM)!=null&&!request.getParameter(QueryConstans.THREADNUM_PARAM).equals("")?Integer.parseInt(request.getParameter(QueryConstans.THREADNUM_PARAM)):1;
		ThreadExector exector = ThreadPoolFactory.getCallableThreadExector(threadNum);
		List<Callable<StringBuilder>> threadTasks = new ArrayList<Callable<StringBuilder>>(threadNum);
		for (int i = 0; i < threadNum; i++) {
			threadTasks.add(new CustomTestThread(loopNum, command, realProcessor));
		}
		List<StringBuilder> result = exector.excuteTasksAndWaitForResult(threadTasks);
		if(!logDebug){
			queryBean.getRequestLog().append("realProcess invoke ").append(this.getClass().getSimpleName()).append(" success, cost: ").append(System.currentTimeMillis()-beginTime).append("ms");
		}
		return new CommandResult().addListData(result).addSuccessStatusCode();
	}

}
