package com.cherry.command.api.http.common;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
  	用户自定义ServletContextHandler，继承自ServletContextHandler，为了可以添加过滤器
 	在jetty.xml文件中不好配置{@code EnumSet<DispatcherType>},故继承已省略此参数
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午9:02:50
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class CustomeServletContextHandler extends ServletContextHandler {
	/**
	 * convenience method to add a filter
	 * @param filterClass
	 * @param pathSpec
	 * @return
	 */
	public FilterHolder addFilter(String filterClass, String pathSpec) {
		return getServletHandler().addFilterWithMapping(filterClass, pathSpec,
				EnumSet.allOf(DispatcherType.class));
	}
	/**
	 * convenience method to add a filter
	 * @param holder
	 * @param pathSpec
	 */
	public void addFilter(FilterHolder holder,String pathSpec){
		getServletHandler().addFilterWithMapping(holder,pathSpec,EnumSet.allOf(DispatcherType.class));
	}
	/**
	 * convenience method to add a filter
	 * @param filterClass
	 * @param pathSpec
	 * @return
	 */
	public FilterHolder addFilter(Class<? extends Filter> filterClass,String pathSpec)
    {
        return getServletHandler().addFilterWithMapping(filterClass,pathSpec,EnumSet.allOf(DispatcherType.class));
    }

}
