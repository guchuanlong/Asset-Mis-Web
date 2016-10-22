package com.myunihome.myxapp.web.system.util;
/**
 * 算费用String处理
 * @author zhangbc
 *
 */
public final class FeesStringUtil {
	
	
	private FeesStringUtil()
	{
		
	}
	
	/**
	 * 请求的空值以及null转换成0方便算费
	 * @param fees
	 * @return
	 */
	public static String nullEmpty2Zero(String fees)
	{
		if(null==fees||"".equals(fees.trim()))
		{
			fees="0";
		}
	 return fees;
	}
	
	
	/**
	 * 厘转换成元
	 * @param fees
	 * @return
	 */
	public static long liToyuan(long fees)
	{
		
		return fees/1000;
	}
	
	/**
	 * 元转换成厘
	 * @param fees
	 * @return
	 */
	public static long yuanToli(long fees)
	{
		return fees*1000;
	}

}
