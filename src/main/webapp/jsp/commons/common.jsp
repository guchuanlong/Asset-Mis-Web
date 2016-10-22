<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String _bathPath = request.getContextPath();
	request.setAttribute("_bathPath", _bathPath);
    request.setAttribute("_base", _bathPath);
    request.setAttribute("_template_path", _bathPath + "/template/default");
    
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setHeader("Pragma", "No-cache");
	request.setAttribute("requestUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getRequestURI());
	
	String fullPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getContextPath();
%>
<script>
	var fullPath = "<%=fullPath%>";
	var _bathPath = "<%=_bathPath%>";
	var _base = "${_base}";
</script>

<!-- 时间插件 -->
<%-- <link type="text/css" rel="stylesheet" href="${_bathPath}/resources/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
<script type="text/javascript" src="${_bathPath}/resources/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${_bathPath}/resources/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
 --%>
<script type="text/javascript" src="${_bathPath}/template/default/scripts/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="${_bathPath}/template/default/scripts/utils.js"></script>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>