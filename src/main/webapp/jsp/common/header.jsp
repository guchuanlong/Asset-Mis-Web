<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="ai" uri="/WEB-INF/tag/ai-tags.tld" %>
<%@ page import="com.myunihome.myxapp.utils.web.model.UserLoginVo" %>
<!DOCTYPE html>
<%  UserLoginVo user=(UserLoginVo) request.getSession().getAttribute("user_login"); %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>宽带在线</title>


	<!--列表页查询按钮-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/business/search.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.md5.js"></script>
</head>
<body>
<div class="herder_top_big">
   <div class="herder_top">
   <ul>
     <li class="code_l"><i class="icon-user"></i><a href="#">${sessionScope.user_login.staffName}</a> ｜
     <p style="display:none">
     <span class="code"><img src="${_bathPath}/images/code_jiao.png"></span>
     <span><a href="${_bathPath}/index/index">个人中心</a></span>
     <span><a href="javascript:void(0);" onclick="showpwd()">修改密码</a></span>
    </p>
   </li>
   <li><p><a href="${_bathPath}/logout">退出</a></p></li>
  <li class="right" id="configMenu">
 
   </li>
   </ul>
   </div>

</div>

<div class="header1" style=" position:relative;">
 <div class="head1">
  <div class="logo1"><a href="#"><img src="${_bathPath}/images/logo.png"  /></a></div>
  <div class="nav1">
   <div class="nava1">
   <div class="yax1" style="width:140px;">
    <p>
     <img src="${_bathPath}/images/i_city.png"><span><%=user.getTenantName() %></span>
     <i style="display:none;" class="icon-caret-down"></i>
    </p>
    <ul style="display:none;">
     <li><a href="#">世通宽带</a></li>
     <li><a href="#">长城网络</a></li>
     <li><a href="#">歌华有线</a></li>
    </ul>
   </div>
   
   </div>
   <div class="naver1">
   <div class="naver_t1 ">
    <ul id="menu" style="margin: 0;">
  
    </ul>
    
   </div>
   </div>
  </div>
 </div>
</div>
<div class="tanc_cz" id="modifypwd" style="display:none;z-index:12;position:fixed;">
	<div class="tanc_bg"></div>
	<div class="qux">
		<div class="qux_title">
			<p>修改密码</p>
			<i class="icon-remove" onclick="hidepwd()"></i>
		</div>
		<div class="box_form">
		  <ul>
		   <li class="yiban" style="width: 100%;"><span class="er_name" style="width: 200px;"><i class="x_red">*</i>原始密码：</span><span><input type="password" id="oldPwd" class="box_input" onblur="verifypwd();"><span style="display: none;margin-left: 5px;" id="oldpwd"></span></span><label style="font-size:12px;padding-left: 200px;"
							id="oldPwd_error"></label></li> 
		   <li class="yiban" style="width: 100%;"><span class="er_name" style="width: 200px;"><i class="x_red">*</i>新密码：</span><span><input type="password" id="newPwd" class="box_input"><i class="lan" id="lit_tip">请输入6-11位</i></span><label style="font-size:12px;padding-left: 200px;"
							id="newPwd_error"></label></li>
		   <li class="yiban" style="width: 100%;"><span class="er_name" style="width: 200px;"><i class="x_red">*</i>确认新密码：</span><span><input type="password" id="con_newPwd" class="box_input"></span><label style="font-size:12px;padding-left: 200px;"
							id="conPwd_error"></label></li>
		  </ul>
		 </div>
		<div class="tanc_qx" style="margin-top: 0;">
			<ul>
				<li><button class="btn sc1" onclick="modifypwd()">确认</button></li>
				<li><button class="btnh sc1" onclick="hidepwd()">关闭</button></li>
			</ul>
		</div>
	</div>
</div>
<script type="text/javascript" language="JavaScript">
var myScrollTop=function()  //获得当前窗口显示区顶点位置
{
        return document.documentElement.scrollTop?document.documentElement.scrollTop:document.body.scrollTop;
 };
       
function mymovemenu()  // 随着窗口的拉动，实时调整广告在窗口中的位置
{
        document.getElementById("xuanfu").style.top=myScrollTop()+60+'px';document.getElementById("xuanfu").style.right=3+'px';setTimeout("mymovemenu();",10);
}
</script>



</body>
</html>
<script>


$(document).ready(function() {
	getmenu();
	
	//getConfgMenu();
});



function showpwd(){
	$("#modifypwd").show();
}

