package com.myunihome.myxapp.web.system.util;

import javax.servlet.jsp.PageContext;

import org.springframework.util.StringUtils;

public final class TagUtils {
	
	private TagUtils(){}
	
	public static String getStack(PageContext pageContext, String expr) {
		 String pageValue = pageContext.getAttribute(expr)==null?null:pageContext.getAttribute(expr).toString();
		 if(StringUtils.isEmpty(pageValue)){
			 pageValue = pageContext.getRequest().getAttribute(expr)==null
					 ?null:pageContext.getRequest().getAttribute(expr).toString(); 
		 }
		 if(StringUtils.isEmpty(pageValue)){
			 pageValue = pageContext.getSession().getAttribute(expr)==null?null
					 :pageContext.getSession().getAttribute(expr).toString(); 
		 }
		 return pageValue;
	}
}
