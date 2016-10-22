<%@page import="java.util.Map"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String _base = request.getContextPath();
    request.setAttribute("_base", _base);
    request.setAttribute("_bathPath", _base);
    request.setAttribute("_template_path", _base + "/template/default");
    
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setHeader("Pragma", "No-cache");
	request.setAttribute("requestUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getRequestURI());
	String fullPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getContextPath();
    
%>
<script>
    var _base = "${_base}";
    var _template_path = "${_template_path}";
</script>
<link href="${_template_path}/css/bootstrap.css" type="text/css" rel="stylesheet" />
<link href="${_template_path}/css/font-awesome.css" type="text/css" rel="stylesheet" />
<link href="${_template_path}/css/css.css" type="text/css" rel="stylesheet" />
<link href="${_template_path}/css/main.css" type="text/css" rel="stylesheet" />
<link href="${_template_path}/scripts/ajaxhelper/css/jquery.pagcontroller.css" rel="stylesheet"/>
<script src="${_template_path}/scripts/jquery/jquery-1.11.3.min.js"></script>
<script src="${_template_path}/scripts/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${_template_path }/scripts/jsrender/jsrender.min.js"></script>
<script src="${_template_path }/scripts/jsviews/jsviews.min.js"></script>
<script src="${_template_path }/scripts/bootbox/bootbox.js"></script>
<script src="${_template_path }/scripts/form/jquery.form.min.js"></script>
<script src="${_template_path }/scripts/ajaxhelper/jquery.pagcontroller.js"></script>
<script src="${_template_path }/scripts/json2/json2.js"></script>
<script src="${_template_path }/scripts/twbs-pagination/jquery.twbsPagination.min.js"></script>
<script src="${_template_path }/scripts/runnerpagination/runner.pagination.js"></script>
<script src="${_template_path}/scripts/My97DatePicker/WdatePicker.js"></script>
<script src="${_template_path}/scripts/utils.js"></script>
<script src="${_template_path}/scripts/jquery.flexslider-min.js"></script>
