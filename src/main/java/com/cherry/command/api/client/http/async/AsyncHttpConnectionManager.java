package com.cherry.command.api.client.http.async;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParser;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
import org.apache.http.impl.nio.conn.ManagedNHttpClientConnectionFactory;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.nio.NHttpMessageParser;
import org.apache.http.nio.NHttpMessageParserFactory;
import org.apache.http.nio.NHttpMessageWriterFactory;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.conn.NHttpConnectionFactory;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.util.CharArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.command.api.utils.CommonMethod;

public class AsyncHttpConnectionManager {
	private static Logger logger = LoggerFactory.getLogger(AsyncHttpConnectionManager.class);
	private static final int cpuSize = Runtime.getRuntime().availableProcessors();
	private static CloseableHttpAsyncClient asyncHttpClient = null;
	private AsyncHttpConnectionManager(){}
	static{
		 asyncHttpClient = getAsyncHttpClient();
	}
	public static List<HttpResponse> getAsyncHttpResults(List<String> urls,boolean isPost){
		List<HttpRequestBase> list = new ArrayList<HttpRequestBase>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			for (String uri : urls) {
				list.add(buildHttpRequest(uri,isPost));	
			}
		}
		return getAsyncHttpResults(list);
	}
	
	public static List<HttpResponse> getAsyncHttpResults(List<String> urls,boolean isPost,long timeOut,TimeUnit timeUnit){
		List<HttpRequestBase> list = new ArrayList<HttpRequestBase>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			for (String uri : urls) {
				list.add(buildHttpRequest(uri,isPost));	
			}
		}
		return getAsyncHttpResults(list,timeOut,timeUnit);
	}
	
	public static List<HttpResponse> getAsyncHttpResults(List<HttpRequestBase> urls){
		return getAsyncHttpResults(urls, 0, null,true);
	}
	public static List<HttpResponse> getAsyncHttpResults(List<HttpRequestBase> urls,long timeOut,TimeUnit timeUnit){
		return getAsyncHttpResults(urls, timeOut, timeUnit,false);
	}
	
	public static HttpResponse getAsyncHttpResult(String url,boolean isPost){
		if(StringUtils.isNotBlank(url)){
			return getAsyncHttpResult(buildHttpRequest(url,isPost));
		}
		return null;
	}
	
	public static HttpResponse getAsyncHttpResult(String url,boolean isPost,long timeOut,TimeUnit timeUnit){
		if(StringUtils.isNotBlank(url)){
			return getAsyncHttpResult(buildHttpRequest(url,isPost),timeOut,timeUnit);
		}
		return null;
		
	}
	
	public static HttpResponse getAsyncHttpResult(HttpRequestBase url,long timeOut,TimeUnit timeUnit){
		return getAsyncHttpResult(url, timeOut, timeUnit, false);
	}
	
	public static HttpResponse getAsyncHttpResult(HttpRequestBase url){
		return getAsyncHttpResult(url, 0, null, true);
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 使用完毕注意手动关闭
	 * @return
	 */
	public static CloseableHttpAsyncClient getAsyncHttpClient(){

        // Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        NHttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {
            @Override
            public NHttpMessageParser<HttpResponse> create(final SessionInputBuffer buffer,
                    final MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {
                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }
                };
                return new DefaultHttpResponseParser(buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints);
            }

        };
        NHttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory = new ManagedNHttpClientConnectionFactory(requestWriterFactory, responseParserFactory, HeapByteBufferAllocator.INSTANCE);

        // Client HTTP connection objects when fully initialized can be bound to
        // an arbitrary network socket. The process of network socket initialization,
        // its connection to a remote address and binding to a local one is controlled
        // by a connection socket factory.

        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        // Use custom hostname verifier to customize SSL hostname verification.
        X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();

        // Create a registry of custom connection session strategies for supported
        // protocol schemes.
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
            .register("http", NoopIOSessionStrategy.INSTANCE)
            .register("https", new SSLIOSessionStrategy(sslcontext, hostnameVerifier))
            .build();

        // Use custom DNS resolver to override the system DNS resolution.
        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("localhost")) {
                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
                } else {
                    return super.resolve(host);
                }
            }

        };

        // Create I/O reactor configuration
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()*8)
                .setConnectTimeout(30000).setSoTimeout(60000) .build();

        // Create a custom I/O reactort
        ConnectingIOReactor ioReactor=null;
		try {
			ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
		} catch (IOReactorException e) {
			logger.error("",e);
		}

        // Create a connection manager with custom configuration.
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor, connFactory, sessionStrategyRegistry, dnsResolver);

        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setConnectionConfig(new HttpHost("localhost", 80), ConnectionConfig.DEFAULT);

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        if(cpuSize > 16)
        	connManager.setMaxTotal(cpuSize*2);//并发数
        else
        	connManager.setMaxTotal(32);
        connManager.setDefaultMaxPerRoute(10);
        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("localhost", 80)), 20);

        // Use custom cookie store if necessary.
        CookieStore cookieStore = new BasicCookieStore();
        // Use custom credentials provider if necessary.
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.BEST_MATCH).setExpectContinueEnabled(true) .setStaleConnectionCheckEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .build();

        // Create an HttpClient with the given custom dependencies and configuration.
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
            .setConnectionManager(connManager).setDefaultCookieStore(cookieStore)
            .setDefaultCredentialsProvider(credentialsProvider)
            .setDefaultRequestConfig(defaultRequestConfig).build();
        httpclient.start();
        return httpclient;
	}
	
	private static HttpResponse getAsyncHttpResult(HttpRequestBase url,long timeOut,TimeUnit timeUnit,boolean isWaitForFinish) {
		
		HttpResponse result = null;
		if (url != null) {
			try {
				Future<HttpResponse> future = asyncHttpClient.execute(url, null);
				HttpResponse httpResponse = null;
        		if(isWaitForFinish)// and wait until response is received
        			httpResponse = future.get();
        		else
        			httpResponse = future.get(timeOut,timeUnit);
        		if(httpResponse != null){
        			if(httpResponse.getStatusLine().getStatusCode() >= HttpStatus.SC_OK && httpResponse.getStatusLine().getStatusCode() < HttpStatus.SC_MULTIPLE_CHOICES)
        				result = httpResponse;
					else
						logger.error("Unexpected response status: " + httpResponse.getStatusLine().getStatusCode()+",url:"+httpResponse.toString()+"，原始url:"+url.toString());
        		}else{
        			logger.error("response timeout,url: "+url.getURI().toString());
        		}
        		
			}catch(Exception e){
				logger.error("getAsyncHttpResult error,url: "+url.getURI().toString(),e);
			}
		}

		return result;
	}

	private static List<HttpResponse> getAsyncHttpResults(List<HttpRequestBase> urls,long timeOut,TimeUnit timeUnit,boolean isWaitForFinish){
		List<HttpResponse> list = new ArrayList<HttpResponse>();
		if(CommonMethod.isCollectionNotEmpty(urls)){
			final CountDownLatch latch = new CountDownLatch(urls.size());
			List<Future<HttpResponse>> futures = new ArrayList<Future<HttpResponse>>();
			for (HttpRequestBase url : urls) {
				 Future<HttpResponse> future = asyncHttpClient.execute(url, new FutureCallback<HttpResponse>() {
	                    public void completed(final HttpResponse response) {
	                        latch.countDown();
	                    }
	                    public void failed(final Exception ex) {
	                        latch.countDown();
	                    }
	                    public void cancelled() {
	                        latch.countDown();
	                    }
	                });
				 futures.add(future);
	            }
	            try {
					latch.await();
				} catch (InterruptedException e) {
					logger.error("",e);
				}
	            
	            for (int i = 0; i < futures.size(); i++) {
	            	try {
	            		HttpResponse httpResponse = null;
	            		if(isWaitForFinish)
	            			 httpResponse = futures.get(i).get();
	            		else
	            			 httpResponse = futures.get(i).get(timeOut,timeUnit);
	            		if(httpResponse != null){
	            			if(httpResponse.getStatusLine().getStatusCode() >= HttpStatus.SC_OK && httpResponse.getStatusLine().getStatusCode() < HttpStatus.SC_MULTIPLE_CHOICES)
		        				list.add(httpResponse);
							else
							 logger.error("Unexpected response status: " + httpResponse.getStatusLine().getStatusCode()+",url:"+httpResponse.toString()+"，原始url:"+urls.get(i).toString());
	            		}else{
	            			logger.error("response timeout,url: "+urls.get(i).getURI().toString());
	            		}
						
					} catch (Exception e) {
		     			//如果出错，不要影响其他的任务，取消出错的这个任务
		     			futures.get(i).cancel(true);
						logger.error("",e);
					} 
				}
		
		}
		return list;
	}
	
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
