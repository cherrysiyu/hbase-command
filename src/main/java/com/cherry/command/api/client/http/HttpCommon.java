package com.cherry.command.api.client.http;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient获取数据后相应的转换结果类
 * @author Cherry
 * @version 0.1
 * @Desc
 * 2014年10月25日 下午3:57:35
 */
public class HttpCommon {
	private static final Charset charset = Charset.forName("UTF-8");
	private HttpCommon(){}
	
	/**
	 * 获取Http返回的String结果
	 * @param httpEntity
	 * @return
	 * @throws IOException
	 */
	public static String getResponseString(HttpEntity httpEntity) throws IOException{
		return getResponseString(httpEntity, charset);
	}
	/**
	 * 制定字符集获取Http返回的String结果
	 * @param httpEntity
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getResponseString(HttpEntity httpEntity,Charset charset) throws IOException{
		return httpEntity == null?"":EntityUtils.toString(httpEntity,charset);
	}
	/**
	 * 默认采用UTF-8字符集转换成String
	 * @param bytes
	 * @return
	 */
	public static String getResponseString(byte[] bytes){
		return new String(bytes, charset);
	}
	/**
	 * 采用指定字符集转换成String
	 * @param bytes
	 * @param charset
	 * @return
	 */
	public static String getResponseString(byte[] bytes,Charset charset){
		return new String(bytes, charset);
	}
	/**
	 * 获取Http返回的byte字节流
	 * @param httpEntity
	 * @return
	 * @throws IOException
	 */
	public static byte[] getResponseByteArray(HttpEntity httpEntity) throws IOException{
		return httpEntity == null?new byte[]{}:EntityUtils.toByteArray(httpEntity);
	}
	
	

}
