package com.surfnew.classify.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cherry.command.api.client.http.HttpCommon;
import com.cherry.command.api.client.http.sync.HttpConnectionManager;
import com.cherry.command.api.query.recognize.command.QueryRecordsCommand;
import com.cherry.command.api.utils.CommonMethod;
import com.cherry.command.api.utils.JacksonUtils;

public class ClientTest {
	private static String tableName="user_act_access";
	private static String  uriPrifix = "http://192.168.10.99:8080/service/query?serviceType=1&requestInfo=";
	private static JacksonUtils instance = JacksonUtils.getInstance();
	
	public static void main(String[] args) throws Exception {
		QueryRecordsCommand cmd = new QueryRecordsCommand(tableName);
		String url = uriPrifix+instance.beanToJson(cmd);
		System.out.println(url);		
		byte[] bytes = HttpConnectionManager.getByteArrayResult(url, true);
		System.out.println(HttpCommon.getResponseString(bytes));
		
		String stringResult = HttpConnectionManager.getStringResult(url, true);
		System.out.println(stringResult);
		List<String> urls = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			urls.add(url);
		}
		List<String> results = HttpConnectionManager.getHttpStringResults(urls, true);
		System.out.println("JVM预热结束。。。。"+results.size());
		System.out.println("=========HttpConnectionManager.getHttpStringResults=== start======");
		
		results = HttpConnectionManager.getHttpStringResults(urls, true);
		print(results);
		System.out.println("=========HttpConnectionManager.getHttpStringResults=== end====resultSize=="+results.size());
	
		System.out.println("=========HttpConnectionManager.getHttpStringResults=== start======");
		
		results = HttpConnectionManager.getHttpStringResults(urls, true, 3, TimeUnit.SECONDS);
		print(results);
		System.out.println("=========HttpConnectionManager.getHttpStringResults=== end====resultSize=="+results.size());
		
		List<String> urlList = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			urlList.addAll(urls);
		}
		System.out.println("HttpConnectionManager准备开始访问");
		int total = 0;
		long beginTime = System.currentTimeMillis();
		for (int i = 0; i < 3; i++) {
			long beginTime1 = System.currentTimeMillis();
			List<String> httpStringResults = HttpConnectionManager.getHttpStringResults(urlList, true);
			int size = httpStringResults.size();
			System.out.println("本次耗时:"+(System.currentTimeMillis()-beginTime1)+"ms,成功个数:"+size);
			total += size;
		}
		System.out.println("共耗时:"+(System.currentTimeMillis()-beginTime)+"ms,成功个数:"+total);
		
		/*TimeUnit.SECONDS.sleep(30);
		System.out.println("HttpConnectionManager准备开始访问");
		 total = 0;
		 beginTime = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			long beginTime1 = System.currentTimeMillis();
			List<HttpResponse> httpStringResults = AsyncHttpConnectionManager.getAsyncHttpResults(urlList, true);
			for (HttpResponse httpResponse : httpStringResults) {
				HttpCommon.getResponseString(httpResponse.getEntity());
			}
			int size = httpStringResults.size();
			System.out.println("本次耗时:"+(System.currentTimeMillis()-beginTime1)+"ms,成功个数:"+size);
			total += size;
		}
		System.out.println("共耗时:"+(System.currentTimeMillis()-beginTime)+"ms,成功个数:"+total);
		*/
		
	
	}

	private static void print(List<String> results){
		if(CommonMethod.isCollectionNotEmpty(results)){
			for (String string : results) {
				System.out.println(string);
			}
			
		}
	}
	
}
