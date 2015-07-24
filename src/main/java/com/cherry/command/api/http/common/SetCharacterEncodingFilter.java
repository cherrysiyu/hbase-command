package com.cherry.command.api.http.common;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 SetCharacterEncodingFilter 实现中文乱码过滤器
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月22日 上午9:27:18
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class SetCharacterEncodingFilter implements Filter {
	// ----------------------------------------------------- Instance Variables
    /**
     * The default character encoding to set for requests that pass through this
     * filter.
     */
    protected String encoding = null;

 // --------------------------------------------------------- Public Methods
    /**
     * Take this filter out of service.
     */
    public void destroy(){
        this.encoding = null;
    }
    /**
     * Select and set (if specified) the character encoding to be used to
     * interpret request parameters for this request.
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		String requestedWith = request.getHeader("x-requested-with");
		String type = request.getContentType();
		if (requestedWith != null && "XMLHttpRequest".equals(requestedWith)
			&& null != type&& type.indexOf("application/x-www-form-urlencoded") != -1) {
			request.getParameterMap();
		}
		fc.doFilter(req, res);
	}

	 /**
     * Place this filter into service.
     * @param filterConfig The filter configuration object
     */
	public void init(FilterConfig fc) throws ServletException {
		encoding = fc.getInitParameter("encoding");
		if (encoding == null) {
			encoding = "UTF-8";
		}
	}
}
