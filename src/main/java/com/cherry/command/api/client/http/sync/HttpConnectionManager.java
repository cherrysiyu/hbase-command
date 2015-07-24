package com.cherry.command.api.client.http.sync;

import java.net.URI;
import java.net.URL;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.client.http.HttpByteArrayThread;
import com.cherry.command.api.client.http.HttpStringThread;
import com.cherry.command.api.threadPool.ThreadExector;
import com.cherry.command.api.threadPool.ThreadPoolFactory;
import com.cherry.command.api.utils.CommonMethod;

/**
 * HttpClient简单封装
 * @author Cherry
 * @version 0.1
 * @Desc
 * 2014年10月25日 下午4:06:50
 */
public class HttpConnectionManager {
	private static Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);
	private static final int cpuSize = Runtime.getRuntime().availableProcessors();
	private static CloseableHttpClient httpClient = null;
	private HttpConnectionManager(){}
	static{
		httpClient = getHttpClient();
	}
	/**
	 * 查询一批Http请求返回结果
	 * @param urls
	 * @param isPost 是否是post方式
	 * @return 返回每个请求的字节流数组的集合
	 */
	public static List<byte[]> getHttpResults(List<String> urls,boolean isPost){
		List<HttpRequestBase> list = new ArrayList<HttpRequestBase>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			for (String uri : urls) {
				list.add(buildHttpRequest(uri,isPost));
			}
		}
		return getHttpResults(list);
	}
	public static List<byte[]> getHttpResults(List<String> urls,boolean isPost,long timeOut,TimeUnit timeUnit){
		List<HttpRequestBase> list = new ArrayList<HttpRequestBase>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			for (String uri : urls) {
				list.add(buildHttpRequest(uri,isPost));
			}
		}
		return getHttpResults(list,timeOut,timeUnit);
	}
	
	public static List<byte[]> getHttpResults(List<HttpRequestBase> urls){
		return getHttpResults(urls, 0, null,true);
	}
	public static List<byte[]> getHttpResults(List<HttpRequestBase> urls,long timeOut,TimeUnit timeUnit){
		return getHttpResults(urls, timeOut, timeUnit,false);
	}
	
	/**
	 * 查询一批Http请求返回结果
	 * @param urls
	 * @param isPost 是否是post方式
	 * @return
	 */
	public static List<String> getHttpStringResults(List<String> urls,boolean isPost){
		List<HttpRequestBase> list = new ArrayList<HttpRequestBase>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			for (String uri : urls) {
				list.add(buildHttpRequest(uri,isPost));
			}
		}
		return getHttpStringResults(list);
	}
	public static List<String> getHttpStringResults(List<String> urls,boolean isPost,long timeOut,TimeUnit timeUnit){
		List<HttpRequestBase> list = new ArrayList<HttpRequestBase>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			for (String uri : urls) {
				list.add(buildHttpRequest(uri,isPost));
			}
		}
		return getHttpStringResults(list,timeOut,timeUnit);
	}
	
	public static List<String> getHttpStringResults(List<HttpRequestBase> urls){
		return getHttpStringResults(urls, 0, null,true);
	}
	public static List<String> getHttpStringResults(List<HttpRequestBase> urls,long timeOut,TimeUnit timeUnit){
		return getHttpStringResults(urls, timeOut, timeUnit,false);
	}
	
	
	
	
	public static String getStringResult(String url,boolean isPost){
		if(StringUtils.isNotBlank(url)){
			return getStringResult(buildHttpRequest(url,isPost));
		}
		return null;
	}
	
	public static String getStringResult(String url,boolean isPost,long timeOut,TimeUnit timeUnit){
		if(StringUtils.isNotBlank(url)){
			return getStringResult(buildHttpRequest(url,isPost),timeOut,timeUnit);
		}
		return null;
		
	}
	
	
	
	public static String getStringResult(HttpRequestBase url,long timeOut,TimeUnit timeUnit){
		return getStringResult(url, timeOut, timeUnit, false);
	}
	
	public static String getStringResult(HttpRequestBase url){
		return getStringResult(url, 0, null, true);
	}
	
	
	
	
	
	
	public static byte[] getByteArrayResult(String url,boolean isPost){
		if(StringUtils.isNotBlank(url)){
			return getByteArrayResult(buildHttpRequest(url,isPost));
		}
		return null;
	}

	public static byte[] getByteArrayResult(String url,boolean isPost,long timeOut,TimeUnit timeUnit){
		if(StringUtils.isNotBlank(url)){
			return getByteArrayResult(buildHttpRequest(url,isPost),timeOut,timeUnit);
		}
		return null;
		
	}
	public static byte[] getByteArrayResult(HttpRequestBase url,long timeOut,TimeUnit timeUnit){
		return getByteArrayResult(url, timeOut, timeUnit, false);
	}
	public static byte[] getByteArrayResult(HttpRequestBase url){
		return getByteArrayResult(url, 0, null, true);
	}
	
	
	
	
	/**
	 * 使用完毕注意手动关闭
	 * @return
	 */
	public static CloseableHttpClient getHttpClient(){
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		 // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .build();
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);

        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
            .setMaxHeaderCount(200)
            .setMaxLineLength(2000)
            .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        
     // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        if(cpuSize > 16)
        	connManager.setMaxTotal(cpuSize*2);//并发数
        else
        	connManager.setMaxTotal(32);
        
        connManager.setDefaultMaxPerRoute(10);
        
        // Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.BEST_MATCH)
            .setExpectContinueEnabled(true)
            .setStaleConnectionCheckEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .setSocketTimeout(30000)
            .setConnectTimeout(30000)
            .setConnectionRequestTimeout(30000)
            .build();

        // Create an HttpClient with the given custom dependencies and configuration.
        return HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(defaultRequestConfig).build();
	}
	
	
	private static  List<byte[]> getHttpResults(List<HttpRequestBase> urls,long timeOut,TimeUnit timeUnit,boolean isWaitForFinish){
		List<byte[]> list = new ArrayList<byte[]>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			int poolSize = cpuSize+1;
			if(poolSize < urls.size()*2)
				poolSize = cpuSize*2;
			ThreadExector exector = ThreadPoolFactory.getCallableThreadExector(poolSize);
			List<Callable<byte[]>> taskThreads = new ArrayList<Callable<byte[]>>(urls.size());
			for (HttpRequestBase url : urls) {
				taskThreads.add(new HttpByteArrayThread(httpClient, url));
			}
			if(isWaitForFinish)
				list.addAll(exector.excuteTasksAndWaitForResult(taskThreads));
			else
				list.addAll(exector.excuteTasksAndWaitForResult(taskThreads,timeOut,timeUnit));
		}
		return list;
	}
	
	private static  List<String> getHttpStringResults(List<HttpRequestBase> urls,long timeOut,TimeUnit timeUnit,boolean isWaitForFinish){
		List<String> list = new ArrayList<String>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			int poolSize = cpuSize+1;
			if(poolSize < urls.size()*2)
				poolSize = cpuSize*2;
			ThreadExector exector = ThreadPoolFactory.getCallableThreadExector(poolSize);
			List<Callable<String>> taskThreads = new ArrayList<Callable<String>>(urls.size());
			for (HttpRequestBase url : urls) {
				taskThreads.add(new HttpStringThread(httpClient, url));
			}
			if(isWaitForFinish)
				list.addAll(exector.excuteTasksAndWaitForResult(taskThreads));
			else
				list.addAll(exector.excuteTasksAndWaitForResult(taskThreads,timeOut,timeUnit));
		}
		return list;
	}
	
	/**
	 * 获取Http访问结果返回的String字节流
	 * @param url
	 * @param timeout
	 * @param unit
	 * @param isWaitForFinished
	 * @return
	 */
	private static String getStringResult(HttpRequestBase url,long timeout,TimeUnit unit,boolean isWaitForFinished){
		String result = null;
		if(url != null){
			FutureTask<String> futureTask = new FutureTask<String>(new HttpStringThread(httpClient, url));
			futureTask.run();
			try {
				if(isWaitForFinished)
					result = futureTask.get();
				else
					result = futureTask.get(timeout,unit);
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		return result;
		
	}
	
	/**
	 * 获取Http访问结果返回的byte字节流
	 * @param url
	 * @param timeOut
	 * @param timeUnit
	 * @param isWaitForFinished 是否同步等待任务完成
	 * @return
	 */
	private static byte[] getByteArrayResult(HttpRequestBase url, long timeOut,TimeUnit timeUnit, boolean isWaitForFinished) {
		byte[] result = null;
		if(url != null){
			FutureTask<byte[]> futureTask = new FutureTask<byte[]>(new HttpByteArrayThread(httpClient, url));
			futureTask.run();
			try {
				if(isWaitForFinished)
					result = futureTask.get();
				else
					result = futureTask.get(timeOut,timeUnit);
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		return result;
	}
	
	/**
	 * 实例化得到HttpRequestBase对象
	 * @param url 访问地址
	 * @param isPost 是否采用post方式
	 * @return
	 */
	private static HttpRequestBase buildHttpRequest(String url,boolean isPost){
		URI uri = convertUrl(url);
		HttpRequestBase httpRequestBase = null;
		if(isPost){
			if(uri != null)
				httpRequestBase = new HttpPost(uri);
			else
				httpRequestBase = new HttpPost(url);
		}else{
			if(uri != null)
				httpRequestBase = new HttpGet(uri);
			else
				httpRequestBase = new HttpGet(url);
		}
		return httpRequestBase;
	}
	/**
	 * 转换url的值，因为如果String 类型的url有特殊字符或者其他例如json字符串时会客户端报错等问题
	 * @param url 访问地址
	 * @return
	 */
	private static URI convertUrl(String url) {
		URI uri = null;
		URL urlTemp = null;
		try{
			urlTemp = new URL(url);
			uri = new URI(urlTemp.getProtocol(), urlTemp.getUserInfo(), urlTemp.getHost(), urlTemp.getPort(), urlTemp.getPath(), urlTemp.getQuery(), null);
		}catch(Exception e){
			logger.error("",e);
		}
		return uri;
	}
}
