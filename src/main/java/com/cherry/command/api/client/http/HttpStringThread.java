package com.cherry.command.api.client.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

/**
 * HttpClient获取String返回值线程
 * @author Cherry
 * @version 0.1
 * @Desc
 * 2014年10月25日 下午4:05:44
 */
public class HttpStringThread implements Callable<String>{
	 private final CloseableHttpClient httpClient;
     private final HttpRequestBase httpRequestBase;
     private static final Charset charset = Charset.forName("UTF-8");
     
	public HttpStringThread(CloseableHttpClient httpClient,
			HttpRequestBase httpRequestBase) {
		super();
		this.httpClient = httpClient;
		this.httpRequestBase = httpRequestBase;
	}

	@Override
	public String call() throws Exception {
		return httpClient.execute(httpRequestBase, getHandler());
	}
	
	/**
	 * 获取ResponseHandler<String>对象
	 * @return
	 */
	private  ResponseHandler<String> getHandler(){
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity,charset) : "";
                } else {
                	LoggerFactory.getLogger(getClass()).error("Unexpected response status: " + status+",url:"+httpRequestBase.toString());
                    return null;
                }
            }

        };
        return responseHandler;
	}
	

}
