package com.cherry.command.api.query.action.process;

import java.util.List;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.DeleteAllColumnCommand;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.CommonMethod;

public class DeleteAllColumnProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd) throws Exception {
		DeleteAllColumnCommand command = (DeleteAllColumnCommand)cmd;
		List<String> rowKeys = command.getRowKeys();
		if(CommonMethod.isNotEmpty(rowKeys)){
			boolean deleteAllColumn = HBaseUtils.deleteAllColumn(command.getTableName(), rowKeys);
			return buildStringResult(String.valueOf(deleteAllColumn));
		}
		return buildFaildResult("rowKey is null or empty,please check it and then try again");
	}

}
