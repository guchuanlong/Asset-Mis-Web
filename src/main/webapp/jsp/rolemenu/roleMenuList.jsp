<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="roleMenuInfo" style="display: none;">
	<div class="query_list query_list_k_paad">
		<div class="query_title">角色查询</div>
		<!--外侧白色背景框架-->
		<div class="query_list_main">
		<!-- 数据区域 -->
			<div id=menuList_div class="list">
				<table width="100%" border="0">
					<thead>
						<tr class="tr_bj">
							<td>角色ID</td>
							<td>角色名称</td>
							<td>菜单ID</td>
							<td>菜单名称</td>
							<td>生效时间</td>
							<td>失效时间</td>
							<td>操作</td>
						</tr>
					</thead>
					<tbody id="roleMenuListData"></tbody>
				</table>
				<div class="tishia">
					<p><img src="${_template_path}/images/this1.png"></p>
					<span>请选择角色，查看菜单相关信息！</span>
				</div>
				<div id="tishia" class="tishia" style="display: none;">
					<p><img src="${_template_path}/images/this2.png"></p>
					<span>sorry，没有找到该角色相关菜单!</span>
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
	</div>
</div>

<!-- 定义JsRender模版 -->
<script id="roleMenuListDataTmpl" type="text/x-jsrender">
	<tr>
		<td>{{:roleId}}</td>
		<td>{{:roleName}}</td>
		<td>{{:menuId}}</td>
		<td>{{:menuName}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
		<td><a href="javascript:void(0);" onclick="delRoleMenu({{:roleMenuRelId}})"><i class="icon-remove"></i>删除</a></td>
	</tr>
</script>

<script type="text/javascript">

//刷新列表数据信息
function refreshRoleMenuList() {
	//显示列表并修改title
	$("#roleMenuInfo").show();
	var roleName = $("#roleList_div").find("input[name='radio_roleId']:checked").attr("value_rn");
	$("#roleMenuInfo").find(".query_title").text("角色［" + roleName + "］对应的菜单列表");
	//查询数据并展示在页面
		 
	    $("#roleMenuInfo").find("#pagination-demo").runnerPagination({
 			url: "${_bathPath}/roleMenuManage/queryRoleMenuList",
 			method: "POST",
 			dataType: "json",
            showWait: true,
            data: {
                "roleId": $.trim($("input[name='radio_roleId']:checked").val())
            },
           	pageSize: 10,
           	visiblePages:5,
            message: "正在为您查询数据..",
            render: function (data) {
        	    if(data != null && data != 'undefined' && data.length>0){
    	        	$("#roleMenuInfo").find(".tishia").hide();
    	        	$("#roleMenuInfo").find("#tishia").hide();
    	        	var template = $.templates("#roleMenuListDataTmpl");
    	        	var htmlOutput = template.render(data);
    	        	$("#roleMenuListData").html(htmlOutput);
    	        }else{
            		$("#roleMenuInfo").find(".tishia").hide();
            		$("#roleMenuInfo").find("#tishia").show();//显示无数据提示
    	        	$("#roleMenuListData").html(null);
            		$("#roleMenuInfo").find('#pagination-demo').empty();
    	        }
            }
 		});
}

/**
 * 删除角色菜单关系
 */
function delRoleMenu(roleMenuRelId){
	messageController.confirm("确认要删除该角色菜单关系吗？", function(){
		var url = "${_bathPath}/roleMenuManage/delRoleMenu";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	        	 "roleMenuRelId": roleMenuRelId
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	messageController.alert(data.statusInfo, function(){
		        	//刷新列表
	        		refreshRoleMenuList();
	        	});
	        }
	    });
	});
}
</script>
