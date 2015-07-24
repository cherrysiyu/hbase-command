package com.cherry.command.api.query.action.process;

import java.util.List;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.CreateTableCommand;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.utils.CommonMethod;

public class CreateTableProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)throws Exception {
		CreateTableCommand command = (CreateTableCommand)cmd;
		List<String> family = command.getFamily();
		if(CommonMethod.isCollectionNotEmpty(family)){
			int result = HBaseUtils.creatTable(command.getTableName(), family);
			return buildStringResult(String.valueOf(result));
		}
		return buildFaildResult("family is null or empty,please check it and then try again");
	}

}
