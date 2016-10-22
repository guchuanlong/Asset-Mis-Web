<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="addMenuToRole_div" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
	<input name="roleId" type="hidden">
	<input name="roleName" type="hidden">
	<div class="tanc_bg"></div>
	<div class="tanc_nr">
		<div class="tanc_gb">
			<a href="javascript:void(0);"><img src="${_template_path }/images/close2.png"></a>
		</div>
		<div class="page-header">
           	<h4>赋菜单权限</h4>
       	</div>
		<div class="query_qeint">
			<ul>
				<li>菜单名称：</li><li><input type="text" class="query_input" id="serch_menuName"></li>
			    <li class="query_btn">
					<a href="javascript:searchAddMenuToRoleData()">查询</a>
				</li>
			</ul>
		</div>
		<div class="table_tc">
			<table border="0" width="100%">
		       	<tr class="tc_tr">
			         <td height="35" align="center" valign="middle">选择</td>
			         <td height="35" align="center" valign="middle">菜单ID</td>
			         <td height="35" align="center" valign="middle">菜单名称</td>
			         <td height="35" align="center" valign="middle">菜单URL</td>
			         <td height="35" align="center" valign="middle">生效时间</td>
			         <td height="35" align="center" valign="middle">失效时间</td>
		        </tr>
		        <tbody id="addMenuToRoleListData"></tbody>
			</table>
		    <div class="tishia" style="padding-left: 240px;">
				<p>
					<img src="${_template_path}/images/this1.png">
				</p>
				<span>请点击查询，查看菜单相关信息！</span>
			</div>
			<div id="tishia" class="tishia" style="display: none; padding-left: 240px;">
				<p>
					<img src="${_template_path}/images/this2.png">
				</p>
				<span>sorry,没有找到符合你要求的菜单!</span>
			</div>
			<!-- 分页信息 -->
			<div>
				<nav style="text-align:center">
					<ul id="pagination-demo">
					</ul>
				</nav>
			</div>
	 
		</div>
		<div class="tanc_qy">
		    <ul>
		        <li><button type="button" class="form_btn" onclick="addMenuToRoleSubmit()">提交</button></li>
		    </ul>
		</div>
	</div>
</div>
<!-- 定义JsRender模版 -->
<script id="addMenuToRoleDataTmpl" type="text/x-jsrender">
	<tr class="tr_bj">
		<td height="35" align="center" valign="middle"><input type="checkbox" name="checkbox" value="{{:menuId}}" value_mn="{{:menuName}}"></td>
        <td height="35" align="center" valign="middle">{{:menuId}}</td>
        <td height="35" align="center" valign="middle">{{:menuName}}</td>
        <td height="35" align="center" valign="middle">{{:~subStrLessThan30(menuUrl)}}</td>
        <td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
        <td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
	</tr>
</script>
<script type="text/javascript" >
//赋菜单权限
var isMenuToRoleFlag = true;
function addMenuToRole(){
	if(isMenuToRoleFlag){
		isMenuToRoleFlag = false;
		
		//修改title
		var roleName = $("#roleList_div").find("input[name='radio_roleId']:checked").attr("value_rn");
		$("#addMenuToRole_div").find("h4").text("角色［" + roleName + "］赋菜单权限");
		
		var roleId = $("input[name='radio_roleId']:checked").val();
		if(roleId != null && roleId != 'undefined'){
			var roleName = $("input[name='radio_roleId']:checked").attr("value_rn");
			
			$("#addMenuToRole_div").find("input[name='roleId']").val(roleId);
			$("#addMenuToRole_div").find("input[name='roleName']").val(roleName);
			
			$("#addMenuToRole_div").css('display', 'block');
		}else{
			messageController.alert("请选择记录！");
		}
		
		isMenuToRoleFlag = true;
	}
}

//查询按钮
var isAddMenuToRoleSearchFlag = true;
function searchAddMenuToRoleData() {
	if(isAddMenuToRoleSearchFlag){
		isAddMenuToRoleSearchFlag = false;
    	refreshAddMenuToRoleData();
        isAddMenuToRoleSearchFlag = true;
	}
}

