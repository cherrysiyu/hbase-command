package com.cherry.command.api.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月21日 下午5:06:14
 * <p>操作公用方法
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class CommonMethod {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonMethod.class);
	
	
	private CommonMethod(){}

	/**
	 * 根据java类的获取配置文件的路径,转换为配置文件的绝对路径
	 * linux环境下获取的文件路径为: file:/usr/java/apache-tomcat/webapps/web/WEB-INF/classes/db.xml
	 * window环境下获取的文件路径为: /F:/SVNCODE_2009/GUI_common_data/resources/WEB-INF/classes/db.xml
	 * 针对上面两种情况中多余的"file:" 和 "/"进行相关处理
	 * @param fileName 
	 * @return 根据java类的获取配置文件的路径,返回绝对路径	
	 */
	public static String getAbsoluteFilePath(String fileName){
		String fileSeparator = System.getProperty("file.separator");
		if(fileSeparator.equals( "\\" ) ){
            if(fileName.indexOf('/') > 0){
                fileName = fileName.substring(fileName.indexOf('/'));      
            }      
        }
        if(fileSeparator.equals( "/" ) ){
              fileName = fileName.substring(fileName.indexOf('/'));      
        }
        return fileName;
	}
	
	 /**
     * 去除特殊字符串
     * @param str  要过滤的字符串
     * @param regEx  指定要过滤的正则表达式(如果不指定此方法就会使用默认的正则把所有特殊字符全部过滤掉)
     * @return 去除特殊字符后的字符串
     */
    public static String StringFilter(String str,String regEx){
    	
   	 if(isEmpty(str)){
   		 return "";
   	 }
   	 String defaultRegEx = "[`~!@#$%^&*()+=|{}':;',///\\[//\\].<>/?~！_@#￥%……&* （）——+|{}【】‘；：”“’。，、？]";
   	 // 只允许字母和数字        
   	  // String   regEx  =  "[^a-zA-Z0-9]";                      
   	  // 清除掉所有特殊字符   
   	  //String regEx="[`~!@#$%^&*()+=|{}':;',///\\[//\\].<>/?~！_@#￥%……&* （）——+|{}【】‘；：”“’。，、？]";  
   	 if(isEmpty(regEx)){
   		regEx = defaultRegEx;
   	 }
   	  Pattern   p   =   Pattern.compile(regEx);      
   	  Matcher   m   =   p.matcher(str);      
   	  return   m.replaceAll("").trim();    
    }
    
	
		
	/**
	 * 用"<br>
	 * "替换回车符和字符'\r\n'
	 * @param orgStr 含有回车符的字符串。
	 * @return 替换后的字符串.
	 */
	public static String replaceEnter(String orgStr) {
		if (orgStr == null || orgStr.length() == 0)
			return orgStr;
		return orgStr.replaceAll("\r\n", "<br>");
	}

	/**
	 * 用指定的字符替换回车符和字符'\r\n'
	 * @param orgStr  含有回车符的字符串。
	 * @param value  用来代替回车的字符
	 * @return 替换后的字符串.
	 */
	public static String replaceEnter(String orgStr, String value) {
		if (orgStr == null || orgStr.length() == 0)
			return orgStr;
		return orgStr.replaceAll("\r\n", value);
	}
    /**
     * 转换HTML字符转
     * @param s 需要转移的字符串
     * @return 转以后的字符串
     */
    public static String htmlToCode(String s){
		if(isEmpty(s)){
			return "";
		}else{
			s = s.replace("\n\r", "<br>&nbsp;&nbsp;");
			s = s.replace("\r\n", "<br>&nbsp;&nbsp;");
			s = s.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
			s.replace(" ", "&nbsp;");
			s.replace("\"", "\\"+"\"");
			return s;
		}
    }
    
    public static String htmlFilter(String str) {
		if(str == null) return "";
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < str.length(); i++){
			switch(str.charAt(i)) {
			case '&': sb.append("&amp;");
				break;
			case '"': sb.append("&quot;");
				break;
			case '\'': sb.append("&#0039;");
				break;
			case '<': sb.append("&lt;");
				break;
			case '>': sb.append("&gt;");
				break;
			case ' ': sb.append("&nbsp;");
				break;
			case '*': sb.append("");
				break;
			default: sb.append(str.charAt(i));
        	}
        }
        return sb.toString();
	}
    
    /**
	 * 将字符串中html特殊字符替换成转义字符
	 * @param str  需要处理的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceHtmlChars(String str) {
		if (!isNotEmpty(str)){// 检查是否空字符串
			return "";
		}else {
			return str.replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
					.replaceAll("\'", "&#039;").replaceAll("<", "&lt;")
					.replaceAll(">", "&gt;");
		}
	}

	/**
	 * 将字符串中被替换的html转义字符还原成html特殊字符
	 * @param str  需要处理的字符串
	 * @return 还原后的字符串
	 */
	public static String restoreHtmlChars(String str) {
		if (!isNotEmpty(str)){// 检查是否空字符串
			return "";
		}else {
			return str.replaceAll("&amp;", "&").replaceAll("&quot;", "\"")
					.replaceAll("&#039;", "\'").replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">");
		}
	}
    
    /**
     * 得到iso-8859-1编码的字符串
     * @param value 文件名
     * @param value 要转换的字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getIso_8859_1EncodeString(String value)throws UnsupportedEncodingException{
    	return new String(value.getBytes("gbk"), "iso-8859-1");
    }
    
    /**
	 * 把Char字符转换成UniCode码
	 * @param s 
	 * @return
	 */
	public static String convertChar2Unicode(String s) {
		String unicode = "";
		char[] charAry = new char[s.length()];
		for (int i = 0; i < charAry.length; ++i) {
			charAry[i] = s.charAt(i);
			unicode = unicode + "\\u" + Integer.toString(charAry[i], 16);
		}
		return unicode;
	}
	
	/**
	 * 判断一个集合是否是空值
	 * @param collection 带判断的集合
	 * @return true/false (true:不是空的，false：是空的)
	 */
	public static boolean isCollectionNotEmpty(Collection<?> collection){
		return collection != null && !collection.isEmpty();
	}
	
	/**
	 * 判断一个Map集合是否是空值
	 * @param map 带判断的Map集合
	 * @return true/false (true:不是空的，false：是空的)
	 */
	public static boolean isMapNotEmpty(Map<?,?> map){
		return map != null && !map.isEmpty();
	}
	
	/**
	 * <font color='red'>判断一个数组是否为空的,不推荐使用此方法判断数组是否为空，可以使用org.apache.commons.lang.ArrayUtils.isNotEmpty()方法来判断</font>
	 * @param array 待判断的数组
	 * @return true/false (true:不是空的，false：是空的)
	 */
	public static <T> boolean isArrayNotEmpty(T... array) {
		if(array == null)
			return false;
		else if(array.getClass().isArray()){
			boolean flag = true;
			for (T t : array) {
				if(t== null)
					flag = false;
				else if(t.getClass().isArray()){
					if("{}".equals(ArrayUtils.toString(t)))
						flag = false;
				}
			}
			return flag;	
		}
		return false;
    }
	
	/**
	 * 判断是否不是空，可以支持判断Object 类型,String 类型，Number 类型，Map类型及其子类，collection及其子类,以及数组对象(<font color='red'>不推荐使用此方法判断数组是否为空，可以使用org.apache.commons.lang.ArrayUtils.isNotEmpty()</font>),
	 * @param obj Object ,String,Map(及其子类)，Collection(及其子类) Array 类型
	 * @return 是否不是空值，如果不是空返回true，否则返回false
	 */
	public static boolean isNotEmpty(Object obj){
		
		if(obj == null)
			return false;
		else if(obj.getClass() == String.class)
			return !"".equals(obj);
		else if(obj instanceof Number)
			return obj != null;
		else if(obj instanceof Map<?,?>)
			return !((Map<?,?>)obj).isEmpty();
		else if(obj instanceof Collection<?>)
			return !((Collection<?>)obj).isEmpty();
		else if(obj.getClass().isArray())
			return Array.getLength(obj) != 0;
		else 
		return false;
	}
	
	/**
	 * 判断是否是空，可以支持判断Object 类型,String 类型，Map类型及其子类，collection及其子类,以及数组对象
	 * @param obj Object ,String,Map(及其子类)，Collection(及其子类) Array 类型
	 * @return 是否是空值，如果是空返回true，否则返回false
	 */
	public static boolean isEmpty(Object obj){
		return !isNotEmpty(obj);
	}
	
	
	/**
	 * 判断可变个参数范围类是否都是不为空的情况
	 * <pre>
	 * <b>说明:此种方法适合于界面上一次判断多个空值的情况,多个参数间是并且的关系</b></br>
	 *  例子:</br>
	 *  if(string != null && list != null && map != null){
	 *  	doSomething();
	 *  	...
	 *  }
	 *  可以替换成:
	 *	if(isAllNotEmpty(string,list,map)){
	 *  	doSomething();
	 *  	...
	 *  }
	 * </pre>
	 * @param objects 
	 * @return 如果都不为空，则返回true，只要有一个为空则返回false
	 */
	public static boolean isAllNotEmpty(Object... objects ){
		boolean flag = false;
		if(objects != null && objects.length > 0){
			for (Object object : objects) {
				flag = isNotEmpty(object);
				if(!flag){//只要有空的情况就立即结束
					break;
				}
			}
		}
		return flag;
	}
	
	public static boolean isAnyEmpty(Object... objects ){
		return !isAllNotEmpty(objects);
	}
	/**
     * 判断是否为整数
     * @param str 需要判断的对象
     * @return 如果对象是整数,则返回true;否则返回false;
     */
    public static boolean isInteger(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为浮点数，包括double和float
     * @param str 需要判断的对象
     * @return 如果对象是浮点数,则返回true;否则返回false;
     */
    public static boolean isDouble(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
        return pattern.matcher(str).matches();
    }
    /**
     * 判断输入的字符串是否符合Email样式
     * @param email
     * @return 如果为mail,则返回true.如果不是mail,则返回false
     */
    public static boolean isEmail(String email){
        if(email == null || email.length() < 1 || email.length() > 256){
            return false;
        }
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(email).matches();
    }

    /**
     * 判断输入的字符串是否为纯汉字
     * @param str 需要判断的字符串
     * @return 如果字符串中无汉字,则返回true;如果有汉字,则返回false
     */
    public static boolean isAllChinese(String str){
        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(str).matches();
    }
    
    /**
     * 是够包含中文
     * @param str  需要判断的字符串
     * @return
     */
    public static boolean isContainsChinese(String str){    
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        Matcher matcher = pattern.matcher(str); 
        return matcher.find();
    } 

    /**
     * 根据需要转换的字符串,转换为int类型
     * @param str 需要转换的字符串
     * @return 如果字符串含有特殊字符等情况,则返回0;如字符串正常,则返回转换后的Interger类型的字符串
     */
    public static int convertStrToInt(String str){
        if(isNotEmpty(str) && !"null".equals(str)){
            try{
                return Integer.parseInt(str);
            }catch(NumberFormatException ex){
            	logger.error("类型转换异常:",ex);
            }
        }
        return 0;
    }
	/**
	 * Utf8URL编码
	 * @param s
	 * @return
	 */
	public static String Utf8URLencode(String text) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 0 && c <= 255) {
				result.append(c);
			} else {
				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes("UTF-8");
				} catch (Exception ex) {
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}
	
	
	/**
	 * Utf8URL解码
	 * @param text
	 * @return
	 */
	public static String Utf8URLdecode(String text) {
		String result = "";
		int p = 0;
		if (text != null && text.length() > 0) {
			text = text.toLowerCase();
			p = text.indexOf("%e");
			if (p == -1)
				return text;
			while (p != -1) {
				result += text.substring(0, p);
				text = text.substring(p, text.length());
				if (text == "" || text.length() < 9)
					return result;
				result += CodeToWord(text.substring(0, 9));
				text = text.substring(9, text.length());
				p = text.indexOf("%e");
			}
		}
		return result + text;
	}
     
	/**
	 * utf8URL编码转字符
	 * @param text
	 * @return
	 */
	private static String CodeToWord(String text) {
		String result;
		if (Utf8codeCheck(text)) {
			byte[] code = new byte[3];
			code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
			code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
			code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
			try {
				result = new String(code, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				result = null;
			}
		} else {
			result = text;
		}
		return result;
	}
	
	/**
	 * 编码是否有效
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean Utf8codeCheck(String text) {
		String sign = "";
		if (text.startsWith("%e"))
			for (int i = 0, p = 0; p != -1; i++) {
				p = text.indexOf("%", p);
				if (p != -1)
					p++;
				sign += p;
			}
		return sign.equals("147-1");
	}
	
    
    /**
	 * 如果Throwable是Error，那么将它抛出，如果是RuntimeException那么返回，否则抛出IllegalStateException
	 * @param t
	 * @return
	 */
    public static RuntimeException launderThrowable(Throwable t){
		
		if(t instanceof RuntimeException)
			return (RuntimeException)t;
		else if(t instanceof Error)
			throw (Error) t;
		else
			throw new IllegalStateException("Not uncheded,"+t);
	}
    
    /**
     * 得到指定分隔的字符串
     * @param separater 分隔符号
     * @param coll 对象集合
     * @param separaterRule 生成集合中单个对象String的规则
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getSeparaterString(String separater, Collection<?> coll, SeparaterRule separaterRule ){
		if(!isCollectionNotEmpty(coll))
			return "";
		if( separater == null )
			throw new IllegalArgumentException( "分隔符不可为null" );
		
		List<String> list = new ArrayList<String>();
		for ( Object object:coll ){
			String string = separaterRule.getString(object);
			if(isNotEmpty(string))
				list.add(string);
			
		}
		return getSeparaterString(separater,list );
	}
    
    /**
	 * 得到指定分隔的字符串
	 * @param separater 分隔符号
	 * @param maps 要分隔集合
	 * @param key 分隔集合中map的键值
	 * @throws IllegalAccessException 不和函数调用逻辑异常
	 */
	public static String getSeparaterString(String separater, Collection<? extends Map<String,Object>> maps, String key){
		
		if(!isCollectionNotEmpty(maps))
			return "";
		if( separater == null )
			throw new IllegalArgumentException( "分隔符不可为null" );
		
		List<String> list = new ArrayList<String>();
		for ( Map<String,Object> map:maps ){
			Object obj = map.get( key );
			if(isNotEmpty(obj))
				list.add(String.valueOf(obj));
		}
		return getSeparaterString( separater,  list );
	}
    
    /**
	 * 得到指定分隔的字符串
	 * @param separater 分隔符号
	 * @param coll 要分隔集合
	 * @throws IllegalAccessException 不和函数调用逻辑异常
	 */
	public static String getSeparaterString(String separater, Collection<?> coll){
		if (!isCollectionNotEmpty(coll))
			return "";
		List<String> list = new ArrayList<String>(coll.size());
		for (Object obj : coll) {
			if(isNotEmpty(obj))
				list.add(String.valueOf(obj));
		}
		return getSeparaterString(separater, list.toArray(new String[]{}));
	}
	/**
	 * 得到指定分隔的字符串,以默认的逗号隔开
	 * @param numbers 
	 * @return
	 */
	public static String getSeparaterStringNumber(Collection<? extends Number> numbers){
		if(!isCollectionNotEmpty(numbers))
			return "";
		List<String> list = new ArrayList<String>(numbers.size());
		for (Number number : numbers) {
			if(number != null)
			list.add(String.valueOf(number));
		}
		return getSeparaterString(",",list.toArray(new String[]{}));
	}
	
	/**
	 * 得到指定分隔的字符串
	 * @param separater 分隔符号
	 * @param numbers   要分隔集合
	 * @return
	 */
	public static String getSeparaterStringNumber(String separater,Collection<? extends Number> numbers){
		if(!isCollectionNotEmpty(numbers)|| isEmpty(separater))
			return "";
		List<String> list = new ArrayList<String>(numbers.size());
		for (Number number : numbers) {
			if(number != null)
			list.add(String.valueOf(number));
		}
		return getSeparaterString(separater,list.toArray(new String[]{}));
	}
	/**
	 * 得到指定分隔的字符串
	 * @param separater 分隔符号
	 * @param strings 要分隔集合
	 * @throws IllegalAccessException 不符合函数调用逻辑异常
	 */
	public static String getSeparaterString(String separater, String... strings  ) {
		return getSeparaterString(separater,false, strings );
	}
	
	/**
	 * 得到sql语句中使用逗号分隔的字符串
	 * @param coll 对象集合
	 * @param separaterRule 生成集合中单个对象String的规则
	 * @return 逗号分隔的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String getSQLSeparaterString( Collection<?> coll, @SuppressWarnings("rawtypes") SeparaterRule separaterRule ){
		if(!isCollectionNotEmpty(coll) || separaterRule == null)
			return "";
		
		List<String> list = new ArrayList<String>();
		for ( Object object:coll ){
			String string = separaterRule.getString(object);
			if(isNotEmpty(string))
				list.add(string);
		}
		return getSQLSeparaterString(list);
	}
	
	/**
	 * 得到sql语句中使用逗号分隔的字符串
	 * @param coll 含有Map对象的集合
	 * @param key map中的键值
	 * @return 逗号分隔的字符串
	 */
	public static String getSQLSeparaterString( Collection<? extends Map<String,Object>> coll, String key){
		if(!isCollectionNotEmpty(coll))
			return "";
		List<String> list = new ArrayList<String>();
		for ( Map<String,Object> map:coll ){
			Object object = map.get(key);
			if(isNotEmpty(object))
				list.add(String.valueOf(object));
		}
		return getSQLSeparaterString(  list );
	}
	
	/**
	 * 得到sql语句中使用逗号分隔的字符串
	 * @param strings string类型的集合对象
	 * @return 逗号分隔后的字符串
	 */
	public static String getSQLSeparaterString(Collection<String> strings){
		if(!isCollectionNotEmpty(strings))
			return "";
		return getSQLSeparaterString(strings.toArray(new String[]{}));
	}
	
	
	/**
	 * 得到sql语句中使用逗号分隔的字符串
	 * @param strings string类型数组
	 * @return 逗号分隔后的字符串
	 */
	public static String getSQLSeparaterString( String... strings  ){
		return getSeparaterString(  ",",  true,  strings  );
	}
	
	/**
	 * 得到指定分隔的字符串
	 * @param separater 分隔符号
	 * @param isAppSql 是否应用于SQL语句
	 * @param strings 要分隔集合
	 * @throws IllegalAccessException 不和函数调用逻辑异常
	 */
	private static String getSeparaterString(String separater,boolean isAppSql,String... strings  ) {
		
		if(!isArrayNotEmpty(strings))
			return "";
		if( separater == null )
			throw new IllegalArgumentException( "分隔符不可为null" );
		if(isAppSql)
			separater = ",";
		
		StringBuilder strb = new StringBuilder();
		for (String string : strings) {
			
			if(isEmpty(string))
				continue;
			if(string.contains(separater))
				throw new IllegalArgumentException( "集合中含有分隔符号" );
			
			if( strb.length()>0)
				strb.append( separater );
			
			if(isAppSql)
				strb.append("'").append(string).append("'");
			else
				strb.append(string);
		}
		
		return strb.toString();
	}
	/**
	 * JavaBean转换成Map
	 * @param javaBean javaBean对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> convertBean2Map(Object javaBean){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			Map<String,Object> describe = BeanUtils.describe(javaBean);
			describe.remove("class");
			map.putAll(describe);
		} catch (Exception e) {
			logger.error("convertBean2Map error",e);
		}
		return map;
	}
	/**
	 * 把Map转换成JavaBean
	 * @param <T>
	 * @param beanClass JavaBean的class
	 * @param beanMap 需要转换成JavaBean的Map
	 * @return
	 */
	public static <T> T convertMap2Bean(Class<T> beanClass,Map<String,Object> beanMap){
		if(beanClass == null || !CommonMethod.isMapNotEmpty(beanMap))
			throw new IllegalArgumentException( "beanClass不能为null，并且beanMap不能为空" );
		JacksonUtils instance = JacksonUtils.getInstance();
		T t = null;
		try {
			t = instance.readJSON2Bean(beanClass, instance.writerJavaObject2JSON(beanMap));
		}catch (Exception e) {
			logger.error("convertMap2Bean error",e);
		}
		return t;
	}
	/**
	 * 如果是NullPointException的话使用e.getMessage()得到的是null而不会得到所有的异常信息，所以使用如下方法来获取异常信息
	 * @param t
	 * @return
	 */
	public static String getStackTraceMessage(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
}
