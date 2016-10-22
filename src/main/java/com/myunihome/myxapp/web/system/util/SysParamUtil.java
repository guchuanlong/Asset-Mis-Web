package com.myunihome.myxapp.web.system.util;

import javax.servlet.jsp.PageContext;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.framepage.util.OperInfoUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;

@Component
public final class SysParamUtil {
    private SysParamUtil() {
    }

    public static String getSysParamDesc(PageContext pageContext, String typeCode, String paramCode,
            String value) {
        ICacheSV cacheSV = DubboConsumerFactory.getService(ICacheSV.class);
        return cacheSV
                .getSysParam(OperInfoUtil.getTenantId(pageContext), typeCode, paramCode, value)
                .getColumnDesc();
    }

    public static JSONArray getSysParams(PageContext pageContext, String typeCode,
            String paramCode) {
        ICacheSV cacheSV = DubboConsumerFactory.getService(ICacheSV.class);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(
                cacheSV.getSysParams(OperInfoUtil.getTenantId(pageContext), typeCode, paramCode));
        return jsonArray;
    }
}
