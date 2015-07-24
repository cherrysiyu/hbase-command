package com.cherry.command.api.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

/**
 * 
 
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月21日 下午5:05:02
 * <p>推荐使用JacksonUtils.getInstance().getObjectMapper()得到ObjectMapper对象操作，比较灵活(基于内存树形式,效率比较高)
 * 或者使用JacksonUtils.getInstance().getJsonGenerator(OutputStream out)得到JsonGenerator对象操作(基于流形式，效率极高，比内存树形式还高20%左右)
 * 
 * <p>
 * @author Cherry
 * @version 1.0.0
 */
public enum JacksonUtils {
	INSTANCE;
	
	private final static ObjectMapper objectMapper ;
	private  static JsonGenerator jsonGenerator;
	
	static {
		objectMapper = new ObjectMapper();
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
		//对于特殊符号入单引号的处理
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		//设置遇到时间格式Date的处理
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * 得到JacksonUtils实例
	 * @return
	 */
	public static JacksonUtils getInstance(){
		return INSTANCE;
	}
	
	/**
	 * 得到ObjectMapper对象,此方法是线程安全的
	 * 树模型 ：提供一个 JSON 文档可变内存树的表示形式。
	 * @return
	 */
	public  ObjectMapper getObjectMapper(){
		return objectMapper;
	}
	
	
	
	/**
	 * 得到JsonGenerator对象,用JsonGenerator对象操作JSON数据的转换效率极高
	 * 流 API： 性能最佳的方式 （最低开销、 速度最快的读/写； 其它二者基于它实现）。
	 * @param out OutputStream对象，如果out传过来的是null，则以字符串传递，可以是直接输出到控制台的也可以是文件形式的
	 * @return
	 */
	public  JsonGenerator getJsonGenerator(OutputStream out) throws IOException{
		JsonFactory jsonFactory = objectMapper.getJsonFactory();
		if(out != null){
			jsonGenerator = jsonFactory.createJsonGenerator(out);
		}else{
			jsonGenerator = jsonFactory.createJsonGenerator(new StringWriter());
		}
		return jsonGenerator;
	}
	
	
	/**
	 * 得到JsonParser对象
	 * @return
	 * @throws IOException 
	 * @throws JsonParseException 
	 */
	public JsonParser getJsonParser(InputStream in) throws JsonParseException, IOException{
		
		if(in == null){
			throw new IllegalArgumentException("传入的参数为null");
		}
		JsonFactory jsonFactory = new JsonFactory();
		return jsonFactory.createJsonParser(in);
	}
	
	/**
	 * 把java对象转换成JSON字符串
	 * @param object
	 * @return
	 */
	public String writerJavaObject2JSON(Object object)throws Exception{
		// 这里异常都未进行处理，而且流的关闭也不规范。开发中请勿这样写，如果发生异常流关闭不了
		StringWriter writer = new StringWriter();
		JsonGenerator gen =  new JsonFactory().createJsonGenerator(writer);
		objectMapper.writeValue(gen, object);
		gen.close();
		return writer.toString();
	}
	
	
	public  String beanToJson(Object obj)throws IOException{
		return objectMapper.writeValueAsString(obj);
	}
	
	/**
	 * 把Java对象转换成json字符串后写入到文件中
	 * @param object
	 * @param out
	 */
	public void writer2File(Object object,OutputStream out)throws IOException{
		if(object != null && out != null){
			objectMapper.writeValue(out, object);
		}else{
			throw new IllegalArgumentException("传入的参数不合法，有null值存在 object: "+  object + "  out:" + out);
		}
	}
	
	
	public <T> T readJSON2Bean(Class<T> beanClass,String jsonString)throws IOException{
		if(StringUtils.isEmpty(jsonString)){
			return null;
		}
		return (T) objectMapper.readValue(jsonString,beanClass);
	}
	
	/**
	 * 把jsonString字符串转换成Map&ltString,Object&gt;
	 * @param jsonString
	 * @return
	 */
	public Map<String,Object> readJSON2Map(String jsonString)throws IOException{
		if(StringUtils.isEmpty(jsonString)){
			return null;
		}
		return objectMapper.readValue(jsonString, new TypeReference<Map<String,Object>>(){});
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> readJSON2ListMap(String jsonString)throws IOException{
		if(StringUtils.isEmpty(jsonString)){
			return null;
		}
		return objectMapper.readValue(jsonString, List.class);
		
	}
	
	
	/**
	 * 如果需要转换成{@code List<Map<String,Object>>} 的形式 那么传入的typeReference是 {@code new TypeReference<List<Map<String,Object>>>(){}
	 *如果需要转换成{@code List<JavaBean> }的形式,那么传入的typeReference是{@code new TypeReference<List<JavaBean>>(){}}
	 *其它的依次类推
	 * @param <T>
	 * @param jsonString
	 * @param typeReference
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T readJSON2Genric(String jsonString,TypeReference<T> typeReference)throws IOException{
		if(StringUtils.isEmpty(jsonString)){
			return null;
		}
			return (T)objectMapper.readValue(jsonString,typeReference);
	}
	
	/**
	 * 从文件中读取数据转换成需要的对象
	 * @param <T>
	 * @param input 
	 * @param typeReference  如果需要转换成{@code List<Map<String,Object>>} 的形式 那么传入的typeReference是 new TypeReference<List<Map<String,Object>>>(){}
	 *						如果需要转换成{@code List<JavaBean>}的形式,那么传入的typeReference是new TypeReference<List<JavaBean>>(){}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T readFromFile(InputStream input,TypeReference<T> typeReference)throws IOException{
		if(input == null )
			throw new IllegalArgumentException("传入的input参数不合法，为null");
		return (T)objectMapper.readValue(input, typeReference);
	}
}
