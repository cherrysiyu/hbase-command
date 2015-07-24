package com.cherry.command.api.query.recognize.command;

import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.dto.ColumnBean;
/**
 * 
 
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 下午12:18:29
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class DeleteColumnCommand extends AbstractHbaseCommand{
	private ColumnBean columnBean;

	@SuppressWarnings("unused")
	private DeleteColumnCommand(){
		
	}
	public DeleteColumnCommand(String tableName, ColumnBean columnBean) {
		super(tableName);
		if(columnBean == null)
			throw new IllegalArgumentException("columnBean is null,please check it and then try again");
		this.columnBean = columnBean;
	}

	@Override
	public int getCommandId() {
		return 5;
	}

	public ColumnBean getColumnBean() {
		return columnBean;
	}
	
	
	
	
}
