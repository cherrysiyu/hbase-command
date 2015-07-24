package com.cherry.command.api.query.action.process;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.DeleteTableCommand;
import com.cherry.command.api.query.result.CommandResult;

public class DeleteTableProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)throws Exception {
		
		DeleteTableCommand command = (DeleteTableCommand)cmd;
		return buildStringResult(String.valueOf(HBaseUtils.deleteTable(command.getTableName())));
	}

}
