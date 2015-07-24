package com.cherry.command.api.query.action.process;

import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.query.action.AbstractActionProcessor;
import com.cherry.command.api.query.action.CommandAction;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.DeleteColumnCommand;
import com.cherry.command.api.query.recognize.dto.ColumnBean;
import com.cherry.command.api.query.result.CommandResult;

public class DeleteColumnProcessor extends AbstractActionProcessor implements CommandAction{

	@Override
	public CommandResult processCommand(AbstractHbaseCommand cmd)throws Exception {
		DeleteColumnCommand command  = (DeleteColumnCommand)cmd;
		ColumnBean columnBean = command.getColumnBean();
		if(columnBean != null){
			boolean deleteColumn = HBaseUtils.deleteColumn(command.getTableName(), columnBean.getRowKey(), columnBean.getFamilyName(), columnBean.getColumnName());
			return buildStringResult(String.valueOf(deleteColumn));
		}
		return buildFaildResult("columnBean is null or empty,please check it and then try again");
	}

}
