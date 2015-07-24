package com.cherry.command.api.dto.query;

import com.cherry.command.api.query.action.CommandAction;

public class ProcessorBean {
	
	private int commandId;
	private Class<?> commandClass;
	private CommandAction processor;
	
	public ProcessorBean(Class<?> commandClass, CommandAction processor) {
		super();
		this.commandClass = commandClass;
		this.processor = processor;
	}

	public ProcessorBean(int commandId, Class<?> commandClass,
			CommandAction processor) {
		super();
		this.commandId = commandId;
		this.commandClass = commandClass;
		this.processor = processor;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public Class<?> getCommandClass() {
		return commandClass;
	}

	public void setCommandClass(Class<?> commandClass) {
		this.commandClass = commandClass;
	}

	public CommandAction getProcessor() {
		return processor;
	}

	public void setProcessor(CommandAction processor) {
		this.processor = processor;
	}
	
	

}
