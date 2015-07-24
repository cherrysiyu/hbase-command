package com.cherry.command.api.query.action.process;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.io.TimeRange;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.QueryRangeRecordsCommand;
import com.cherry.command.api.query.result.CommandResult;

public class QueryRangeRecordsProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)throws Exception {
		QueryRangeRecordsCommand command = (QueryRangeRecordsCommand)cmd;
		List<Map<String, String>> list = HBaseUtils.convertResults2List(
						HBaseUtils.getRangeResult(command.getTableName(), command.getStartRowKey(), command.getEndRowKey(),command.getMaxSize(),new TimeRange(command.getBeginTime(), command.getEndTime())), 
							command.getColumnNames(),command.isNeedRowKey());
		return buildListResult(list);
	}

}
