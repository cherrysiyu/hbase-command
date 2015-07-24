package com.cherry.command.api.query.recognize.command;

import java.util.List;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.dto.HBaseRecordBean;

public class AddRecordsCommand extends AbstractHbaseCommand{
	
	private List<HBaseRecordBean> records;
	@SuppressWarnings("unused")
	private AddRecordsCommand(){
		
	}
	
	public AddRecordsCommand(String tableName, List<HBaseRecordBean> records) {
		super(tableName);
		if(records == null || records.isEmpty()){
			throw new IllegalArgumentException("records is null or empty,please check it and then try again");
		}
		this.records = records;
	}

	@Override
	public int getCommandId() {
		return 7;
	}

	public List<HBaseRecordBean> getRecords() {
		return records;
	}
	
	
	
}
