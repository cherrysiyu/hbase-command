package com.cherry.command.api.http.process;

import java.util.Map;

import com.cherry.command.api.dto.query.ProcessorBean;
import com.cherry.command.api.dto.query.QueryBean;
import com.cherry.command.api.http.QueryConstans;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.CommonMethod;

/**
 * 
 复杂查询处理器，支持传入表名称查询条件查询表中的数据
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午10:42:02
 * <p>	
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class TableQueryProcessor extends AbstractProcessor {

	

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
		CommandResult processCommand = realProcessor.getProcessor().processCommand(command);
		if(!logDebug){
			queryBean.getRequestLog().append("realProcess invoke ").append(this.getClass().getSimpleName()).append(" success, cost: ").append(System.currentTimeMillis()-beginTime).append("ms");
		}
		return processCommand;
	}
	
	

}
