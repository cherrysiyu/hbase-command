package com.cherry.command.api.query.recognize.command;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.dto.ColumnBean;

public class UpdateColumnCommand extends AbstractHbaseCommand{
	private ColumnBean columnBean;

	@SuppressWarnings("unused")
	private UpdateColumnCommand(){
		
	}
	public UpdateColumnCommand(String tableName, ColumnBean columnBean) {
		super(tableName);
		if(columnBean == null)
			throw new IllegalArgumentException("columnBean is null,please check it and then try again");
		this.columnBean = columnBean;
	}

	@Override
	public int getCommandId() {
		return 6;
	}

	public ColumnBean getColumnBean() {
		return columnBean;
	}
	
}
