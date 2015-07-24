package com.cherry.command.api.client.http;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.LoggerFactory;

/**
 * 获取HttpResponse线程
 * @author Cherry
 * @version 0.1
 * @Desc
 * 2014年10月25日 下午4:06:20
 */
public class HttpThread implements Callable<HttpResponse>{
	 private final CloseableHttpClient httpClient;
     private final HttpRequestBase httpRequestBase;
     private final HttpContext context;
	public HttpThread(CloseableHttpClient httpClient,HttpRequestBase httpRequestBase) {
		super();
		this.httpClient = httpClient;
		this.context = new BasicHttpContext();
		this.httpRequestBase = httpRequestBase;
	}
	
	@Override
	public HttpResponse call() throws Exception {
		 BasicHttpResponse httpResponse = null;
         CloseableHttpResponse response = httpClient.execute(httpRequestBase, context);
         int statusCode = response.getStatusLine().getStatusCode();
         try {
        	 if (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
        		 httpResponse = new BasicHttpResponse(response.getStatusLine());
        		 httpResponse.setEntity(response.getEntity());
        		 httpResponse.setLocale(response.getLocale());
        		 httpResponse.setHeaders(response.getAllHeaders());
        		 httpResponse.setStatusLine(response.getProtocolVersion(), statusCode);
        	 }else {
        		 LoggerFactory.getLogger(getClass()).error("Unexpected response statusCode: " + statusCode+",url:"+httpRequestBase.toString());
             }
         } finally {
             response.close();
         }
		return httpResponse;
	}

}
