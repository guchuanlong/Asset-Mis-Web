package com.myunihome.myxapp.web.system.util;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@SuppressWarnings("serial")
public class SelectTag extends BodyTagSupport{
	
	private static final Logger LOGGER = Logger.getLogger(SelectTag.class);
	
	/* 字典相关 */
	private String id;
	
	private String tenantId; //租户id
	
	private String dictId; // ---大类

	private String parentValue; // 

	private String defaultValue;// 默认值

	/* select 控制相关 */

	private String name;

	private String size;

	private String multiple;

	private String disabled;

	private String tabindex;
	// 是否是可搜索的下拉框
	private String searchSelect;
	// private StringBuilder noselectlist;

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
	private String nullOption;

	private String notnull;

	private String nullText = "";

	// **css的控制
	private String styleClass;

	private String style;

	private String preNode;

	private String nextNode;

	private String sort;

	private String sortMode;

	public SelectTag() {
		id = "";
		dictId = "";
		parentValue = "";

		nullOption = "false";
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
		// noselectlist = null;

		notnull = "";
		style = "";
		keys = "";
		filters = "";
		nullText = "请选择";
		sort = "ASC";
		sortMode = "CODE";

		searchSelect = "FALSE";
	}

	@Override
	public int doStartTag()
	// throws JspException
	{
		return SKIP_BODY;

	}