function hidepwd(){
	$("#modifypwd").hide();
	$("#oldpwd").hide("");
	$("#oldPwd").val("");
	$("#newPwd").val("");
	$("#con_newPwd").val("");
	$("#oldpwd").text("");
}

function modifypwd(){
	if(checkPwd()){
		if(verifypwd()){
			var url = "${_bathPath}/index/modifyPwd";
			ajaxController.ajax({
		        method: "POST",
		        url: url,
		        dataType: "json",
		        showWait: true,
		        async: false,
		        data: {
		            "oldPwd":$.md5($.trim($("#oldPwd").val())),
		            "newPwd":$.md5($.trim($("#newPwd").val()))
		        },
		        message: "正在加载数据..",
		        success: function (data) {
		        	if(data!=null&&data!="undefined"){
		        		if(data.statusCode=='1'){
		        			bootbox.alert(data.statusInfo);
		        			hidepwd();
		        		}else{
		        			bootbox.alert(data.statusInfo);
		        		}
		        	}
		        }
		    });
		}else{
			bootbox.alert("原始密码错误");
		}
	}
}

function verifypwd(){
	var res = false;
	if($.trim($("#oldPwd").val())!=""){
		$('#oldPwd_error').html("");
		var url = "${_bathPath}/index/verifyPwd";
		ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        async: false,
	        data: {
	            "oldPwd":$.md5($.trim($("#oldPwd").val())),
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	if(data!=null&&data!="undefined"){
	        		if(data.data=='1'){
	        			$("#oldpwd").text(data.statusInfo);
	        			$("#oldpwd").css("color","green");
	        			$("#oldpwd").show();
	        			res = true;
	        		}else{
	        			$("#oldpwd").text(data.statusInfo);
	        			$("#oldpwd").css("color","red");
	        			$("#oldpwd").show();
	        		}
	        	}
	        }
	    });
	}
	return res;
}

function checkPwd(){
	var oldPwd = $.trim($("#oldPwd").val());
	var newPwd = $.trim($("#newPwd").val());
	var conPwd = $.trim($("#con_newPwd").val());
	if (oldPwd == "") {
		$('#oldPwd_error').html('请输入密码');
		return false;
	} else {
		$('#oldPwd_error').html("");
	}
	
	if (newPwd == "") {
		$('#newPwd_error').html('请输入密码');
		return false;
	} else if(newPwd.length>11||newPwd.length<6){
		$('#newPwd_error').html('密码长度需不少于6位，不超过11位');
		return false;
	}else {
		$('#newPwd_error').html("");
	}
	
	if (conPwd == "") {
		$('#conPwd_error').html('请输入确认密码');
		return false;
	} else {
		$('#conPwd_error').html("");
	}
	
	if(newPwd!=conPwd){
		bootbox.alert("密码和确认密码必须一致");
		return false;
	}
	
	return true;
}

function getConfgMenu()
{
	$.ajax({
		url : '${_bathPath}/menu/getChannelMenu',
		type : "post",
		async : true,
		dataType : "html",
		timeout : "10000",
		error : function() {
			alert("服务加载出错");
		},
		success : function(data) {
			var json = eval(data);
			var str="";
			$.each(json, function(index, item) {
				//循环获取数据    
				$("#configMenu").append(" <p><a href="+item.menuUrl+">"+item.menuName+"</a>｜</p>");
			});	
		}
	});

}


