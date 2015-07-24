package com.surfnew.classify.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.cherry.command.api.hbase.HBaseUtils;

public class HbaseThreadTest {
	public static final String tableName = "t_user";
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);
		List<Future<List<Map<String, String>>>> futures = new ArrayList<Future<List<Map<String,String>>>>();
		for (int i = 0; i < 500; i++) {
			futures.add(executor.submit(new QueryThread(tableName)));
		}
		for (Future<List<Map<String, String>>> future : futures) {
			List<Map<String, String>> list = future.get();
			System.out.println(list);
		}
		executor.shutdown();
	}
}

class QueryThread implements Callable<List<Map<String, String>>> {
	private String tableName;
	public QueryThread(String tableName) {
		super();
		this.tableName = tableName;
	}

	@Override
	public List<Map<String, String>> call() throws Exception {
		return HBaseUtils.convertResults2List(HBaseUtils.getAllResult(tableName,0,null), null,true);
	}

}
