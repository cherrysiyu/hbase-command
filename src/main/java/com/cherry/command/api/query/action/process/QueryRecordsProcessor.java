package com.cherry.command.api.query.action.process;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.TimeRange;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.QueryRecordsCommand;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.CommonMethod;

public class QueryRecordsProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)
			throws Exception {
		QueryRecordsCommand command = (QueryRecordsCommand)cmd;
		List<String> rowKeys = command.getRowKeys();
		List<Result> results = null;
		TimeRange timeRange = new TimeRange(command.getBeginTime(), command.getEndTime());
		if(CommonMethod.isCollectionNotEmpty(rowKeys)){
			 results = HBaseUtils.getResults(command.getTableName(), command.getRowKeys(),command.getMaxSize(),timeRange);
		}else{
			 results = HBaseUtils.getAllResult(command.getTableName(),command.getMaxSize(),timeRange);
		}
		
		return buildListResult(HBaseUtils.convertResults2List(results, command.getColumnNames(),command.isNeedRowKey()));
	}

}
