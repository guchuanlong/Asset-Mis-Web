<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title><sitemesh:write property='title' />-亚信在线宽带</title>
<meta name="description" content="app, web app, responsive, responsive layout, admin, admin panel, admin dashboard, flat, flat ui, ui kit, AngularJS, ui route, charts, widgets, components" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<%@ include file="include/common.jsp" %>
<%-- <script type="application/javascript" src="${_fp_context_path}/scripts/myxapp.template.js"></script> --%>
<sitemesh:write property='head' />

</head>
<body>
    <!-- header start-->
    <%@ include file="/template/default/include/header.jsp"%>
    <!-- header end-->
	<!-- APP FUNCTION AREA start -->
	<div class="box">
		
			<sitemesh:write property="body"/>
		
	</div>

	<!-- APP FUNCTION AREA end -->
     
    <!-- header start-->
    <%@ include file="/template/default/include/footer.jsp"%>
    <!-- header end-->
    	
	

</body>
</html>
