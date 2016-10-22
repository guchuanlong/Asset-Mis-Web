package com.myunihome.myxapp.web.webtag;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.myunihome.myxapp.web.system.util.SysParamUtil;

public final class DictUtil {

    private DictUtil() {
    }

    private static final Logger logger = LogManager.getLogger(DictUtil.class);

    /**
     * 获取参数表GN_SYS_PARAM 参数描述信息
     *
     * @param dictId TYPE_CODE.PARAM_CODE
     * @param value  COLUMN_VALUE
     * @return
     */
    public static String getValueDesc(PageContext pageContext, String dictId, String value) {
        try {
            String result = "";
            if (StringUtils.isNotBlank(dictId) && dictId.contains(".")) {
                //--表名、字段名
                String typeCode = dictId.substring(0, dictId.indexOf('.'));
                String paramCode = dictId.substring(dictId.indexOf('.') + 1);
                result = SysParamUtil.getSysParamDesc(pageContext, typeCode, paramCode, value);
            }
            return result;
        } catch (Exception e) {
            logger.error("翻译字段失败", e);
        }
        return null;
    }

    /**
     * 获取参数表GN_SYS_PARAM 参数信息
     *
     * @param dictId TYPE_CODE.PARAM_CODE
     * @return
     */
    public static JSONArray getSysParamS(PageContext pageContext, String dictId) {
        try {
            JSONArray arrays = null;
            if (StringUtils.isNotBlank(dictId) && dictId.contains(".")) {
                //--表名、字段名
                String typeCode = dictId.substring(0, dictId.indexOf('.'));
                String paramCode = dictId.substring(dictId.indexOf('.') + 1);
                arrays = SysParamUtil.getSysParams(pageContext, typeCode, paramCode);
                if (arrays == null || arrays.isEmpty() || arrays.size() == 0) {
                    arrays = SysParamUtil.getSysParams(pageContext, typeCode, paramCode);
                }
            }
            return arrays;
        } catch (Exception e) {
            logger.error("翻译字段失败", e);
        }
        return null;
    }
}