//刷新列表数据、处理分页信息
function refreshAddMenuToRoleData() {
	    $('#addMenuToRole_div').find("#pagination-demo").runnerPagination({
 			url: "${_bathPath}/roleMenuManage/queryCanAddMeunList",
 			method: "POST",
 			dataType: "json",
            showWait: true,
            data: {
                "roleName":  $.trim($("#role_name").val()),
                "menuName":  $.trim($("#serch_menuName").val()),
                "roleId": $.trim($("input[name='radio_roleId']:checked").val()),
                "systemModeId": $("input[name='radio_roleId']:checked").attr("systemModeId")
            },
           	pageSize: 5,
           	visiblePages:5,
            message: "正在为您查询数据..",
            render: function (data) {
            	 if(data != null && data != 'undefined' && data.length>0){
            			//隐藏无数据提示
            			$('#addMenuToRole_div').find(".tishia").hide();
            			$('#addMenuToRole_div').find("#tishia").hide();
            			 var template = $.templates("#addMenuToRoleDataTmpl");
                         var htmlOutput = template.render(data);
                         $("#addMenuToRoleListData").html(htmlOutput);
            	 }else{
            			$("#addMenuToRoleListData").html(null);
            			$('#addMenuToRole_div').find('#pagination-demo').empty();
            			$('#addMenuToRole_div').find(".tishia").hide();
            			$('#addMenuToRole_div').find("#tishia").show();//显示无数据提示
            		}
            }
 		});
}

//获取角色列表
function getAddMenuToRoleData(currentPage) {
	var jsonData;
    var url = "${_bathPath}/roleMenuManage/queryCanAddMeunList";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "currentPage": currentPage,
            "menuName": function () {
                return jQuery.trim($("#serch_menuName").val())
            },
            "roleId":function(){
            	return $("input[name='radio_roleId']:checked").val()
            },
            "systemModeId":function(){
            	return $("input[name='radio_roleId']:checked").attr("systemModeId")
            }
            
        },
        message: "正在加载数据...",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}

//角色赋菜单权限提交方法
function addMenuToRoleSubmit(){
	//获取参数
	var roleId = $("#addMenuToRole_div").find("input[name='roleId']").val();
	var roleName = $("#addMenuToRole_div").find("input[name='roleName']").val();
	
	//构建参数数组
	var roleMenuParamsList = new Array();
	$("#addMenuToRole_div").find("input[name=checkbox]").each(function() {
		if($(this).prop("checked")) {
			var menuId = $(this).val();
			var menuName = $(this).attr('value_mn');
			roleMenuParamsList.push({
				"roleId" : roleId,
				"roleName" : roleName,
				"menuId" : menuId,
				"menuName" : menuName
			});
		}
	});
	
	//发送处理请求
	if (roleMenuParamsList.length == 0) {
		messageController.alert("请选择要添加的数据！");
	} else{
		closeAddMenuToRole();//关闭窗口
		ajaxController.ajax({
	        method: "POST",
	        url: "${_bathPath}/roleMenuManage/addMenuToRole",
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "roleMenuParamsList": JSON.stringify(roleMenuParamsList)
	        },
	        message: "正在加载数据...",
	        success: function (data) {
            	messageController.alert(data.statusInfo,function(){
            		refreshRoleMenuList();
            	});
	        }
	    });
	}
}

/*关闭*/
$('#addMenuToRole_div').find(".tanc_gb a").click(function(event) {
	closeAddMenuToRole();
});

function closeAddMenuToRole(){
	$("#addMenuToRole_div").css('display', 'none');
    $("#addMenuToRoleListData").html(null);
	$('#addMenuToRole_div').find('#pagination-demo').empty();
	$('#addMenuToRole_div').find(".tishia").show();
	$('#addMenuToRole_div').find("#tishia").hide();
	$('#serch_menuName').val(null);
}

</script>