package com.cherry.command.api.query.action.process;

import java.util.List;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.AddRecordsCommand;
import com.cherry.command.api.query.recognize.dto.HBaseRecordBean;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.CommonMethod;

public class AddRecordsProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)throws Exception {
		AddRecordsCommand addRecordsCommand = (AddRecordsCommand)cmd;
		List<HBaseRecordBean> records = addRecordsCommand.getRecords();
		if(CommonMethod.isCollectionNotEmpty(records)){
			boolean isSuccess = HBaseUtils.batchAddRecords(addRecordsCommand.getTableName(), records);
			return buildStringResult(String.valueOf(isSuccess));
		}else{
			return buildFaildResult("records is null or empty,please check it and then try again");
		}
	}
}
