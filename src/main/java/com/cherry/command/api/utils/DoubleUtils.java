package com.cherry.command.api.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 
 如果使用 double 构造，API中已经说明，这个构造器的结果可能会有不可预知的结果。如： LogUtils.debug(new BigDecimal(1.2)); 
结果是：1.1999999999999999555910790149937383830547332763671875
而并非我们认为的 1.2；

所以再构造BigDecimal的时候，原则上我们使用 BigDecimal(String val)构造，这样就不会出问题
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月21日 下午5:15:44
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class DoubleUtils {
	
	private DoubleUtils(){}
	
	/**
     * 判断一字符串是否可转化为Double
     * @param str
     * @return   可转为true
     */
     public static Boolean isDoubleType(String str) {
         try {
                 Double.valueOf(str);
                 return true;
         } catch (Exception e) {
                 return false;
         }
     }
     
     /**
      * 判断一个数组中的元素是否都是Double类型
      * @param array
      * @return
      */
     public static Boolean isAllDoubleType(String... array){
    	boolean flag = false;
 		if(CommonMethod.isArrayNotEmpty(array)){
 			for (String str : array) {
 				flag = isDoubleType(str);
 				if(!flag){//只要有空的情况就立即结束
 					break;
 				}
 			}
 		}
 		return flag;
     }

	/** 
     * 对double数据进行取精度. 
     * @param value double数据. 
     * @param scale 精度位数(保留的小数位数). 
     * @param roundingMode 精度取值方式. 
     * @return 精度计算后的数据. 
     */ 
    public static double round(double value, int scale, int roundingMode) {   
        BigDecimal bd = new BigDecimal(value);   
        bd = bd.setScale(scale, roundingMode);   
        double d = bd.doubleValue();   
        bd = null;   
        return d;   
    }   


     /** 
     * double 相加 
     * @param d1 
     * @param d2 
     * @return 
     */ 
    public static double sum(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.add(bd2).doubleValue(); 
    } 


    /** 
     * double 相减 
     * @param d1 
     * @param d2 
     * @return 
     */ 
    public static double sub(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.subtract(bd2).doubleValue(); 
    } 

    /** 
     * double 乘法 
     * @param d1 
     * @param d2 
     * @return 
     */ 
    public static double mul(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.multiply(bd2).doubleValue(); 
    } 


    /** 
     * double 除法 
     * @param d1 
     * @param d2 
     * @param scale 四舍五入 小数点位数 
     * @return 
     */ 
    public static double div(double d1,double d2,int scale){ 
        // 当然在此之前，你要判断分母是否为0，   
        // 为0你可以根据实际需求做相应的处理 

        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.divide(bd2,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    } 
    
    
    /**
     * 会4舍5入的
     * @param value
     * @param scale
     * @return
     */
    public static double getDoubleValue(double value, int scale){
    	String pattern = ".";
    	for (int i = 0; i < scale; i++) {
    		pattern += "#";
		}
    	DecimalFormat df = new DecimalFormat(pattern);
		return Double.valueOf(df.format(value));
    }
	
}
