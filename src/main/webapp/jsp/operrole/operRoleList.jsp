<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="operRoleInfo" style="display: none;">
	<div class="query_list query_list_k_paad">
		<div class="query_title">账号对应角色列表</div>
		<!--外侧白色背景框架-->
		<div class="query_list_main">
			<!-- 数据区域 -->
			<div id=staffnoInfo class="list">
				<table width="100%" border="0">
					<thead>
						<tr class="tr_bj">
							<td>角色ID</td>
							<td>角色名称</td>
							<td>生效时间</td>
							<td>失效时间</td>
							<td>操作</td>
						</tr>
					</thead>
					<tbody id="operRoleListData"></tbody>
				</table>
				<div class="tishia">
					<p><img src="${_bathPath}/images/this1.png"></p>
					<span>请选择账号，查看角色相关信息！</span>
				</div>
				<div id="tishia" class="tishia" style="display: none;">
					<p><img src="${_bathPath}/images/this2.png"></p>
					<span>sorry,没有找到该账号相关角色!</span>
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
<script id="operRoleListDataTmpl" type="text/x-jsrender">
	<tr>
		<td>{{:roleId}}</td>
		<td>{{:roleName}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
		<td><a href="javascript:void(0);" onclick="delOperRole({{:operRoleRelId}})"><i class="icon-remove"></i>删除</a></td>
	</tr>
</script>

<script type="text/javascript">

//刷新列表数据信息
function refreshOperRoleList() {
	//展现角色区域
	$("#operRoleInfo").show();
	//改变标题
	var operCode = $("#staffnoInfo").find("input[name='radio_staffnoId']:checked").attr("value");
	$("#operRoleInfo").find(".query_title").text("账号["+operCode+"]对应角色列表");
	//查询数据并展示在页面
	$("#operRoleInfo").find("#pagination-demo").runnerPagination({
    		url: "${_bathPath}/operRoleManage/queryOperRoleList",
    		method: "POST",
    		dataType: "json",
    	    showWait: true,
    	    async: false,
    	    data: {
    	    	"staffnoId": function () {
                    return $("input[name='radio_staffnoId']:checked").val();
                },
				"random":Math.random()
    	    },
    	   	pageSize: 5,
    	   	visiblePages:5,
    	    message: "正在为您查询数据..",
    	    render: function (data) {
    	        if(data != null && data != 'undefined' && data.length>0){
    	        	$("#operRoleInfo").find(".tishia").hide();
    	        	$("#operRoleInfo").find("#tishia").hide();
    	        	var template = $.templates("#operRoleListDataTmpl");
    	        	var htmlOutput = template.render(data);
    	        	$("#operRoleListData").html(htmlOutput);
    	        }else{
            		$("#operRoleInfo").find(".tishia").hide();
            		$("#operRoleInfo").find("#tishia").show();//显示无数据提示
    	        	$("#operRoleListData").html(null);
            		$("#operRoleInfo").find('#pagination-demo').empty();
    	        }
    	    }
    	});
	
}

//查询员工账号列表
function getOperRoleList(currentPage) {
    var jsonData;
    var url = "${_bathPath}/operRoleManage/queryOperRoleList";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
        	"currentPage": currentPage,
            "operId": function () {
                return $("input[name='radio_operId']:checked").val();
            }
        },
        message: "正在加载数据...",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}

/**
 * 删除账号角色关系
 */
function delOperRole(operRoleRelId){
	messageController.confirm("确认要删除该角色吗？", function(){
		var url = "${_bathPath}/operRoleManage/delOperRole";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	        	 "operRoleRelId": operRoleRelId
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	messageController.alert(data.statusInfo, function(){
		        	//刷新列表
	        		refreshOperRoleList();
	        	});
	        }
	    });
	});
}
</script>
