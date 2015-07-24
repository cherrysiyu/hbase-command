package com.surfnew.classify.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.query.recognize.command.QueryRecordsCommand;
import com.cherry.command.api.utils.JacksonUtils;

public class JacksonTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String tableName = "user_feature_category";
	JacksonUtils instance = JacksonUtils.getInstance();
	@Test
	public void testJacksonUtils() throws Exception{
		String writerJavaObject2JSON = instance.writerJavaObject2JSON(new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
		logger.info(writerJavaObject2JSON);
		List<?> readJSON2Bean = instance.readJSON2Bean(List.class, writerJavaObject2JSON);
		logger.info(readJSON2Bean.toString());
		ArrayList<Integer> readJSON2Genric = instance.readJSON2Genric(writerJavaObject2JSON, new TypeReference<ArrayList<Integer>>() {});
		logger.info(readJSON2Genric.toString());
		
		Map<String,Object> map  = new HashMap<String, Object>();
		map.put("int", 1);
		map.put("boolean", false);
		map.put("String", "Cherry");
		map.put("char", 'a');
		map.put("array", new String[]{"sf","22"});
		String beanToJson = instance.beanToJson(map);
		logger.info(beanToJson);
		Map<String, Object> readJSON2Map = instance.readJSON2Map(beanToJson);
		logger.info(readJSON2Map.toString());
		Map<String, Object> readJSON2Genric2 = instance.readJSON2Genric(beanToJson, new TypeReference<Map<String, Object>>() {});
		logger.info(readJSON2Genric2.toString());
	}
	
	@Test
	public void testQuery() throws IOException{
		QueryRecordsCommand cmd = new QueryRecordsCommand(tableName);
		System.out.println(instance.beanToJson(cmd));
	}
	
	
}
