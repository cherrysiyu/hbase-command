package com.surfnew.classify.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.cherry.command.api.client.http.sync.HttpConnectionManager;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.AddRecordsCommand;
import com.cherry.command.api.query.recognize.command.CreateTableCommand;
import com.cherry.command.api.query.recognize.command.DeleteAllColumnCommand;
import com.cherry.command.api.query.recognize.command.DeleteColumnCommand;
import com.cherry.command.api.query.recognize.command.DeleteTableCommand;
import com.cherry.command.api.query.recognize.command.QueryColumnRecordCommand;
import com.cherry.command.api.query.recognize.command.QueryRangeRecordsCommand;
import com.cherry.command.api.query.recognize.command.QueryRecordsCommand;
import com.cherry.command.api.query.recognize.command.TableExistCommand;
import com.cherry.command.api.query.recognize.command.UpdateColumnCommand;
import com.cherry.command.api.query.recognize.dto.ColumnBean;
import com.cherry.command.api.query.recognize.dto.HBaseRecordBean;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.query.result.ValueTypeEnum;
import com.cherry.command.api.utils.JacksonUtils;

public class ServiceTest {
	private static String tableName="t_info";
	private static String  uriPrifix = "http://127.0.0.1:8080/service/query?serviceType=1&requestInfo=";
	private static JacksonUtils instance = JacksonUtils.getInstance();
	AbstractHbaseCommand command = null;
	
	@Test
	public void isTableExist(){
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(new TableExistCommand(tableName)), true);
		System.out.println(stringResult);
	}

	
	@Test
	public void createTable(){
		List<String> family = new ArrayList<String>();
		family.add("basic");
		family.add("detail");
		command = new CreateTableCommand(tableName, family);
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	
	@Test
	public void  addRecords() throws InterruptedException{
		
		for (int j = 0; j < 100; j++) {
			List<HBaseRecordBean> records = new ArrayList<HBaseRecordBean>();
			Map<String,String> map = null;
			HBaseRecordBean hBaseRecordBean = null;
			for (int i = 0; i < 10; i++) {
				TimeUnit.MICROSECONDS.sleep(5);
				map = new HashMap<String, String>();
				map.put("username", "Cherry"+i);
				map.put("realname", "李波"+i);
				map.put("address", "江苏南京"+i);
				hBaseRecordBean = new HBaseRecordBean().addColumnData(map).addFamilyName("basic");
				String rowKey = hBaseRecordBean.getRowKey();
				records.add(hBaseRecordBean);
				
				map = new HashMap<String, String>();
				map.put("nickname", "Cherry"+i);
				map.put("email", "785028177@qq.com"+i);
				map.put("mobilephone", "454548fwe242"+i);
				records.add(new HBaseRecordBean().addRowKey(rowKey).addColumnData(map).addFamilyName("detail"));
			}
			command = new AddRecordsCommand(tableName, records);
			String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
			System.out.println(stringResult);
		}
		
	}
	
	@Test
	public void deleteColumn(){
		ColumnBean columnBean = new ColumnBean("9223370622605058631", "detail", "nickname");
		command = new DeleteColumnCommand(tableName, columnBean);
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	
	@Test
	public void  deleteAllColumn() {
		command = new DeleteAllColumnCommand(tableName, Arrays.asList("9223370622605058631"));
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	
	@Test
	public void  queryColumnRecord() {
		ColumnBean columnBean = new ColumnBean("9223370622605058575", "detail", "nickname","");
		command = new QueryColumnRecordCommand(tableName, columnBean);
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	
	@Test
	public void  queryRangeRecordsCommand() {
		QueryRangeRecordsCommand command = new QueryRangeRecordsCommand(tableName, "9223370622605058575","9223370622605058596");
		command.setNeedRowKey(true);//返回结果是否需要rowKey
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	
	//返回所有记录
	@SuppressWarnings("unchecked")
	@Test
	public void  queryRecordsCommand() throws IOException {
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		CommandResult result = instance.readJSON2Bean(CommandResult.class, stringResult);
		if(result.getStatusCode()==0){//成功
			int valueType = result.getValueType();
			ValueTypeEnum fromIntValue = ValueTypeEnum.fromIntValue(valueType);
			if(fromIntValue == ValueTypeEnum.STRINGDATA){
				String resultStr = result.getStrData();
				System.out.println("返回String的结果是:"+resultStr);
			}else if(fromIntValue == ValueTypeEnum.LISTDATA){
				List<Map<String,String>> listData = (List<Map<String,String>>)result.getListData();
				System.out.println("返回List的结果是:"+listData);
				
				
				for (Map<String, String> map : listData) {
					System.out.println(map);
				}
				
			}else if(fromIntValue == ValueTypeEnum.MAPDATA){
				Map<String,Object> mapData = (Map<String,Object>)result.getMapData();
				System.out.println("返回Map的结果是:"+mapData);
				
			}
			
		}else if(result.getStatusCode()==-1){//失败
			String msg = result.getMsg();
			System.out.println("错误信息是:"+msg);
		}
		System.out.println(stringResult);
	}
	//返回10条记录
	@Test
	public void  queryRecordsCommand1() {
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.setMaxSize(10);//返回10条记录
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	/**
	 * 返回定制字段
	 */
	@Test
	public void  queryRecordsCommand2() {
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.setMaxSize(10);
		command.setColumnNames(new HashSet<String>(Arrays.asList("username","address","nickname","email")));
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	
	@Test
	public void  queryRecordsCommand3() {
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.setMaxSize(10);
		command.setColumnNames(new HashSet<String>(Arrays.asList("username","address")));
		command.addRowKeys(Arrays.asList(new String[]{"9223370622605058575","9223370622605058596"}));
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
	}
	
	@Test
	public void  updateColumn() {
		ColumnBean columnBean = new ColumnBean("9223370622605058575", "basic", "username","李波");
		command = new UpdateColumnCommand(tableName, columnBean);
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
		
		command = new QueryColumnRecordCommand(tableName, columnBean);
		stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		System.out.println(stringResult);
		
	}
	
	@Test
	public void deleteTable(){
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(new DeleteTableCommand("t_user")), true);
		System.out.println(stringResult);
	}
	
	
	private String getFullUrl(AbstractHbaseCommand command){
		try {
			return uriPrifix+instance.beanToJson(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
