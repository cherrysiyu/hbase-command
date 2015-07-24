package com.cherry.command.api.query.action.process;

import java.util.Map;

import org.apache.hadoop.hbase.io.TimeRange;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.QueryColumnRecordCommand;
import com.cherry.command.api.query.recognize.dto.ColumnBean;
import com.cherry.command.api.query.result.CommandResult;

public class QueryColumnRecordProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)throws Exception {
		QueryColumnRecordCommand command = (QueryColumnRecordCommand)cmd;
		ColumnBean columnBean = command.getColumnBean();
		if(columnBean != null){
			Map<String, String> map = HBaseUtils.convertResult2Map(
					HBaseUtils.getResultByColumn(
							command.getTableName(), columnBean.getRowKey(), columnBean.getFamilyName(),
							columnBean.getColumnName(),new TimeRange(command.getBeginTime(), command.getEndTime())), command.getColumnNames(),command.isNeedRowKey());
			return buildMapResult(map);
		}
		return buildFaildResult("columnBean is null or empty,please check it and then try again");
	}

}
