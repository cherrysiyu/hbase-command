package com.surfnew.classify.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.TimeRange;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.hbase.HBaseUtils;

public class HBaseUtisTest {
	private static final String tableName = "t_user";
	private static final String rowKey = "1";
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void isTableExist() throws IOException {
		boolean tableExist = HBaseUtils.isTableExist(tableName);
		logger.info("isTableExist===================" + tableExist);
	}

	@Test
	public void creatTable() throws IOException {
		int creatTable = HBaseUtils.creatTable(tableName, Arrays.asList(new String[] {
				"basic_info", "detail_info" }));
		logger.info("creatTable===================" + (creatTable == 0));
	}

	@Test
	public void addData() throws IOException {
		Map<String, String> basicMap = new HashMap<String, String>();
		basicMap.put("username", "Cherry");
		basicMap.put("realname", "李波");
		basicMap.put("sex", "男");
		basicMap.put("passowrd", "123456");

		Map<String, String> detailMap = new HashMap<String, String>();
		detailMap.put("nickname", "Cherry");
		detailMap.put("email", "cherry_siyu@126.com");
		detailMap.put("qq", "785028177");
		detailMap.put("mobilephone", "15261897347");
		detailMap.put("compney", "北京宽连十方南京分公司");

		boolean addData = HBaseUtils.addRecord(tableName, rowKey, "basic_info",
				basicMap);
		logger.info("addData=======basicMap ============" + addData);

		boolean addData2 = HBaseUtils.addRecord(tableName, rowKey, "detail_info",
				detailMap);
		logger.info("addData=======detailMap ============" + addData2);
	}

	@Test
	public void deleteAllColumn() {
		boolean deleteAllColumn = HBaseUtils.deleteAllColumn(tableName, Arrays.asList(rowKey));
		logger.info("deleteAllColumn===================" + deleteAllColumn);
	}

	@Test
	public void getSingleResult() throws Exception {
		Result singleResult = HBaseUtils.getSingleResult(tableName, rowKey,new TimeRange(0, System.currentTimeMillis()));
		printResult(singleResult);
	}

	private void printResult(Result result) {
		if (result != null) {
			Map<String, String> map = new HashMap<String, String>();
			for (KeyValue kv : result.list()) {//需要判断
				map.put(Bytes.toString(kv.getQualifier()), Bytes.toString(kv.getValue()));
			}
			logger.info(map.toString());
			NavigableMap<byte[], NavigableMap<byte[], byte[]>> map2 = result
					.getNoVersionMap();
			for (byte[] key : map2.keySet()) {
				Map<String, String> mm = new HashMap<String, String>();
				mm.put("family", Bytes.toString(key));
				NavigableMap<byte[], byte[]> navigableMap = map2.get(key);
				for (byte[] key2 : navigableMap.keySet()) {
					mm.put(Bytes.toString(key2),
							Bytes.toString(navigableMap.get(key2)));
				}
				logger.info(mm.toString());
			}
		}
	}

}
