package com.cherry.command.api.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 切分List集合的帮助类，当List的集合size&gt;1000时，可以调用下面的方法把集合拆分成指定大小的集合的集合</br>
<span style="font:bold;">说明:此类的用处主要是因为数据库in后面","隔开1000个就会挂掉，写这个类是为了为了将大于1000的List结合切分成一个个的小的List，便于操作</span>
 * 
 * <p>
 * Copyright: Copyright (c) 2014年10月21日 下午5:54:23
 * <p>
 * 
 * <p>
 * 
 * @author Cherry
 * @version 1.0.0
 */
public class ListSeparaterUtils {

	/**
	 * 默认集合的大小800
	 */
	private static final int DEFAULT_SIZE = 800;
	
	/**
	 * 默认采用DEFAULT_SIZE（默认值是800）的方式切分集合</br>
	 * <span style="font:bold;">说明:传入的list集合的size大小小于separaterSize的大小会直接新建一个集合，然后把原来传入的集合放进去返回</span>
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<List<T>> separaterList2Lists(List<T> list){
		return separaterList2Lists(list,DEFAULT_SIZE);
	}
	
	/**
	 * 指定要切分的大小切分一个集合</br>
	 * <span style="font:bold;">
	 * <ul>说明:<ul>
	 * <li>1.当指定的separaterSize 不合法是会抛出IllegalArgumentException异常，separaterSize must in  0&gtseparaterSize&lt1000"</li>
	 * <li>2.传入的list集合的size大小小于separaterSize的大小会直接新建一个集合，然后把原来传入的集合放进去返回</li>
	 * </span>
	 * @param <T>
	 * @param list
	 * @param separaterSize
	 * @return
	 */
	public static <T> List<List<T>> separaterList2Lists(List<T> list,int separaterSize){
		if(separaterSize < 0){
			 throw new IllegalArgumentException("Illegal separaterSize: "+ separaterSize +",separaterSize must > 0");
		}
		
		if(!CommonMethod.isCollectionNotEmpty(list)){
			return new ArrayList<List<T>>();
		}
		
		int size = list.size();
		if(size < separaterSize){
			List<List<T>> result =  new ArrayList<List<T>>();
			result.add(list);
			return result;
		}
		
		int level = size%separaterSize;//求余数
		int listsSize = size/separaterSize;//求整除的数
		if(level != 0){//如果余数不为0，则集合就需要多加一个
			listsSize++;
		}
		//为了提高list的效率，初始化集合时指定集合的大小
		List<List<T>>  result =  new ArrayList<List<T>>(listsSize);
		//这里看过subList的源代码，如果i<listsSize的话最后一次如果计算出来的i*(separaterSize+1)>集合的list.size(),则会抛出异常
		for (int i = 0; i < listsSize-1; i++) {
			result.add(list.subList(i*separaterSize, (i+1)*separaterSize));
		}
		//把最后一次的剩余的加入
		result.add(list.subList((listsSize-1)*separaterSize,size));
		/*上述这里本来写成size+1的，因为subList方法会返回列表中指定的 fromIndex（包括 ）和 toIndex（不包括）之间的部分视图,
		但是后来想了下发现数组是从0开始的，所以List集合中的最后一个元素是get(size-1),所以这里直接填写size了*/
		
		return result;
	}
}