function getmenu(){
	$.ajax({
		url : '${_bathPath}/menu/getMenu',
		type : "post",
		async : true,
		dataType : "html",
		timeout : "10000",
		error : function() {
			alert("服务加载出错");
		},
		success : function(data) {
			var json = eval(data);
			var str="";
			
			var i=0;
			$.each(json, function(index, item) {
				var ol="";
				//循环获取数据    
				var menuId = json[index].menuId;
				var left=i*141;
				i++;
				//$('#menu').html("<li>");
				//$('#menu').append("<a href='"+json[index].menuUrl+"'>"+json[index].menuName+"</a>");
				var obj=json[index].menuList;
				
				var hasthreed=false;
				$.each(obj,function(index,item){
					var thred=obj[index].menuList;
					if(thred!="")
						{
						hasthreed=true;
						return;
						}
				});
				if(hasthreed)
					{
						str+=" <li class='"+json[index].menuPic+"'><p><a href=''#'>"+json[index].menuName+"</a></p>";
					    str+= "<div class='ther1' style='display:none;'><img src='${_bathPath}/images/top_1.png'></div>";
					    str+= "<div class='ther' style='left:-"+left+"px; display:none;'>";
					    str+=  "<ul>";
					}
				else
					{
					str+=" <li class='"+json[index].menuPic+"'><p><a href=''#'>"+json[index].menuName+"</a></p>";
				    str+= "<div class='ther1' style='display:none;'><img src='${_bathPath}/images/top_1.png'></div>";
				    str+= "<div class='ther' style='left:-"+left+"px; display:none;'>";
				    str+=  "<ol style='width:700px;'>";
					}
					
				//$('#menu').append("<dl style='display:none;'><i class='icon-play'></i>");
				$.each(obj,function(index,item){
					var thred=obj[index].menuList;
					if(thred=='')
						{
					str+="<li><a onmouseover=show('a') href='${_bathPath}/"+obj[index].menuUrl+"'>"+obj[index].menuName+"</a></li>";
						}
					else
						{
						str+="<li><a onmouseover='show("+obj[index].menuId+")'>"+obj[index].menuName+"</a><span class='icon-angle-right'></span></li>";
							ol+=" <ol  class='menu' style='width:320px;display:none' id="+obj[index].menuId+">";
						}
					$.each(thred,function(index,item){
						ol+="<li><a href='${_bathPath}/"+thred[index].menuUrl+"'>"+thred[index].menuName+"</a></li>";
					});
					ol+="</ol>"
				});
					
				if(hasthreed)
					{
					str+="</ul>";
						str+=ol;
					str+="</div></li>";
					}
				else{
				str+="</ol></div></li>";
				}
				
			});	
			$('#menu').append(str);
			
			 $(".erji").mouseover(function () {
				  $(".erji .ther").show(0);
				  $(".erji .ther1").show(0);
				  $(".erji p a ").removeClass("reorder");
				  $(".erji p a").addClass("remove");
				  });
			   $(".erji").mouseout(function () {
				  $(".erji .ther").hide(0);
				  $(".erji .ther1").hide(0);
				  $(".erji p a").removeClass("remove");
				  $(".erji p a").addClass("reorder");
				  });
			   
			   
			   $(".erji1").mouseover(function () {
					  $(".erji1 .ther").show(0);
					  $(".erji1 .ther1").show(0);
					  $(".erji1 p a ").removeClass("reorder");
					  $(".erji1 p a").addClass("remove");
					  });
				   $(".erji1").mouseout(function () {
					  $(".erji1 .ther").hide(0);
					  $(".erji1 .ther1").hide(0);
					  $(".erji1 p a").removeClass("remove");
					  $(".erji1 p a").addClass("reorder");
					  });
			   
			   
				   $(".erji2").mouseover(function () {
						  $(".erji2 .ther").show(0);
						  $(".erji2 .ther1").show(0);
						  $(".erji2 p a ").removeClass("reorder");
						  $(".erji2 p a").addClass("remove");
						  });
					   $(".erji2").mouseout(function () {
						  $(".erji2 .ther").hide(0);
						  $(".erji2 .ther1").hide(0);
						  $(".erji2 p a").removeClass("remove");
						  $(".erji2 p a").addClass("reorder");
						  });
					   
					   $(".erji3").mouseover(function () {
							  $(".erji3 .ther").show(0);
							  $(".erji3 .ther1").show(0);
							  $(".erji3 p a ").removeClass("reorder");
							  $(".erji3 p a").addClass("remove");
							  });
						   $(".erji3").mouseout(function () {
							  $(".erji3 .ther").hide(0);
							  $(".erji3 .ther1").hide(0);
							  $(".erji3 p a").removeClass("remove");
							  $(".erji3 p a").addClass("reorder");
							  });
						   
						   $(".erji4").mouseover(function () {
								  $(".erji4 .ther").show(0);
								  $(".erji4 .ther1").show(0);
								  $(".erji4 p a ").removeClass("reorder");
								  $(".erji4 p a").addClass("remove");
								  });
							   $(".erji4").mouseout(function () {
								  $(".erji4 .ther").hide(0);
								  $(".erji4 .ther1").hide(0);
								  $(".erji4 p a").removeClass("remove");
								  $(".erji4 p a").addClass("reorder");
								  });
		}
	});
}


function show(id)
{
	$('.menu').hide();
	$('#'+id).show();
}

$(document).ready(function(){
	$('.flexslider').flexslider({
		directionNav: true,
		pauseOnAction: false
	});
});
</script>