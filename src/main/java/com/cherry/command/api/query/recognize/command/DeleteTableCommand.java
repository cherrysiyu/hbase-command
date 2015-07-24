package com.cherry.command.api.query.recognize.command;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;

public class DeleteTableCommand extends AbstractHbaseCommand{
	
	@SuppressWarnings("unused")
	private DeleteTableCommand(){
		
	}
	public DeleteTableCommand(String tableName) {
		super(tableName);
	}

	@Override
	public int getCommandId() {
		return 2;
	}

}
