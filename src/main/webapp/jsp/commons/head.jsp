<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<script type="text/javascript">
$(document).ready(function () {
	$("#back").click(function () { 
		history.go(-1);
	});
	   /* $("#xs").onmouse(function(){ 
		document.getElementById( "cj").style.display= "block";
		
	 }); */   
	 
});
</script> 
<script id="menuDataTmpl" type="text/x-jsrender">
	<li><a class="{{:menuPic}}" href="${_base}/{{:menuUrl}}">{{:menuName}}</a></li>
 </script>
   <!----头部---->
<div class="header">
 <div class="head_wai">
  <div class="head">
   <div class="logo"><a href="${_base}"><Img src="${_base}/resources/style_resources/images/logo.png" /></a></div>
   <div class="nav">
   <div class="nav_list">
    <ul id="data">
    </ul>
   </div>
   <div class="user_name">
	    <ul>
	      <div class="mor"><a href="#">${sessionScope.user_session_key.staffName}</a><a href="${_base}/logout">登出</a></div>
	    </ul>
    </div>
   </div>
  </div>
 </div>
</div>

<!--中间主体-->

<div class="fanhui">
 <ul><p id="back"><i class="icon-arrow-right"></i>返回上级</p></ul>
</div>
<div class="top_banner"></div>

<script>
    $(document).ready(function () {
        var url = "${_base}/menu/all"
        ajaxController.ajax({
            method: "POST",
            url: url,
            dataType: "json",
            showWait: true,
            message: "正在加载数据..",
            success: function (data) {
                var jsonData = data.data;//转换为json对象;
                var template = $.templates("#menuDataTmpl");
                var htmlOutput = template.render(jsonData);
                $("#data").html(htmlOutput);
            }
        }); //end of ajax
    });
</script>