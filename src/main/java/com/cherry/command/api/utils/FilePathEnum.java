package com.cherry.command.api.utils;

/**
 * 
 此枚举主要用来列出文件的位置，
 *  默认路径;
 *  相对路径,以%classpath%/为头. 如: "com/frame/properties" ;
 *  path为绝对路径,如:"e:/file"
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月21日 下午5:21:41
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public enum FilePathEnum {
	/**
	 * 默认根目录路径
	 */
	DEFAULT_PATH(0),
	/**
	 * 相对路径
	 */
	RELATIVE_PATH(1),
	/**
	 * 绝对路径
	 */
	ABSOLUTE_PATH(2);
	
	private final int intValue;
	
	FilePathEnum(int intValue){
		this.intValue = intValue;
	}

	public int getIntValue() {
		return intValue;
	}
	
}
