package com.myunihome.myxapp.web.system.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.common.api.cache.param.SysParam;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;

@Component
public final class SysCacheParamUtil {
	
	private SysCacheParamUtil(){}
	
	
	/**
	 * 
	 * @param tenantId
	 * @param typeCode
	 * @param paramCode
	 * @return
	 */
	public static Map<String, String> getSysParam(String tenantId, String typeCode, String paramCode)
	{
		ICacheSV cacheSV = DubboConsumerFactory.getService("cacheSV");
        List<SysParam> paramList = cacheSV.getSysParams(tenantId, typeCode, paramCode);
		Map<String, String> map=new HashMap<String, String>();
		for(int i=0;i<paramList.size();i++)
		{
			SysParam sysParam=paramList.get(i);
			map.put(sysParam.getColumnValue(), sysParam.getColumnDesc());
		}
		return map;
	}

    /**
     * 获取字典参数列表
     * @param tenantId  租户ID
     * @param typeCode  字典类型
     * @param paramCode 字典参数
     * @return          参数集合
     * @author liangbs
     * @ApiDocMethod
     */
    public static JSONArray getSysParams(String tenantId, String typeCode, String paramCode) {
        
        ICacheSV cacheSV = DubboConsumerFactory.getService("cacheSV");
        List<SysParam> paramList = cacheSV.getSysParams(tenantId, typeCode, paramCode);
        
        JSONArray jsonArray = new JSONArray();
        if(paramList != null){
            jsonArray.addAll(paramList);
        }
        
        return jsonArray;
    }
    
    /**
     * 获取单条字典参数
     * @param tenantId      租户ID
     * @param typeCode      字典类型
     * @param paramCode     字典参数
     * @param columnValue   参数值
     * @return              参数描述
     * @author liangbs
     * @ApiDocMethod
     */
    public static SysParam getSysParam(String tenantId, String typeCode, String paramCode, String columnValue) {
        ICacheSV cacheSV = DubboConsumerFactory.getService("cacheSV");
        return cacheSV.getSysParam(tenantId, typeCode, paramCode, columnValue);
    }
    
    /**
     * 获取单条字典参数描述
     * @param tenantId      租户ID
     * @param typeCode      字典类型
     * @param paramCode     字典参数
     * @param columnValue   参数值
     * @return              参数描述
     * @author liangbs
     * @ApiDocMethod
     */
    public static String getSysParamDesc(String tenantId, String typeCode, String paramCode, String columnValue) {
        ICacheSV cacheSV = DubboConsumerFactory.getService("cacheSV");
        SysParam sysParam = cacheSV.getSysParam(tenantId, typeCode, paramCode, columnValue);
        if(sysParam != null){
            return sysParam.getColumnDesc();
        }
        return null;
    }
}
