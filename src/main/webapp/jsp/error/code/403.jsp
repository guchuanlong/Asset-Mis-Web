<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width; initial-scale=0.8;  user-scalable=0;" />
    <title>403</title>
    
     <link href="<%=request.getContextPath()%>/css/main.css" rel="stylesheet" type="text/css">
</head>

<body>
   
     <div class="container"><!--中间主体-->
    <div class="w-400">
    <div class="login-400"><img src="<%=request.getContextPath()%>/images/403.png"></div>
     <ul>
      <li>被拒绝了，这个页面打不开...</li>
      <li><span>●请确认您所拥有的访问权限。</span></li>
     </ul>
    </div>
   </div><!--中间主体结束-->
</body>
</html>
