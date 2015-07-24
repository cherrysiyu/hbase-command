package com.cherry.command.api.query.recognize.command;

import com.cherry.command.api.query.recognize.BaseQuery;
import com.cherry.command.api.query.recognize.dto.ColumnBean;

public class QueryColumnRecordCommand extends BaseQuery{

	private ColumnBean columnBean;
	
	@SuppressWarnings("unused")
	private QueryColumnRecordCommand(){
		addMaxSize(1);
	}
	
	public QueryColumnRecordCommand(String tableName,ColumnBean columnBean) {
		super(tableName);
		if(columnBean == null)
			throw new IllegalArgumentException("columnBean is null,please check it and then try again");
		addMaxSize(1);
		this.columnBean = columnBean;
	}

	public ColumnBean getColumnBean() {
		return columnBean;
	}


	@Override
	public int getCommandId() {
		return 10;
	}
	
	
}
