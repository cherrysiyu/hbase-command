package com.cherry.command.api.client.http;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

/**
 * HttpClient获取byte字节流的返回值线程
 * @author Cherry
 * @version 0.1
 * @Desc
 * 2014年10月25日 下午4:04:17
 */
public class HttpByteArrayThread implements Callable<byte[]>{
	 private final CloseableHttpClient httpClient;
     private final HttpRequestBase httpRequestBase;
     
	public HttpByteArrayThread(CloseableHttpClient httpClient,
			HttpRequestBase httpRequestBase) {
		super();
		this.httpClient = httpClient;
		this.httpRequestBase = httpRequestBase;
	}

	@Override
	public byte[] call() throws Exception {
		return httpClient.execute(httpRequestBase,getHandler());
	}
	
	
	/**
	 * 获取ResponseHandler对象
	 * @return
	 */
	private  ResponseHandler<byte[]> getHandler(){
		ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
            public byte[] handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                    return EntityUtils.toByteArray(response.getEntity());
                } else {
                	LoggerFactory.getLogger(getClass()).error("Unexpected response status: " + status+",url:"+httpRequestBase.toString());
                    return null;
                }
            }

        };
        return responseHandler;
	}

}
