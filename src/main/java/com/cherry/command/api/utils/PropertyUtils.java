package com.cherry.command.api.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * <br>TITLE       : 读/写属性文件的工具类.
 * <br>DESCRIPTION : 可以使用此工具的getProperty(*) / setProperty(**)方法读、写properties文件.
 * 	   <br>
 *     <br>使用步骤:
 *     <br>1.首先,获得PropertyUtil实例. 使用 静态法 newInstance(**) 获得
 *     <br>  如: PropertyUtil propUtil =
 *     PropertyUtil.newInstance("property_name"); // 其中参数"property_name" 为property的文件名.注意 不包含扩展名.
 *     <br>
 *     <br>2.使用PropertyUtil实例的(getProperty(String code)
 *     或 getProperty(String code, Object[] objs))方法取值即可. setProperty(String key, String value) 用来写.
 *     <br>  如: propUtil.get("name"); // 获得name对应的值
 *	   <br>
 *     <br><b>特点: 无论有多少properties文件只需这一个类足矣!
 *     <br>注意: properties的文件名字不可重复.</b>
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月21日 下午5:18:25
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public final class PropertyUtils {
	public static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);
	private PropertyUtils(){}

    private static Object clockObj = PropertyUtils.class;

    // 存放参数的property文件的默认位置
    private static final String PROPERTY_FILE_PATH = "/";

    // 存放每个properties文件对应的PropertyUtil
    private static ConcurrentMap<String,PropertyUtils> propertyUtilMap = new ConcurrentHashMap<String,PropertyUtils>();

    // 记录Timer是否已经启动. true :已经启动；false:未启动
   // private static volatile boolean timerIsStart = false;

    private String filePath = null;

    private Properties properties = null;

    // 记录文件修改时间
    private long modifyTime = 0;

    // 临时判断创建是否成功
    private static boolean success = false;

    /**
     * 实例化PropertyUtil对象.
     * @param propertyName 属性文件的名字. 注意不包含扩展名.且属性文件默认在 %classpath%下（即:工程的src下）.
     * @param path 文件路径,不包含文件名
     * @param bln
     *            FilePathEnum.DEFAULT_PATH:默认路径;
     *            FilePathEnum.RELATIVE_PATH:path为相对路径,以%classpath%/为头. 如:"com/frame/properties" ;
     *            FilePathEnum.ABSOLUTE_PATH:path为绝对路径,如: "e:/file"
     */
    private PropertyUtils(String propertyName, String path,FilePathEnum filePathEnum) {

        try {
            success = false;
            if (filePathEnum == FilePathEnum.DEFAULT_PATH) {
                filePath = this.getClassPath() + propertyName + ".properties";
            } else if (filePathEnum == FilePathEnum.RELATIVE_PATH) {
                filePath = this.getClassPath() + path + "/" + propertyName + ".properties";
            } else if (filePathEnum == FilePathEnum.ABSOLUTE_PATH) {
                filePath = path + "/" + propertyName + ".properties";
            } else {
            	logger.error("属性文件路径类型在规定范围之外! 规定范围: 0、1、2 ");
            }

            if (CommonMethod.isNotEmpty(filePath)) {
                File file = new File(filePath);
                if (file.exists()) {
                    InputStream instream = new FileInputStream(filePath);
                    this.properties = new Properties();

                    properties.load(instream);
                    instream.close();

                    success = true;
                } else {
                	logger.error("属性文件不存在! filePath = " + filePath);
                }
            } else {
            	logger.error("属性文件路径为空!");
            }
        } catch (IOException e) {
        	logger.error("init construct error",e);
        }
    }

    /**
     * 获得ClassPath的路径.
     * @return String 路径字符串
     */
    private String getClassPath() {

        URL url = this.getClass().getResource(PROPERTY_FILE_PATH);
        String path = null;
        try {
            path = URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (IOException e) {
        	logger.error("getClassPath error",e);
        }
        return path;
    }

    /**
     * 获得指定文件的PropertyUtil.
     * @param propertyName
     *            属性文件的名字. 注意不包含扩展名.
     *            且属性文件默认在 %classpath%/下（即:工程的src下）.
     * @return PropertyUtil
     */
    public static PropertyUtils newInstance(String propertyName) {

        return initPropertyUtil(propertyName, "",  FilePathEnum.DEFAULT_PATH);
    }

    /**
     * 获得指定文件的PropertyUtil. 可以指定文件所在路径
     *
     * @param propertyName
     *            属性文件的名字. 注意不包含扩展名.
     *            且属性文件默认在 %classpath%/下（即:工程的src下）.
     * @param path 文件路径,不包含文件名
     * @param bln
     *            FilePathEnum.DEFAULT_PATH:默认路径;
     *            FilePathEnum.RELATIVE_PATH:path为相对路径,以%classpath%/为头. 如:"com/frame/properties" ;
     *            FilePathEnum.ABSOLUTE_PATH:path为绝对路径,如: "e:/file"
     * @return PropertyUtil
     */
    public static PropertyUtils newInstance(String propertyName,String path,FilePathEnum filePathEnum) {

        return initPropertyUtil(propertyName, path, filePathEnum);
    }

    /**
     * 初始化.
     *
     * @param propertyName
     * @param path
     * @param bln
     * @return PropertyUtil
     */
    private static PropertyUtils initPropertyUtil(String propertyName,String path,FilePathEnum filePathEnum) {

        synchronized (clockObj) {
            PropertyUtils propertyUtil =  propertyUtilMap.get(propertyName);

            if (propertyUtil == null) {
                propertyUtil = new PropertyUtils(propertyName, path, filePathEnum);

                if (success) {
                    propertyUtilMap.put(propertyName, propertyUtil);
                }
            }
            //启动周期性任务
           /* if (!PropertyUtils.timerIsStart) {
            	//在创建后10s开始每300秒执行一次
            	ThreadPoolFactory.executTimerTask(new TimerTask(new CheckFileChange(propertyUtil), 10, 5*60, TimeUnit.SECONDS));
                //new Timer().schedule(new CheckFileChange(propertyUtil), 1000, 3000);
                timerIsStart = true;
            }*/

            return propertyUtil;
        }

    }

    /**
     * 获得key对应的value值.
     *
     * @param key 消息的key
     * @return String 消息的key所对应的value
     */
    public String getProperty(String key) {
        if (null != properties) {
            return properties.getProperty(key);
        } else {
            return null;
        }
    }

    /**
	 * 根据key得到值，如果不存在则返回给定的默认值
	 * @param key 指定的key
	 * @param defaultValue 不存在时返回的值
	 * @return
	 */
	public  String getProperty(String key, String defaultValue) {
		if(properties != null){
			String tmp = properties.getProperty(key);
			return CommonMethod.isNotEmpty(tmp) ? tmp : defaultValue;
		}else{
			return null;
		}
	}
    /**
     * 获得key对应的value值. 并具有格式化的功能
     * @param key 消息的key
     * @param objs 欲插入value中的参数
     * @return String 消息的key所对应的value
     */
    public String getProperty(String key,Object[] objs) {
        if (properties == null || !CommonMethod.isNotEmpty(key) ||properties.getProperty(key) == null) {
            return null;
        } else {
            return MessageFormat.format(properties.getProperty(key), objs);
        }
    }

    /**
     * 更新（或插入）一对properties信息(主键及其键值)
     * 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
     * @param key 键名
     * @param value 键值
     */
    public void setProperty(String key,String value) {
    	 OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            properties.setProperty(key, value);
            properties.store(out, null);
            
        } catch (IOException e) {
           logger.error("occur error when upate the property! filePath = "+ filePath);
        }finally{
        	if(out != null){
        		try {
					out.flush();
					out.close();
					out = null;
				} catch (IOException e) {
					logger.error("",e);
				}
        	}
        }
    }

    /**
     * 获得属性文件的路径.
     *
     * @return String 属性文件路径
     */
    protected String getFilePath() {
        return filePath;
    }

    /**
     * @return the propertyUtilMap
     */
    protected static Map<String,PropertyUtils> getPropertyUtilMap() {
        return propertyUtilMap;
    }

    /**
     * @return the modifyTime
     */
    public long getModifyTime() {
        return modifyTime;
    }
    
    public void setModifyTime(long modifyTime){
    	this.modifyTime = modifyTime;
    }

    /**
     *
     * @return Properties
     */
    protected Properties getProperties() {
        return properties;
    }

}

/**
 * 检查文件是否修改过，如果修改过就重新加载.
 *
 * @author Cherry
 *
 */
class CheckFileChange implements Runnable {

    PropertyUtils propertyUtil = null;

    public CheckFileChange(PropertyUtils propertyUtil) {
        this.propertyUtil = propertyUtil;
    }

    @SuppressWarnings("static-access")
	@Override
    public void run() {
    	//LogUtils.info("检查文件是否更新,开始时间:"+System.currentTimeMillis());
        try {
            Map<String,PropertyUtils> properties = propertyUtil.getPropertyUtilMap();
            if (null != properties) {
                for (String name : properties.keySet()) {
                    PropertyUtils util =  properties.get(name);
                    File file = new File(util.getFilePath());
                    // 变更时间
                    long updateTime = file.lastModified();
                    if (util.getModifyTime() != updateTime) {
                    	//LogUtils.info("重新加载配置文件:"+util.getFilePath());
                        InputStream instream = new FileInputStream(file);
                        util.getProperties().load(instream);
                        instream.close();
                        //设置时间
                        util.setModifyTime(updateTime);
                    }
                }
            }
        } catch (Exception e) {
        	PropertyUtils.logger.error("CheckFileChange run() error",e);
        }
    }

}
