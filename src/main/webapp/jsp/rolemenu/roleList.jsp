<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />

<html>
<head>
	<%@ include file="/jsp/commons/common.jsp"%>
	<title>权限管理</title>
</head>

<body>
<!----2.中部开始---->
<div class="main_right" id="roleList_div">
	<div class="main_right_Title">
		<p>权限管理</p>
	</div>
	<!--查询列表区域-->
	<div class="col-xs-12" >
		<div class="query_list query_list_k_paad" id="roleMenu_roleList">
			<div class="query_title">角色查询</div>
			<!--外侧白色背景框架-->
			<div class="query_list_main">
				<!--查询区域-->
				<div class="query_duan">
					<div class="query_last">
						<ul>
							<li>
								<p class="new_xinz">
									<a href="javascript:addMenuToRole();">赋菜单权限</a>
								</p>
							</li>
						</ul>
					</div>
					<div class="query_qeint">
						<ul>
							<li class="jig">角色名称：</li>
							<li>
								<input id="role_name" type="text" class="query_input form-control">
							</li>
							<li class="query_btn">
								<a href="javascript:void(0)" id="searchRoleData" onclick="searchRoleData()">查询</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="list">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr class="tr_bj">
							<td>选择</td>
							<td>角色ID</td>
							<td>角色名称</td>
							<td>角色描述</td>
							<td>生效时间</td>
							<td>失效时间</td>
						</tr>
						<tbody id="roleData"></tbody>
					</table>
					<div class="tishia">
						<p>
							<img src="${_template_path}/images/this1.png">
						</p>
						<span>请点击查询，查看角色相关信息！</span>
					</div>
					<div id="tishia" class="tishia" style="display: none;">
						<p>
							<img src="${_template_path}/images/this2.png">
						</p>
						<span>sorry,没有找到符合你要求的角色!</span>
					</div>
				</div>
				<!-- 分页信息 -->
				<div>
					<nav style="text-align:center">
						<ul id="pagination-demo">
						</ul>
					</nav>
				</div>
				
			</div>
		</div>
			<!-- 角色拥有菜单列表 begin -->
			<%@ include file="/jsp/rolemenu/roleMenuList.jsp"%>
			<!-- 角色拥有菜单列表  end  -->
	</div>
</div>

<!-- 新增、修改、详情窗口  end  -->
<!-- 赋菜单权限窗口 begin -->
<%@ include file="/jsp/rolemenu/addMenuToRoleView.jsp"%>
<!-- 赋菜单权限窗口  end  -->
<!----2.中部结束---->

<!-- 定义JsRender模版 -->
<script id="roleListDataTmpl" type="text/x-jsrender">
	<tr>
		<td><input type="radio"  name="radio_roleId" onclick="refreshRoleMenuList()" value="{{:roleId}}" value_rn="{{:roleName}}" systemModeId="{{:systemModeId}}"></td>
		<td>{{:roleId}}</td>
		<td>{{:roleName}}</td>
		<td>{{:roleDesc}}</td>	
		<td>{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
	</tr>
</script>

<script type="text/javascript" >
$(document).ready(function () {
	//自动刷新角色数据
	refreshRoleData(1);
});

//查询按钮
var isSearchFlag = true;
function searchRoleData() {
	if(isSearchFlag){
		isSearchFlag = false;
		var roleId = jQuery.trim($("#role_id").val());
    	if(isNaN(roleId)){
    		messageController.alert("角色号必须全是数字！");
    		$("#role_id").val(null);
    		isSearchFlag = true;
    		return false;
    	}
    	refreshRoleData();
        isSearchFlag = true;
	}
}

//刷新列表数据、处理分页信息
function refreshRoleData() {
	 //隐藏角色菜单
	 $("#roleMenuInfo").hide();
	
	//查询数据并展示在页面
	    $("#roleMenu_roleList").find("#pagination-demo").runnerPagination({
 			url: "${_bathPath}/roleManage/queryRoleList",
 			method: "POST",
 			dataType: "json",
            showWait: true,
            data: {
                "roleName":  $.trim($("#role_name").val()),
            },
           	pageSize: 5,
           	visiblePages:5,
            message: "正在为您查询数据..",
            render: function (data) {
                //隐藏角色菜单
	            $("#roleMenuInfo").hide();
	            if(data != null && data != 'undefined' && data.length>0){
    	        	$("#roleMenu_roleList").find(".tishia").hide();
    	        	$("#roleMenu_roleList").find("#tishia").hide();
    	        	var template = $.templates("#roleListDataTmpl");
    	        	var htmlOutput = template.render(data);
    	        	$("#roleData").html(htmlOutput);
    	        }else{
            		$("#roleMenu_roleList").find(".tishia").hide();
            		$("#roleMenu_roleList").find("#tishia").show();//显示无数据提示
    	        	$("#roleData").html(null);
            		$('#pagination-demo').empty();
    	        }
            }
 		});
}


//获取角色列表
function getRoleData(currentPage) {
    var jsonData;
    var url = "${_bathPath}/roleManage/queryRoleList";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "currentPage": currentPage,
            "roleName": function () {
                return jQuery.trim($("#role_name").val())
            }
        },
        message: "正在加载数据..",
        success: function (data) {
            jsonData = data.data;
        }
    }); 
    return jsonData;
}

//角色删除
function delRole() {
	messageController.confirm("确认要删除该角色吗？", function(){
		var roleId = $("input[name='radio_roleId']:checked").val();
		var url = "${_bathPath}/roleManage/delRoleInfo";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	        	 "roleId": roleId
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	messageController.alert(data.statusInfo, function(){
		        	//刷新列表
	        		searchRoleData();
	        	});
	        }
	    });
	});
}
</script>

</body>
</html>