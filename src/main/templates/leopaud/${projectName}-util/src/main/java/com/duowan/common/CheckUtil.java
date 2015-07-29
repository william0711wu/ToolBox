
package com.duowan.common;

import com.duowan.leopard.core.exception.StatusCodeException;

/** 公用的check工具类 */
public class CheckUtil
{
	/** 断言 flag == true */
	public static void assertTrue(boolean flag, int code, String errMsg)
	{
		if(!flag) 
		{
            throw new StatusCodeException(code, errMsg);
        }
	}

	
	/** 断言 flag == false */
	public static void assertFalse(boolean flag, int code, String errMsg)
	{
		if(flag) 
		{
            throw new StatusCodeException( code, errMsg) ;
        }
	}
}
