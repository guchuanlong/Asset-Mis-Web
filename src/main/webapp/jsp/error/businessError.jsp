<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width; initial-scale=0.8;  user-scalable=0;" />
    <title>sorry~</title>
    <link href="<%=request.getContextPath()%>css/font-awesome.css" type="text/css" rel="stylesheet" />
	<link href="<%=request.getContextPath()%>css/css.css" type="text/css" rel="stylesheet" />
</head>

<body>
<div style="display: none;">
		<%
			Object exceptionObj = request.getAttribute("ex");
			if (exceptionObj != null) {
				if (exceptionObj instanceof com.myunihome.myxapp.web.system.exception.BusiException) {
		%>
		<c:if test="${!empty  ex.code}">
			<p>code:${ex.code}</p>
		</c:if>
		<c:if test="${!empty  ex.detail}">
			<p>detail:${ex.detail}</p>
		</c:if>
		<%
			}
		%>
		<c:if test="${!empty  ex.message}">
			<p>message:${ex.message}</p>
		</c:if>
		<%
			} else {
		%>
		<p>由于网络或其他原因，请稍候再试~</p>
		<%
			}
		%>
	</div>
 <div class="w_404">
  <div class="w_404_nr">
  <img src="<%=request.getContextPath()%>/images/500.png">
  </div>
 </div>
</body>
</html>
