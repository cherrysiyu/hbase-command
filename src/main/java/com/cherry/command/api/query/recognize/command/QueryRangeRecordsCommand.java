package com.cherry.command.api.query.recognize.command;

import com.cherry.command.api.query.recognize.BaseQuery;

public class QueryRangeRecordsCommand extends BaseQuery{
	
	private String startRowKey;
	private String endRowKey;
	@SuppressWarnings("unused")
	private QueryRangeRecordsCommand(){
		
	}
	public QueryRangeRecordsCommand(String tableName, String startRowKey,
			String endRowKey) {
		super(tableName);
		this.startRowKey = startRowKey;
		this.endRowKey = endRowKey;
	}

	@Override
	public int getCommandId() {
		return 9;
	}

	public String getStartRowKey() {
		return startRowKey;
	}

	public String getEndRowKey() {
		return endRowKey;
	}

}
