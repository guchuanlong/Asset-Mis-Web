package com.myunihome.myxapp.web.system.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;

public final class DictUtil {
    
    private static final Logger LOGGER = Logger.getLogger(DictUtil.class);
    
    private DictUtil(){}
    
	/**
	 * 获取参数表GN_SYS_PARAM 参数描述信息
	 * 
	 * @param dictId  TYPE_CODE.PARAM_CODE
	 * @param value    COLUMN_VALUE
	 * @return
	 */
	public static String getValueDesc(String tenantId, String dictId, String value){
		try {
			String result = "";
			if(StringUtils.isNotBlank(dictId)&&dictId.contains(".")){
				//--表名、字段名
				String typeCode = dictId.substring(0,dictId.indexOf('.'));
				String paramCode  = dictId.substring(dictId.indexOf('.')+1);
				result = SysCacheParamUtil.getSysParamDesc(tenantId, typeCode, paramCode, value);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("获取参数失败，错误信息：", e);
		}
		return null;
	}
	
	/**
	 * 获取参数表GN_SYS_PARAM 参数信息
	 * 
	 * @param dictId  TYPE_CODE.PARAM_CODE
	 * @return
	 */
	public static JSONArray getSysParamS(String tenantId, String dictId){
		try {
			JSONArray arrays = null;
			if(StringUtils.isNotBlank(dictId)&&dictId.contains(".")){
				//--表名、字段名
				String typeCode = dictId.substring(0,dictId.indexOf('.'));
				String paramCode  = dictId.substring(dictId.indexOf('.')+1);
				arrays = SysCacheParamUtil.getSysParams(tenantId, typeCode, paramCode);
				if(arrays==null||arrays.isEmpty()||arrays.size()==0){
					arrays = SysCacheParamUtil.getSysParams(tenantId, typeCode, paramCode);
				}
			}
			return arrays;
		} catch (Exception e) {
		    LOGGER.error("获取参数失败，错误信息：", e);
		}
		return null;
	}
}
