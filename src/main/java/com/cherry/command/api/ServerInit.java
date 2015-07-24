package com.cherry.command.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.hbase.HBaseConnectionPoolableObjectFactory;
import com.cherry.command.api.hbase.HBaseUtils;
import com.cherry.command.api.http.ProcessorFactory;
import com.cherry.command.api.http.process.AbstractProcessor;
import com.cherry.command.api.utils.CommonMethod;

/**
 * 
 	服务初始化参数等
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午10:18:17
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class ServerInit {
	
	private String log4jFilePath  ;
	private String hbaseConfPath ;
	
	private boolean lifo = false;  
	private int maxActive = 5;  
	private int maxIdle = 5;  
	private int minIdle = 1;  
	private int maxWait = 5 * 1000;
	
	
	public void init(){
		initLog4j();
		initHbase();
	}

	public void initLog4j(){
		if(CommonMethod.isNotEmpty(log4jFilePath)){
			File file = new File(log4jFilePath);
			if(file.exists()){
				Properties props = new Properties();
				try {
					props.load(new FileInputStream(file));
					BasicConfigurator.resetConfiguration();
					PropertyConfigurator.configure(props);
					LoggerFactory.getLogger(getClass()).info("加载log4j外部配置文件成功,路径:"+file.getAbsolutePath());
				} catch (IOException e) {
					LoggerFactory.getLogger(getClass()).error("===========加载系统外部log4j.properties文件失败",e);
				}
			}
		}
	}

	public void initHbase(){
		Configuration	conf = new Configuration();
		HBaseAdmin admin = null;
		try {
			if(CommonMethod.isNotEmpty(hbaseConfPath) && (new File(hbaseConfPath).exists())){
		    	conf.addResource(new FileInputStream(new File(hbaseConfPath)));
			}else{
				hbaseConfPath = "hbase-site.xml";
				conf.addResource(hbaseConfPath);
			}
    		conf =  HBaseConfiguration.create(conf);
    		HBaseUtils.setConf(conf);
			admin = new HBaseAdmin(conf);
			HBaseUtils.setAdmin(admin);
			PoolableObjectFactory<HConnection> factory = new HBaseConnectionPoolableObjectFactory();  
	        GenericObjectPool.Config config = new GenericObjectPool.Config();  
	        config.lifo = lifo;  
	        config.maxActive = maxActive;  
	        config.maxIdle = maxIdle;  
	        config.minIdle = minIdle;  
	        config.maxWait = maxWait;
	        GenericObjectPool<HConnection> objectPool = new GenericObjectPool<HConnection>(factory, config);
	        HBaseUtils.setObjectPool(objectPool);
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error("init HBase error,  System will exit",e);
			System.exit(1);
		}
		LoggerFactory.getLogger(getClass()).info("HBase init success !!!,path:"+hbaseConfPath);
	}
	
	public String getLog4jFilePath() {
		return log4jFilePath;
	}
	public void setLog4jFilePath(String log4jFilePath) {
		this.log4jFilePath = log4jFilePath;
	}
	public String getHbaseConfPath() {
		return hbaseConfPath;
	}
	public void setProcessMap(Map<String, AbstractProcessor> processMap) {
		ProcessorFactory.setProcessors(processMap);
	}
	public void setHbaseConfPath(String hbaseConfPath) {
		this.hbaseConfPath = hbaseConfPath;
	}

	public boolean isLifo() {
		return lifo;
	}

	public void setLifo(boolean lifo) {
		this.lifo = lifo;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}
	
	

}
