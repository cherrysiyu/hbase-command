package com.cherry.command.api.query.action;

import java.util.List;
import java.util.Map;

import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.CommonMethod;

/**
 * 
 
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 下午3:04:41
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class AbstractActionProcessor {

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
	/**
	 * 
	 * @param strData
	 * @return
	 */
	public CommandResult buildStringResult(String strData){
		return new CommandResult().addSuccessStatusCode().addStrData(strData);
	}
	/**
	 * 
	 * @param listData
	 * @return
	 */
	public CommandResult buildListResult(List<?> listData){
		return new CommandResult().addSuccessStatusCode().addListData(listData);
	}
	/**
	 * 
	 * @param mapData
	 * @return
	 */
	public CommandResult buildMapResult(Map<String,?> mapData){
		return new CommandResult().addSuccessStatusCode().addMapData(mapData);
	}
}