	@Override
	public int doEndTag() throws JspException {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			if ("TRUE".equals(this.searchSelect.toUpperCase())) {
				String selName = (this.name).replaceAll("\\.", "");
				//获取参数值
				String dfCode = TagUtils.getStack(this.pageContext, name);
				if (dfCode == null || "".equals(dfCode)){
					dfCode = defaultValue;
					if (StringUtils.isNotEmpty(defaultValue) && dfCode.indexOf("%{") == 0){
						dfCode = dfCode.substring(2, dfCode.length() - 1);
						dfCode = TagUtils.getStack(this.pageContext, name);
						dfCode = (dfCode == null ? "" : dfCode);
					}
				}
				String dfName = "";
				if(dfCode != null && !"".equals(dfCode)){	
					dfName = DictUtil.getValueDesc(tenantId, dictId, dfCode);
				}
				// 显示数据
				stringBuilder.append("<input id=\"" + selName + "Name\"  name=\""+ this.name + "Name\" value=\""+dfName+"\" type=\"text\" ").append(" />");
				// code存放
				stringBuilder.append("<input ");
				initAttribute(stringBuilder);
				stringBuilder.append("id=\"" + selName + "\" name=\"" + this.name + "\" type=\"hidden\" value=\""+dfCode+"\" />");
				// 下拉按钮
				stringBuilder.append("<img id=\"" + selName + "Btn\" type=\"button\" class=\"searchSelBtn\" value=\"\" />").append("<script>").append("var " + selName + "Array = [");
				if ("TRUE".equalsIgnoreCase(nullOption)) {
					stringBuilder.append(" {\"code\":\"\",\"py\":\"\",\"name\":\"" + nullText + "\"},");
				}
				/* 处理数据字典 */
				JSONArray sysVos = initDatasrc();
				if (sysVos != null) {
					// 组合数据,生成[{"code":code,"py":py,"name":name},{"code":code,"py":py,"name":name}]}格式
					for (int i = 0; i < sysVos.size(); i++) {
						JSONObject jsonObject = sysVos.getJSONObject(i);
						stringBuilder.append("{\"code\":").append("\"" + jsonObject.getString("columnValue") + "\",").append("\"py\":")
							.append("\"" + Pinyin.getFirstPinyin(jsonObject.getString("columnDesc")) + "\",").append("\"name\":").append("\"" + jsonObject.getString("columnDesc") + "\"").append("},");
					}
				}
				stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("];");
				// 生成searchSelect
				stringBuilder.append("$(document).ready(function(){");
				stringBuilder.append("$(\"#" + selName + "\").searchSelect();");
				stringBuilder.append("});").append("</script>");
			} else {
				stringBuilder = new StringBuilder("<select ");
				initAttribute(stringBuilder);
				stringBuilder.append(">\n");
				String value = TagUtils.getStack(this.pageContext, name);
				if (value == null || "".equals(value)) {
					value = defaultValue;
					if (StringUtils.isNotEmpty(defaultValue) && value.indexOf("%{") == 0) {
						value = value.substring(2, value.length() - 1);
						value = TagUtils.getStack(this.pageContext, name);
						value = (value == null ? "" : value);
					}
				}
				if (parentValue.indexOf("%{") == 0) {
					parentValue = parentValue.substring(2, parentValue.length() - 1);
					parentValue = TagUtils.getStack(this.pageContext, name);
					parentValue = (parentValue == null ? "" : parentValue);
				}
				/* 处理数据字典 */
				if ("TRUE".equalsIgnoreCase(nullOption)) {
					stringBuilder.append("\t<option value=''>").append(nullText).append("</option>\n");
				}
				JSONArray sysVos = initDatasrc();
				StringBuilder strTemp = new StringBuilder();
				String strCode = "";
				if (sysVos != null) {
				    JSONObject jsonObject = new JSONObject();
					for (int i = 0; i < sysVos.size(); i++) {
						jsonObject = sysVos.getJSONObject(i);
						if ("TRUE".equalsIgnoreCase(displayCode)) {
							strCode = jsonObject.getString("columnValue") + "-";
						}
						if (jsonObject.getString("columnValue").equals(value)) {
							strTemp.append("\t<option value=\"").append(jsonObject.getString("columnValue")).append("\" selected>").append(strCode).append(jsonObject.getString("columnDesc")).append("</option>\n");
						} else {
							strTemp.append("\t<option value=\"").append(jsonObject.getString("columnValue")).append("\">").append(strCode).append(jsonObject.getString("columnDesc")).append("</option>\n");
						}
					}
				}
				stringBuilder.append(strTemp).append("</select>\n");
			}
			pageContext.getOut().print(stringBuilder.toString());
		} catch (Exception e) {
			LOGGER.error("出现异常，信息：", e);
		}
		return EVAL_PAGE;
	}

	private void appendAttribute(StringBuilder sb, String attrName, String attrValue) {
		if (attrValue != null && !"".equals(attrValue)) {
			sb.append(attrName);
			sb.append("='");
			sb.append(attrValue);
			sb.append("'");
		}
	}

	/**
	 * 初始化下拉框数据源
	 * 
	 * @return
	 */
	private JSONArray initDatasrc() {
		JSONArray vos = DictUtil.getSysParamS(tenantId, dictId);
		if(vos==null||vos.size()==0){
			return null;
		}
		
		/*Hashtable itemDict = CRMDict.getDeptItemDict(dictId, parentValue);
		if (itemDict == null) {
			return itemDict;
		}*/
		JSONArray _itemDict = new JSONArray();
		//JSONObject jsonObject = new JSONObject();
		if (StringUtils.isNotEmpty(keys)) {
			String[] _keys = keys.split(",");
			for (int i = 0; i < _keys.length; i++) {
				for(int j=0;j<vos.size();j++){
					JSONObject jsonObject = vos.getJSONObject(j);
					if(_keys[i].equals(jsonObject.getString("columnValue"))){
						_itemDict.add(jsonObject);
						break;
					}
					
				}
			}
		}else{
			_itemDict = vos;
		}

		if (StringUtils.isNotEmpty(filters)) {
			String[] _filters = filters.split(",");
			for (int i = 0; i < _filters.length; i++) {
				for(int j=0;j<_itemDict.size();j++){
					JSONObject jsonObject = _itemDict.getJSONObject(j);
					if(_filters[i].equals(jsonObject.getString("columnValue"))){
						_itemDict.remove(j);
						break;
					}
				}
			}
		}
		
		return _itemDict;
	}

	/**
	 * 初始化下拉框数据源
	 * 
	 * @return
	 */
	private void initAttribute(StringBuilder stringBuilder) {
		this.appendAttribute(stringBuilder, "id", id);
		this.appendAttribute(stringBuilder, "name", name);

		if ("TRUE".equalsIgnoreCase(multiple)) {
			stringBuilder.append(" multiple ");

		}
		if ("TRUE".equalsIgnoreCase(disabled)) {
			stringBuilder.append(" disabled ");

		}
		// 处理属性
		this.appendAttribute(stringBuilder, "size", size);
		this.appendAttribute(stringBuilder, "tabindex", tabindex);
		this.appendAttribute(stringBuilder, "class", styleClass);
		this.appendAttribute(stringBuilder, "style", style);
		this.appendAttribute(stringBuilder, "notnull", notnull);
		this.appendAttribute(stringBuilder, "keys", keys);
		this.appendAttribute(stringBuilder, "filters", filters);
		/* 处理事件 */
		this.appendAttribute(stringBuilder, "onfocus", onfocus);
		this.appendAttribute(stringBuilder, "onblur", onblur);
		this.appendAttribute(stringBuilder, "onchange", onchange);
		this.appendAttribute(stringBuilder, "onclick", onclick);
		this.appendAttribute(stringBuilder, "ondblclick", ondblclick);
		this.appendAttribute(stringBuilder, "onkeydown", onkeydown);
		this.appendAttribute(stringBuilder, "onkeypress", onkeypress);
		this.appendAttribute(stringBuilder, "onkeyup", onkeyup);
		this.appendAttribute(stringBuilder, "onmousedown", onmousedown);
		this.appendAttribute(stringBuilder, "onmousemove", onmousemove);
		this.appendAttribute(stringBuilder, "onmouseout", onmouseout);
		this.appendAttribute(stringBuilder, "onmouseover", onmouseover);
		this.appendAttribute(stringBuilder, "onmouseup", onmouseup);
	}

	@Override
	public void release() {
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getNullText() {
		return nullText;
	}

	public void setNullText(String nullText) {
		this.nullText = nullText;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getDisplayCode() {
		return displayCode;
	}

	public void setDisplayCode(String displayCode) {
		this.displayCode = displayCode;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextNode() {
		return nextNode;
	}

	public void setNextNode(String nextNode) {
		this.nextNode = nextNode;
	}

	public String getNotnull() {
		return notnull;
	}

	public void setNotnull(String notnull) {
		this.notnull = notnull;
	}

	public String getNullOption() {
		return nullOption;
	}

	public void setNullOption(String nullOption) {
		this.nullOption = nullOption;
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

	public String getOnfocus() {
		return onfocus;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
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

	public String getPreNode() {
		return preNode;
	}

	public void setPreNode(String preNode) {
		this.preNode = preNode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getTabindex() {
		return tabindex;
	}

	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
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

	/*@SuppressWarnings("rawtypes")
	class SortByNameAsc implements Comparator {
		public int compare(Object a, Object b) {
			return ((TSysParamVo) a).getColumnDesc().compareTo(
					((TSysParamVo) b).getColumnDesc());
		}
	}

	@SuppressWarnings("rawtypes")
	class SortByCodeAsc implements Comparator {
		public int compare(Object a, Object b) {
			return ((TSysParamVo) a).getColumnValue().compareTo(
					((TSysParamVo) b).getColumnValue());
		}
	}

	@SuppressWarnings("rawtypes")
	class SortByNameDesc implements Comparator {
		public int compare(Object a, Object b) {
			return ((TSysParamVo) b).getColumnDesc().compareTo(
					((TSysParamVo) a).getColumnDesc());
		}
	}

	@SuppressWarnings("rawtypes")
	class SortByCodeDesc implements Comparator {
		public int compare(Object a, Object b) {
			return ((TSysParamVo) b).getColumnValue().compareTo(
					((TSysParamVo) a).getColumnValue());
		}
	}*/

	/*public static void main(String[] args) {
		System.out.println("集团关键人修改1".compareToIgnoreCase("集团关键人修改2"));
	}*/

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getSearchSelect() {
		return searchSelect;
	}

	public void setSearchSelect(String searchSelect) {
		this.searchSelect = searchSelect;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
