<%@ page contentType="text/html; charset=UTF-8" language="java"%>
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
<div id="queryTip" class="tishia">
	<p>
		<img src="${_template_path}/images/this1.png">
	</p>
	<span>请点击查询，查看相关信息！</span>
</div>
<div id="noDataTip" class="tishia" style="display: none;">
	<p>
		<img src="${_template_path}/images/this2.png">
	</p>
	<span>sorry,没有找到符合你要求的信息!</span>
</div>