package com.cherry.command.api.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.io.TimeRange;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.command.api.query.recognize.dto.HBaseRecordBean;
import com.cherry.command.api.utils.CommonMethod;


/**
 * ，假设我们的HBase集群只有一个，那我们的HBase集群的conf配置文件也就只有一个（固定的一组属性），除非你有多个HBase集群另当别论。
在这样一个机制下，如果只有一个conf配置文件，则连接池中永远只会有一个connection实例！那“池”的意义就不大了！
所以，代码中才将基于getConnection获取池中物的机制Deprecated了，转而在官方文档中建议：
*******************************************************************************************************************
当面对多线程访问需求时，我们可以预先建立HConnection，参见以下代码：
Example 9.1. Pre-Creating a HConnection

// Create a connection to the cluster.
HConnection connection = HConnectionManager.createConnection(Configuration);
HTableInterface table = connection.getTable("myTable");
// use table as needed, the table returned is lightweight
table.close();
// use the connection for other access to the cluster
connection.close();
构建HTableInterface实现是非常轻量级的，并且资源是可控的。
*******************************************************************************************************************
如果大家按照官方文档的建议做了，也就是预先创建了一个连接，以后的访问都共享该连接，这样的效果其实和过去的getConnection完全一样，都是在玩一个connection实例！
 
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 下午3:38:55
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */



public class HBaseUtils {
	private static Logger logger = LoggerFactory.getLogger(HBaseUtils.class);
    private static Configuration conf = null;
    /**
     * HBaseAdmin instances are not expected to be long-lived.  For  example, an HBaseAdmin instance will not ride over a Master restart
      * 虽然此类的注释说 instance will not ride over a Master restart， 但是已经测试过，base宕机后重启HBase这个实例会自动连到ZooKeeper上，会再次连接，实例依然可以用
     */
    private static HBaseAdmin admin = null;
    
    private static GenericObjectPool<HConnection> objectPool = null;
    /**
     * HBase表是否存在
     * @param tableName
     * @return
     * @throws IOException
     */
    public static boolean isTableExist(String tableName)throws IOException{
    	return admin.tableExists(tableName);
    }
    
    /**
     * 
     * @param tableName
     * @param family
     * @return 创建成功返回0，表已经存在返回1
     * @throws Exception
     */
    public static int creatTable(String tableName, List<String> family)throws IOException{
         if (admin.tableExists(tableName)) {
            return 1;
         } else {
        	 HTableDescriptor desc = new HTableDescriptor(tableName);
        	 for (String string : family) {
        		 HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(string);
        		 hColumnDescriptor.setInMemory(true);
        		 //hColumnDescriptor.setTimeToLive(timeToLive)
        		 desc.addFamily(hColumnDescriptor);
			}
             admin.createTable(desc);
             logger.debug("create a new table ,tableName:"+tableName+",familys:"+family);
             return 0;
         }
    }
    

    /**
     * 删除指定的列
     * @tableName 表名
     * @rowKey rowKey
     * @familyName 列族名
     * @columnName 列名
     * @return 创建成功返回true，失败返回false
     * @throws IOException
     */
    public static boolean deleteColumn(String tableName, String rowKey, String falilyName, String columnName){
    	if(CommonMethod.isAnyEmpty(tableName,rowKey,falilyName,columnName)){
    		return false;
    	}
    	HConnection conn = null;
    	HTableInterface htable = null;
    	try{
        	 conn = objectPool.borrowObject();
        	 htable = conn.getTable(tableName);
        	 Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
             deleteColumn.deleteColumns(Bytes.toBytes(falilyName),Bytes.toBytes(columnName));
             htable.delete(deleteColumn);
             logger.debug("delete a table Column,tableName:"+tableName+",falilyName:"+falilyName + ",columnName:" + columnName + "  is deleted!");
            return true;
        }catch(Exception ex){
        	logger.error("delete table Column error,tableName:"+tableName+",falilyName:"+falilyName + ",columnName:" + columnName,ex);
        	return false;
        }finally{
        	closeHTableInterface(htable);
        	returnObject(conn);
        }
    }

