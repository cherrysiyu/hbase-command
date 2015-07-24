package com.cherry.command.api.query.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 值类型枚举
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月23日 下午2:22:07
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public enum ValueTypeEnum {
	/**
	 * list类型
	 */
	LISTDATA(1),
	/**
	 * string类型
	 */
	STRINGDATA(2),
	/**
	 * map类型
	 */
	MAPDATA(3);
	
	/**
	 * 值类型
	 */
	private int valueType;
	private static final Map<Integer,ValueTypeEnum> enumCache = new HashMap<Integer,ValueTypeEnum>();
	static{
		for (ValueTypeEnum valueTypeEnum : values()) {
			enumCache.put(valueTypeEnum.getValueType(),valueTypeEnum);
		}
	}

	 ValueTypeEnum(int valueType) {
		this.valueType = valueType;
	}

	public int getValueType() {
		return valueType;
	}
	
	/**
	 * int类型枚举转换成枚举值
	 * @param valueType
	 * @return
	 */
	public static ValueTypeEnum fromIntValue(int valueType){
		if(enumCache.containsKey(valueType)){
			return enumCache.get(valueType);
		}
		return ValueTypeEnum.LISTDATA;
	}
	
}
