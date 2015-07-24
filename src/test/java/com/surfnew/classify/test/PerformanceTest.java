package com.surfnew.classify.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.client.http.sync.HttpConnectionManager;
import com.cherry.command.api.http.QueryConstans;
import com.cherry.command.api.query.recognize.AbstractHbaseCommand;
import com.cherry.command.api.query.recognize.command.QueryRecordsCommand;
import com.cherry.command.api.query.result.CommandResult;
import com.cherry.command.api.query.result.ValueTypeEnum;
import com.cherry.command.api.utils.JacksonUtils;

public class PerformanceTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static String tableName="t_info";
	private static String  uriPrifix = "http://127.0.0.1:8080/service/query?serviceType=2&requestInfo=";
	private static JacksonUtils instance = JacksonUtils.getInstance();
	private int threadNum =1;
	private int loopNum = 1;
	private StringBuilder sb = new StringBuilder();
	
	private List<String> rowKeys=Arrays.asList("","");
	
	@Test
	public void test1() throws IOException{
		threadNum =1;
		loopNum = 10;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	@Test
	public void test2() throws IOException{
		threadNum =1;
		loopNum = 100;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	@Test
	public void test3() throws IOException{
		threadNum =1;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	@Test
	public void test4() throws IOException{
		threadNum =1;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	
	
	@Test
	public void test5() throws IOException{
		threadNum =1;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	@Test
	public void test6() throws IOException{
		threadNum =2;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	@Test
	public void test7() throws IOException{
		threadNum =2;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	@Test
	public void test8() throws IOException{
		threadNum =2;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	@Test
	public void test9() throws IOException{
		threadNum =4;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	@Test
	public void test10() throws IOException{
		threadNum =4;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	@Test
	public void test11() throws IOException{
		threadNum =4;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	@Test
	public void test12() throws IOException{
		threadNum =8;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	@Test
	public void test13() throws IOException{
		threadNum =8;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	@Test
	public void test14() throws IOException{
		threadNum =8;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}

	@Test
	public void test15() throws IOException{
		threadNum =16;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	@Test
	public void test16() throws IOException{
		threadNum =16;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	@Test
	public void test18() throws IOException{
		threadNum =16;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		realTester(command);
	}
	
	
	
	@Test
	public void test19() throws IOException{
		threadNum =1;
		loopNum = 10;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	@Test
	public void test20() throws IOException{
		threadNum =1;
		loopNum = 100;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	@Test
	public void test21() throws IOException{
		threadNum =1;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	@Test
	public void test22() throws IOException{
		threadNum =1;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	
	
	@Test
	public void test23() throws IOException{
		threadNum =1;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	@Test
	public void test24() throws IOException{
		threadNum =2;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	@Test
	public void test25() throws IOException{
		threadNum =2;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	@Test
	public void test26() throws IOException{
		threadNum =2;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	@Test
	public void test27() throws IOException{
		threadNum =4;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	@Test
	public void test28() throws IOException{
		threadNum =4;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	@Test
	public void test29() throws IOException{
		threadNum =4;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	@Test
	public void test30() throws IOException{
		threadNum =8;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	@Test
	public void test31() throws IOException{
		threadNum =8;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	@Test
	public void test32() throws IOException{
		threadNum =8;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}

	@Test
	public void test33() throws IOException{
		threadNum =16;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	@Test
	public void test34() throws IOException{
		threadNum =16;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	@Test
	public void test35() throws IOException{
		threadNum =16;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKey(rowKeys.get(0));
		realTester(command);
	}
	
	
	
	@Test
	public void test36() throws IOException{
		threadNum =1;
		loopNum = 10;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	@Test
	public void test37() throws IOException{
		threadNum =1;
		loopNum = 100;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	@Test
	public void test38() throws IOException{
		threadNum =1;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	@Test
	public void test39() throws IOException{
		threadNum =1;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	
	
	@Test
	public void test40() throws IOException{
		threadNum =1;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	@Test
	public void test41() throws IOException{
		threadNum =2;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	@Test
	public void test42() throws IOException{
		threadNum =2;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	@Test
	public void test43() throws IOException{
		threadNum =2;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	@Test
	public void test44() throws IOException{
		threadNum =4;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	@Test
	public void test45() throws IOException{
		threadNum =4;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	@Test
	public void test46() throws IOException{
		threadNum =4;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	@Test
	public void test47() throws IOException{
		threadNum =8;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	@Test
	public void test48() throws IOException{
		threadNum =8;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	@Test
	public void test49() throws IOException{
		threadNum =8;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}

	@Test
	public void test50() throws IOException{
		threadNum =16;
		loopNum = 1000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	@Test
	public void test51() throws IOException{
		threadNum =16;
		loopNum = 10000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	@Test
	public void test52() throws IOException{
		threadNum =16;
		loopNum = 100000;
		QueryRecordsCommand command = new QueryRecordsCommand(tableName);
		command.addRowKeys(rowKeys);
		realTester(command);
	}
	
	
	
	
	
	
	
	
	
	
	

	private void realTester(AbstractHbaseCommand command) throws IOException {
		String stringResult = HttpConnectionManager.getStringResult(getFullUrl(command), true);
		sb.append("threadNum:").append(threadNum).append("  loopNum:").append(loopNum).append("\n");
		CommandResult result = instance.readJSON2Bean(CommandResult.class, stringResult);
		if(result.getStatusCode()==0){//成功
			int valueType = result.getValueType();
			ValueTypeEnum fromIntValue = ValueTypeEnum.fromIntValue(valueType);
			if(fromIntValue == ValueTypeEnum.STRINGDATA){
				sb.append(result.getStrData());
			}else if(fromIntValue == ValueTypeEnum.LISTDATA){
				sb.append(result.getListData().toString());
			}else if(fromIntValue == ValueTypeEnum.MAPDATA){
				sb.append(result.getMapData().toString());
			}
		}else{
			sb.append(result.getMsg());
		}
		sb.append("\r\n\r\n");
		logger.info(sb.toString());
	}
	
	private String getFullUrl(AbstractHbaseCommand command){
		try {
			return new StringBuilder().append(uriPrifix).append(instance.beanToJson(command))
					.append("&").append(QueryConstans.LOOPNUM_PARAM).append("=").append(loopNum)
					.append("&").append(QueryConstans.THREADNUM_PARAM).append("=").append(threadNum).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