   /**
    * 删除指定的列
    * @param tableName 表名
    * @param rowKey  rowKey
    * @throws IOException
    */
    public static boolean deleteAllColumn(String tableName, List<String> rowKeys){
    	if(!CommonMethod.isAllNotEmpty(tableName,rowKeys)){
    		return false;
    	}
    	 HConnection conn = null;
    	 HTableInterface htable = null;
    	try{
    		conn = objectPool.borrowObject();
        	htable = conn.getTable(tableName);
        	List<Delete> deletes = new ArrayList<Delete>(rowKeys.size());
        	for (String rowKey : rowKeys) {
        		deletes.add(new Delete(Bytes.toBytes(rowKey)));
			}
        	
            htable.delete(deletes);
            logger.debug("delete a table Column,tableName:"+tableName+",rowKeys:"+rowKeys + "  all columns are deleted!");
            return true;
        }catch(Exception ex){
        	logger.error("delete table Column error,tableName:"+tableName+",rowKeys:"+rowKeys,ex);
        	return false;
        }finally{
        	closeHTableInterface(htable);
        	returnObject(conn);
        }
    }

    /**
     * 删除表
     * @tableName 表名
     */
    public static boolean deleteTable(String tableName) throws IOException {
    	if(CommonMethod.isEmpty(tableName)){
    		return false;
    	}
		admin.disableTable(tableName);
        admin.deleteTable(tableName);
        return true;
    }
    
    
   /**
   		更新表中的某一列
     * @tableName 表名
     * @rowKey rowKey
     * @familyName 列族名
     * @columnName 列名
     * @value 更新后的值
    * @return
    */
    public static boolean updateTable(String tableName, String rowKey,String familyName, String columnName, String value){
    	if(CommonMethod.isAnyEmpty(tableName,rowKey,familyName,columnName)){
    		return false;
    	}
    	HConnection conn = null;
    	 HTableInterface htable = null;
    	try{
    		conn = objectPool.borrowObject();
        	 htable = conn.getTable(tableName);
        	 Put put = new Put(Bytes.toBytes(rowKey));
        	 put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName),Bytes.toBytes(value));
             htable.put(put);
             logger.debug("update a table column,tableName:"+tableName+",falilyName:"+familyName + ",columnName:" + columnName + "  success!");
             return true;
        }catch(Exception ex){
        	logger.error("delete table Column error,tableName:"+tableName+",rowKey:"+rowKey,ex);
        	 return false;
        }finally{
        	closeHTableInterface(htable);
        	returnObject(conn);
        }
    }
    /**
     * 批量添加数据
     * @param tableName
     * @param datas
     * @return
     * @throws IOException
     */
   public static boolean batchAddRecords(String tableName,List<HBaseRecordBean> datas) throws IOException{
		if (CommonMethod.isAnyEmpty(tableName, datas)) {
			return false;
		}
		HConnection conn = null;
		HTableInterface htable = null;
		try {
			conn = objectPool.borrowObject();
			htable = conn.getTable(tableName);
			List<Put> puts = new ArrayList<Put>();
			for (HBaseRecordBean hBaseRecordBean : datas) {
				Put put = new Put(Bytes.toBytes(hBaseRecordBean.getRowKey()));// 设置rowkey
				for (Entry<String, String> entry : hBaseRecordBean
						.getColumnData().entrySet()) {
					put.add(Bytes.toBytes(hBaseRecordBean.getFamilyName()),
							Bytes.toBytes(entry.getKey()),Bytes.toBytes(entry.getValue()));
				}
				puts.add(put);
			}
			htable.put(puts);
			logger.debug("batch add Datas to table tableName:" + tableName+ ",size:" + datas.size() + "  success!");
			return true;
		} catch (Exception ex) {
			logger.error("batch add Datas faild,table tableName:" + tableName+ ",size:" + datas.size(), ex);
			return false;
		} finally {
			closeHTableInterface(htable);
        	returnObject(conn);
		}
	   
   }
   /**
    * 
    * @param tableName
    * @param rowKey
    * @param familyName
    * @param datas key:columnName  value:columnValue
    * @throws IOException
    */
    public static boolean addRecord(String tableName,String rowKey, String familyName, Map<String,String> datas)
            throws IOException {
    	
    	 HConnection conn = null;
    	 HTableInterface htable = null;
    	try{
    		conn = objectPool.borrowObject();
        	 htable = conn.getTable(tableName);
        	 Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
        	 for (Entry<String, String> entry : datas.entrySet()) {
        		 put.add(Bytes.toBytes(familyName),
                         Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
			}
        	htable.put(put);
            logger.debug("add Data to table column,tableName:"+tableName+",falilyName:"+familyName + ",rowKey:" + rowKey +",datas:"+datas+ "  success!");
            return true;
        }catch(Exception ex){
        	logger.error("delete table Column error,tableName:"+tableName+",rowKey:"+rowKey,ex);
        	 return false;
        }finally{
        	closeHTableInterface(htable);
        	returnObject(conn);
        }
    }

    /**
     * 根据rwokey查询
     * @rowKey rowKey
     * @tableName 表名
     */
    public static Result getSingleResult(String tableName, String rowKey,TimeRange timeRange)throws Exception{
    	return getResultByColumn(tableName,rowKey,null,null,timeRange);
    }
    /**
     * 查询表中的某一列
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @throws IOException
     */
     public static Result getResultByColumn(String tableName, String rowKey,String familyName, String columnName,TimeRange timeRange) throws Exception {
        if(CommonMethod.isAnyEmpty(tableName,rowKey,familyName,columnName)){
        	return null;
        }
    	HConnection conn = null;
     	HTableInterface htable = null;
     	try{
     		conn = objectPool.borrowObject();
         	 htable = conn.getTable(tableName);
         	 Get get = new Get(Bytes.toBytes(rowKey));
         	 if(timeRange != null && timeRange.getMin() >0 && timeRange.getMin() <timeRange.getMax())
         		 get.setTimeRange(timeRange.getMin(), timeRange.getMax());
         	 if(CommonMethod.isAllNotEmpty(familyName,columnName)){
         		 get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
         	 }
         	 Result result = htable.get(get);
             logger.debug("query  result,tableName:"+tableName+",rowKey:"+rowKey);
             return result;
     	}finally{
     		closeHTableInterface(htable);
        	returnObject(conn);
         }
         
     }
     
     /**
      * 获取rowKeys列表的所有数据
      * @param tableName
      * @param rowKeys
      * @return
      * @throws IOException
      */
     public static List<Result> getResults(String tableName,List<String> rowKeys,long maxSize,TimeRange timeRange)throws Exception{
    	 List<Result> resultSets = new ArrayList<Result>();
    	if(!CommonMethod.isAllNotEmpty(tableName,rowKeys)){
    		return resultSets;
    	}
    	HConnection conn = null;
      	HTableInterface htable = null;
      	ResultScanner rs = null;
      	try{
      		 conn = objectPool.borrowObject();
          	 htable = conn.getTable(tableName);
          	Scan scan = new Scan();
          	if(timeRange != null && timeRange.getMin() >0 && timeRange.getMin() <timeRange.getMax())
          		scan.setTimeRange(timeRange.getMin(), timeRange.getMax());
          	FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ONE);
    		for(String rowKey:rowKeys){
    			filters.addFilter(new RowFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(rowKey))));
    		}
    		if(maxSize >0){
    			scan.setFilter( new FilterList(FilterList.Operator.MUST_PASS_ALL, new PageFilter(maxSize),filters));
    		}else
    			scan.setFilter(filters);
    		
          	 rs = htable.getScanner(scan);
			 if (rs != null) {
				for (Result result : rs) {
					resultSets.add(result);
				}
			 }
            logger.debug("query  result,tableName:"+tableName+",rowKeys:"+rowKeys);
      	}finally{
      		if(rs != null){
    			rs.close();
    			rs = null;
    		}
      		closeHTableInterface(htable);
        	returnObject(conn);
          }
      	return resultSets;
     }
     
     
   /**
    * 遍历查询hbase表
    * @param tableName 表名称
    * @return
    * @throws IOException
    */
    public static List<Result> getAllResult(String tableName,long maxSize,TimeRange timeRange) throws Exception {
    	List<Result> resultSets = new ArrayList<Result>();
    	HConnection conn = null;
    	HTableInterface htable = null;
    	ResultScanner rs = null;
    	try{
    		 Scan scan = new Scan();
    		 if(maxSize >0)
    			 scan.setFilter(new PageFilter(maxSize));
    		 if(timeRange != null && timeRange.getMin() >0 && timeRange.getMin() <timeRange.getMax())
    			 scan.setTimeRange(timeRange.getMin(), timeRange.getMax());
    		 conn = objectPool.borrowObject();
        	 htable = conn.getTable(tableName);
        	 rs = htable.getScanner(scan);
			 if (rs != null) {
				for (Result result : rs) {
					resultSets.add(result);
				}
			 }
            logger.debug("query all result,tableName:"+tableName+",dataSize:"+resultSets.size());
    	}finally{
    		if(rs != null){
    			rs.close();
    			rs = null;
    		}
    		closeHTableInterface(htable);
        	returnObject(conn);
        }
    	return resultSets;
    }

   /**
    *  遍历范围的数据结果集
    * @param tableName 表名称
    * @param startRowKey  开始的rowkey
    * @param endRowKey	  结束的rowkey	
    * @return
    * @throws IOException
    */
    public static List<Result> getRangeResult(String tableName, String startRowKey, String endRowKey,long maxSize,TimeRange timeRange) throws Exception {
    	List<Result> resultSets = new ArrayList<Result>();
    	if(CommonMethod.isEmpty(tableName)){
    		return resultSets;
    	}
    	HConnection conn = null;
    	HTableInterface htable = null;
    	ResultScanner rs = null;
    	try{
    		 Scan scan = new Scan();
    		 if(maxSize >0)
    			 scan.setFilter(new PageFilter(maxSize));
    		 if(timeRange != null && timeRange.getMin() >0 && timeRange.getMin() <timeRange.getMax())
    			 scan.setTimeRange(timeRange.getMin(), timeRange.getMax());
    		 if(CommonMethod.isNotEmpty(startRowKey))
    			 scan.setStartRow(Bytes.toBytes(startRowKey));
    		 if(CommonMethod.isNotEmpty(endRowKey))
    			 scan.setStopRow(Bytes.toBytes(endRowKey));
    		 conn = objectPool.borrowObject();
        	 htable = conn.getTable(tableName);
        	 rs = htable.getScanner(scan);
        	 if(rs != null){
	        	 for (Result result : rs) {
	        		 resultSets.add(result);
				}
        	 }
            logger.debug("query all result,tableName:"+tableName+",dataSize:"+resultSets.size());
    	}finally{
    		if(rs != null){
    			rs.close();
    			rs = null;
    		}
    		closeHTableInterface(htable);
        	returnObject(conn);
        }
    	return resultSets;
    }

    
    
    /**
     * 
     * @param resultsHBase返回结果集集合
     * @param columnNames 需要返回的字段集合
     * @return {@code List<Map<String,String>> } Map(key:columnName  value:columnValue) 
     */
    public static List<Map<String,String>> convertResults2List(List<Result> results,Set<String> columnNames,boolean isNeedRowKey){
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	if(CommonMethod.isCollectionNotEmpty(results)){
    		for (Result result : results) {
    			Map<String, String> map = convertResult2Map(result,columnNames,isNeedRowKey);
    			if(CommonMethod.isNotEmpty(map)){
    				list.add(map);
    			}
			}
    	}
    	return list;
    }
    /**
     * 
     * @param result HBase返回结果集
     * @param columnNames 需要返回的字段集合
     * @return {@code Map<String,String> }  key:columnName  value:columnValue
     */
    public static Map<String,String> convertResult2Map(Result result,Set<String> columnNames,boolean isNeedRowKey){
    	Map<String,String> map = new HashMap<String, String>();
    	if(result == null){
    		return map;
    	}
    	List<KeyValue> listKvs = result.list();
    	if(CommonMethod.isCollectionNotEmpty(listKvs)){
    		if(isNeedRowKey){
    			map.put("rowKey", Bytes.toString(result.getRow()));
    		}
    		for (KeyValue keyValue : listKvs) {
    			String qualifier = Bytes.toString(keyValue.getQualifier());
    			if(CommonMethod.isCollectionNotEmpty(columnNames)){
    				if(columnNames.contains(qualifier)){
    					map.put(qualifier, Bytes.toString(keyValue.getValue()));
    				}
    			}else{
    				map.put(qualifier, Bytes.toString(keyValue.getValue()));
    			}
			}
    	}
    	return map;
    }
    
    
    /**
     * 释放HConnection连接
     * @param conn
     */
    public static void closeHConnection(HConnection conn){
    	if(conn != null){
    		try {
				conn.close();
				conn = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    /**
     * 释放closeHTableInterface连接
     * @param htable
     */
    private static void closeHTableInterface(HTableInterface htable){
    	if(htable != null){
    		try {
    			htable.close();
    			htable = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    private static void returnObject(HConnection conn){
	    if (conn != null) {  
	        try {  
	        	objectPool.returnObject(conn);  
	        } catch (Exception ex) {  
	            logger.error("Cannot return connection from pool.", ex);  
	        }  
	    }  
    }

	public static void setConf(Configuration conf) {
		HBaseUtils.conf = conf;
	}

	public static void setAdmin(HBaseAdmin admin) {
		HBaseUtils.admin = admin;
	}

	public static HConnection getConnection() throws ZooKeeperConnectionException{
		return HConnectionManager.createConnection(conf);
	}

	public static void setObjectPool(GenericObjectPool<HConnection> objectPool) {
		HBaseUtils.objectPool = objectPool;
	}
	
    
}
