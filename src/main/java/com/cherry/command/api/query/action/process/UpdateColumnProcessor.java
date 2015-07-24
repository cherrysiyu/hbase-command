package com.cherry.command.api.query.action.process;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.UpdateColumnCommand;
import com.cherry.command.api.query.recognize.dto.ColumnBean;
import com.cherry.command.api.query.result.CommandResult;

public class UpdateColumnProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)
			throws Exception {
		UpdateColumnCommand command = (UpdateColumnCommand)cmd;
		ColumnBean columnBean = command.getColumnBean();
		if(columnBean !=null){
			boolean updateTable = HBaseUtils.updateTable(command.getTableName(), columnBean.getRowKey(), columnBean.getFamilyName(), columnBean.getColumnName(), columnBean.getColumnValue());
			return buildStringResult(String.valueOf(updateTable));
		}
		return buildFaildResult("columnBean  is null ,please check it and then try again");
	}

}
