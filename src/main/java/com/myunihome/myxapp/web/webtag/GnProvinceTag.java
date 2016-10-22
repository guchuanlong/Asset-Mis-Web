package com.myunihome.myxapp.web.webtag;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.myunihome.myxapp.common.api.area.param.AreaLevel;
import com.myunihome.myxapp.common.api.area.param.GnAreaVo;
import com.myunihome.myxapp.web.system.util.AreaUtil;

public class GnProvinceTag extends BodyTagSupport {
    private static final Logger logger = LogManager.getLogger(GnProvinceTag.class);
    private static final long serialVersionUID = 2009910132830670420L;
    
    /* 字典相关 */

    private String defaultValue;// 默认值

    /* select 控制相关 */

    private String id;

    private String name;

    private String size;

    private String multiple;

    private String disabled;

    private String tabindex;

    // 事件
    private String onfocus;

    private String onblur;

    private String onchange;

    private String onclick;

    private String ondblclick;

    private String onkeydown;

    private String onkeypress;

    private String onkeyup;

    private String onmousedown;

    private String onmousemove;

    private String onmouseout;

    private String onmouseover;

    private String onmouseup;

    private String displayCode;

    /* 被包含的code */
    private String keys;

    /* 被过滤的code */
    private String filters;

    /* 是否允许空值 */

    private String notnull;

    private String nullText = "";

    // **css的控制
    private String styleClass;

    private String style;

    private String preNode;

    private String nextNode;

    // 排序
    private String sort;

    private String sortMode;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getTabindex() {
        return tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getOnfocus() {
        return onfocus;
    }

    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }

    public String getOnblur() {
        return onblur;
    }

    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    public String getOnchange() {
        return onchange;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getOndblclick() {
        return ondblclick;
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    public String getOnkeydown() {
        return onkeydown;
    }

    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    public String getOnkeypress() {
        return onkeypress;
    }

    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    public String getOnkeyup() {
        return onkeyup;
    }

    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    public String getOnmousedown() {
        return onmousedown;
    }

    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    public String getOnmousemove() {
        return onmousemove;
    }

    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    public String getOnmouseout() {
        return onmouseout;
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    public String getOnmouseover() {
        return onmouseover;
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    public String getOnmouseup() {
        return onmouseup;
    }

    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getNotnull() {
        return notnull;
    }

    public void setNotnull(String notnull) {
        this.notnull = notnull;
    }

    public String getNullText() {
        return nullText;
    }

    public void setNullText(String nullText) {
        this.nullText = nullText;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getPreNode() {
        return preNode;
    }

    public void setPreNode(String preNode) {
        this.preNode = preNode;
    }

    public String getNextNode() {
        return nextNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSortMode() {
        return sortMode;
    }

    public void setSortMode(String sortMode) {
        this.sortMode = sortMode;
    }

    public GnProvinceTag() {
        displayCode = "false";
        name = "org.apache.struts.taglib.html.BEAN";
        size = "";
        multiple = "false";
        disabled = "false";
        tabindex = "";
        onfocus = "";
        onblur = "";
        onchange = "";
        onclick = "";
        ondblclick = "";
        onkeydown = "";
        onkeypress = "";
        onkeyup = "";
        onmousedown = "";
        onmousemove = "";
        onmouseout = "";
        onmouseover = "";
        onmouseup = "";
        styleClass = "";
        notnull = "";
        style = "";
        keys = "";
        filters = "";
        nullText = "";
        sort = "CHINESE";
        sortMode = "ITEM";
    }

    @Override
	public int doStartTag() {
        return SKIP_BODY;
    }

    @Override
	public int doEndTag() throws JspException {
        try {
            StringBuilder stringbuffer = new StringBuilder("<select ");
            initAttribute(stringbuffer);
            stringbuffer.append(">\n");
            // stringbuffer.append("<option>请选择</option>\n");

//          String value = TagUtils.getStack(this.pageContext).findString(name);
            String value = TagUtils.getStack(pageContext, name);

            if (value == null || "".equals(value)) {
                value = defaultValue;
                if (StringUtils.isNotEmpty(defaultValue)
                        && value.indexOf("%{") == 0) {
                    value = value.substring(2, value.length() - 1);
                    value = TagUtils.getStack(pageContext, value);
                    value = (value == null ? "" : value);
                }
            }

            /* 处理数据字典 */
            List<GnAreaVo> lstDictItem = initDatasrc();
            if (StringUtils.isNotEmpty(nullText)) {
                stringbuffer.append("\t<option value=''>");
                stringbuffer.append(nullText);
                stringbuffer.append("</option>\n");
            }
            StringBuilder strTemp = new StringBuilder();
            String strCode = null;
            if (lstDictItem != null) {
                GnAreaVo vo;
                for (int i = 0; i < lstDictItem.size(); i++) {
                    vo = lstDictItem.get(i);
                    strCode = "";
                    // 显示code
                    if ("TRUE".equalsIgnoreCase(displayCode)) {
                        strCode = vo.getAreaCode() + "-";
                    }
                    if (vo.getAreaCode().equals(value)) {
                        strTemp.append("\t<option value=\"");
                        strTemp.append(vo.getAreaCode());
                        strTemp.append("\" selected>");
                        strTemp.append(strCode);
                        strTemp.append(vo.getAreaName());
                        strTemp.append("</option>\n");
                    } else {
                        strTemp.append("\t<option value=\"");
                        strTemp.append(vo.getAreaCode());
                        strTemp.append("\">");
                        strTemp.append(strCode);
                        strTemp.append(vo.getAreaName());
                        strTemp.append("</option>\n");
                    }
                }
            }
            stringbuffer.append(strTemp);
            stringbuffer.append("</select>\n");
            pageContext.getOut().print(stringbuffer.toString());

        } catch (Exception e) {
            logger.error("拼接省份下拉框Html片段失败",e);
        }
        return EVAL_PAGE;
    }

    private void appendAttribute(StringBuilder sf, String attrName,
                                 String attrValue) {
        if (attrValue != null && !"".equals(attrValue)) {
            sf.append(attrName);
            sf.append("='");
            sf.append(attrValue);
            sf.append("'");
        }
    }

    /**
     * 初始化下拉框数据源
     *
     * @return
     */
    private List<GnAreaVo> initDatasrc() {
        HttpSession session = pageContext.getSession();
        List<GnAreaVo> vos = AreaUtil.getAreas(AreaLevel.PROVINCE_LEVEL, null,session);
        List<GnAreaVo> res = new ArrayList<GnAreaVo>();
        GnAreaVo vo;
        if (StringUtils.isNotEmpty(keys)) {
            String[] _keys = keys.split(",");
            for (int i = 0; i < _keys.length; i++) {
                for (int j = 0; j < vos.size(); j++) {
                    vo = vos.get(i);
                    if (_keys[i].equals(vo.getAreaCode())) {
                        res.add(vo);
                        break;
                    }

                }
            }
        } else {
            res = vos;
        }
        if (StringUtils.isNotEmpty(filters)) {
            String[] _filters = filters.split(",");
            for (int i = 0; i < _filters.length; i++) {
                for (int j = 0; j < res.size(); j++) {
                    vo = res.get(j);
                    if (_filters[i].equals(vo.getAreaCode())) {
                        res.remove(j);
                        break;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 初始化下拉框数据源
     *
     * @return
     */
    private void initAttribute(StringBuilder stringbuffer) {

        this.appendAttribute(stringbuffer, "id", id);
        this.appendAttribute(stringbuffer, "name", name);

        if ("TRUE".equalsIgnoreCase(multiple)) {
            stringbuffer.append(" multiple ");

        }
        if ("TRUE".equalsIgnoreCase(disabled)) {
            stringbuffer.append(" disabled ");

        }
        // 处理属性
        this.appendAttribute(stringbuffer, "size", size);
        this.appendAttribute(stringbuffer, "tabindex", tabindex);
        this.appendAttribute(stringbuffer, "class", styleClass);
        this.appendAttribute(stringbuffer, "style", style);
        this.appendAttribute(stringbuffer, "notnull", notnull);
        this.appendAttribute(stringbuffer, "keys", keys);
        this.appendAttribute(stringbuffer, "filters", filters);
        /* 处理事件 */
        this.appendAttribute(stringbuffer, "onfocus", onfocus);
        this.appendAttribute(stringbuffer, "onblur", onblur);
        this.appendAttribute(stringbuffer, "onchange", onchange);
        this.appendAttribute(stringbuffer, "onclick", onclick);
        this.appendAttribute(stringbuffer, "ondblclick", ondblclick);
        this.appendAttribute(stringbuffer, "onkeydown", onkeydown);
        this.appendAttribute(stringbuffer, "onkeypress", onkeypress);
        this.appendAttribute(stringbuffer, "onkeyup", onkeyup);
        this.appendAttribute(stringbuffer, "onmousedown", onmousedown);
        this.appendAttribute(stringbuffer, "onmousemove", onmousemove);
        this.appendAttribute(stringbuffer, "onmouseout", onmouseout);
        this.appendAttribute(stringbuffer, "onmouseover", onmouseover);
        this.appendAttribute(stringbuffer, "onmouseup", onmouseup);
    }

    @Override
	public String getId() {
        return id;
    }

    @Override
	public void setId(String id) {
        this.id = id;
    }

}
