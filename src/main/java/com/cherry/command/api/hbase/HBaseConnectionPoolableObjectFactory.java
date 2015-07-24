package com.cherry.command.api.hbase;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.hadoop.hbase.client.HConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseConnectionPoolableObjectFactory  implements PoolableObjectFactory<HConnection>{
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static AtomicInteger count = new AtomicInteger();
	@Override
	public HConnection makeObject() throws Exception {
		logger.info("创建一个HConnection对象，对象池中对象个数: "+count.incrementAndGet());
		return HBaseUtils.getConnection();
	}

	@Override
	public void destroyObject(HConnection conn) throws Exception {
		logger.info("销毁一个HConnection对象，对象池中对象个数: "+count.decrementAndGet());
		HBaseUtils.closeHConnection(conn);
	}

	@Override
	public boolean validateObject(HConnection obj) {
		logger.debug("验证一个HConnection对象: "+obj);
		return obj.isClosed();
	}

	@Override
	public void activateObject(HConnection obj) throws Exception {
		logger.debug("激活一个HConnection对象: "+obj);
	}

	@Override
	public void passivateObject(HConnection obj) throws Exception {
		logger.debug("钝化一个HConnection对象: "+obj);
		
	}

	
	
	

}
