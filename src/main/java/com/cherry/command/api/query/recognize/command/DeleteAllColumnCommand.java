package com.cherry.command.api.query.recognize.command;

import java.util.List;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;

public class DeleteAllColumnCommand extends AbstractHbaseCommand{
	
	private List<String> rowKeys;
	@SuppressWarnings("unused")
	private DeleteAllColumnCommand(){
		
	}
	public DeleteAllColumnCommand(String tableName, List<String> rowKeys) {
		super(tableName);
		if(rowKeys == null || rowKeys.isEmpty()){
			throw new IllegalArgumentException("rowKeys is null or empty,please check it and then try again");
		}
		this.rowKeys = rowKeys;
	}

	@Override
	public int getCommandId() {
		return 4;
	}

	public List<String> getRowKeys() {
		return rowKeys;
	}
	
}
