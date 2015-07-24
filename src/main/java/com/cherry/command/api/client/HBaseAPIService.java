package com.cherry.command.api.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cherry.command.api.client.http.sync.HttpConnectionManager;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.QueryRecordsCommand;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.query.result.ValueTypeEnum;
import com.cherry.command.api.utils.JacksonUtils;

public class HBaseAPIService {
	/**
	 * 查询Api的HTTP url前缀
	 */
	private static String uriPrifix="http://192.168.10.99:8080/service/query?requestInfo=";
	private static JacksonUtils instance = JacksonUtils.getInstance();
	
	private HBaseAPIService(){
		
	}
	/**
	 * 获取单跳HBase表中的数据，如果rowKey为空则直接返回一个空的{@code Map<String,String>}
	 * @param tableName  HBase表名
	 * @param rowKey    HBase表中的主键rowKey集合
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> getSingleHbaseData(String tableName,String rowKey){
		if(StringUtils.isNotBlank(rowKey)){
			List<Map<String, String>> hbaseQueryDatas  = getHbaseQueryDatas(tableName, rowKey);
			if(hbaseQueryDatas != null && !hbaseQueryDatas.isEmpty())
				return hbaseQueryDatas.get(0);
			return new HashMap<String, String>();
		}
		return new HashMap<String, String>();
	}
	
	/**
	 * 获取Hbase表中相关的数据，如果rowKeys为空则返回所有表中的数据
	 * @param tableName HBase表名
	 * @param rowKeys HBase表中的主键rowKey集合
	 * @return {@code List<Map<String,String>>}
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String,String>> getHbaseQueryDatas(String tableName,String ... rowKeys){
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		if(rowKeys != null && rowKeys.length>0)
			command.addRowKeys(Arrays.asList(rowKeys));
		CommandResult result = executeHbaseCommand(command);
		if(result != null && result.getStatusCode()==0 && ValueTypeEnum.fromIntValue(result.getValueType()) == ValueTypeEnum.LISTDATA){//成功
			return (List<Map<String,String>>)result.getListData();
		}else{
			return new ArrayList<Map<String,String>>();
		}
	}
	@SuppressWarnings("unchecked")
	public static List<Map<String,String>> getHbaseQueryDatasWithRow(String tableName,String ... rowKeys){
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.setNeedRowKey(true);
		if(rowKeys != null && rowKeys.length>0)
			command.addRowKeys(Arrays.asList(rowKeys));
		CommandResult result = executeHbaseCommand(command);
		if(result != null && result.getStatusCode()==0 && ValueTypeEnum.fromIntValue(result.getValueType()) == ValueTypeEnum.LISTDATA){//成功
			return (List<Map<String,String>>)result.getListData();
		}else{
			return new ArrayList<Map<String,String>>();
		}
	}
	
	/**
	 * 获取Hbase表中相关的数据，如果rowKeys为空则返回所有表中的数据
	 * @param tableName HBase表名
	 * @param rowKeys HBase表中的主键rowKey集合
	 * @return CommandResult 对象
	 * @throws IOException
	 */
	public static CommandResult getHbaseDatas(String tableName,String ... rowKeys){
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		if(rowKeys != null && rowKeys.length>0)
			command.addRowKeys(Arrays.asList(rowKeys));
		return  executeHbaseCommand(command);
	}
	
	/**
	 * 执行Hbase查询条件删除等命令
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public static CommandResult executeHbaseCommand(AbstractHbaseCommand command){
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		try {
			return instance.readJSON2Bean(CommandResult.class, stringResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 拼接完整http URL
	 * @param command 
	 * @return
	 */
	private static String getFullUrl(AbstractHbaseCommand command){
		try {
			return uriPrifix+instance.beanToJson(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getUriPrifix() {
		return uriPrifix;
	}

	public static void setUriPrifix(String uriPrifix) {
		HBaseAPIService.uriPrifix = uriPrifix;
	}
	
	
}
