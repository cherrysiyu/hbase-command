package com.cherry.command.api;

import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * 程序入口
 * @author Cherry
 * @version 0.1
 * @Desc
 * 2014年10月25日 下午4:00:55
 */
public class Application {
	private static Logger logger = Logger.getLogger(Application.class.getName());
	private static String serverXmlPath="jetty.xml";
	public static void main(String[] args){
		try{
			Resource serverResource  = null;
			if(args == null || args.length ==0){
				//如果没有指定路径则默认使用本地classpath路径
				serverResource = Resource.newClassPathResource(serverXmlPath);
				logger.info("没有系统启动配置文件，使用默认classpath下的文件serverXmlPath:classpath--->"+serverXmlPath);
			}else{//绝对路径
				serverXmlPath = args[0];
				logger.info("使用输入参数配置文件，path:"+serverXmlPath);
				serverResource = Resource.newResource(serverXmlPath);
			}
			XmlConfiguration configuration = new XmlConfiguration(serverResource.getInputStream());
	        Server server = (Server)configuration.configure();
	        server.start();
	        server.join();
		}catch(Exception e){
			e.printStackTrace();
		}
		
        
	}
}
