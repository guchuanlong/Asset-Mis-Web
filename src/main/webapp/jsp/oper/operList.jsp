<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="operInfo" style="display: none;">
	<div class="query_list query_list_k_paad">
		<div class="query_title">工号对应操作员列表</div>
		<!--外侧白色背景框架-->
		<div class="query_list_main">
			<!--操作区域-->
			<!-- <div class="query_duan">
				<div class="query_last" style="width:30%">
					<ul>
						<li>
							<p class="new_xinz">
								<a href="javascript:addOper();">增加</a>
							</p>
							<p class="new_xinz">
								<a href="javascript:updateOper();">修改</a>
							</p>
							<p class="new_xinz">
								<a href="javascript:delOper();">删除</a>
							</p>
							<p class="new_xinz">
								<a href="javascript:operAddRole();">赋角色</a>
							</p>
						</li>
					</ul>
				</div>
			</div> -->
			<div class="query_duan">
				
					<ul>
						<li>
							<p class="new_duan_xinz">
								<a href="javascript:operAddRole();">赋角色</a>
							</p>
							<!-- <p class="new_duan_xinz">
								<a href="javascript:delOper();">删除</a>
							</p>
							<p class="new_duan_xinz">
								<a href="javascript:updateOper();">修改</a>
							</p> -->
							<p class="new_duan_xinz">
								<a href="javascript:addOper();">增加</a>
							</p>
						</li>
					</ul>
				
			</div>
			<!-- 数据区域 -->
			<div id=staffnoInfo class="list">
			
					<table width="100%" border="0">
						<thead>
							<tr class="tr_bj">
								<td>选择</td>
								<td>操作员ID</td>
								<td>操作员编码</td>
								<td>归属组织类型</td>	
								<td>归属组织</td>
								<td>操作</td>
								<td style="display:none;" >归属组织ID</td>
							</tr>
						</thead>
						<tbody id="operListData"></tbody>
					</table>
					<div class="tishia">
						<p><img src="${_bathPath}/images/this1.png"></p>
						<span>请选择员工工号，查看操作员相关信息！</span>
					</div>
					<div id="tishia" class="tishia" style="display: none;">
						<p><img src="${_bathPath}/images/this2.png"></p>
						<span>sorry,没有找到符合你要求的操作员!</span>
					</div>
				</div>
		</div>
	</div>
</div>

<!-- 定义JsRender模版 -->
<script id="operListDataTmpl" type="text/x-jsrender">
	<tr>
		<td><input type="radio" style="width:14px;" name="radio_operId" value="{{:operId}}" onclick="refreshOperRoleList()" operCode="{{:operCode}}"></td>					
		<td>{{:operId}}</td>
		<td>{{:operCode}}</td>
		<td>
			{{if orgType==1}}
				部门
			{{else orgType==2}}
				渠道
			{{/if}}
		</td>
		<td>{{:orgName}}</td>
		<td>
			<a href="javascript:void(0);" onclick="updateOper('{{:operId}}')"><i class="icon-edit"></i>修改</a>
			<a href="javascript:void(0);" onclick="delOper('{{:operId}}')"><i class="icon-remove"></i>删除</a>
		</td>
		<td style="display:none;" >{{:orgId}}</td>
	</tr>
</script>

<script type="text/javascript">
//刷新列表数据信息
function refreshOperList() {
	//展现操作员区域
	$("#operInfo").show();
	$("#operRoleInfo").hide();
	//改变标题
	var staffNo = $("#staffnoInfo").find("input[name='radio_staffnoId']:checked").attr("staffNo");
	$("#operInfo").find(".query_title").text("工号["+staffNo+"]对应操作员列表");
	//查询数据并展示在页面
    var jsondata = getOperList();
	if(jsondata != null && jsondata != 'undefined' && jsondata.length>0){
		//隐藏无数据提示
		$("#operInfo").find(".tishia").hide();
		$("#operInfo").find("#tishia").hide();
		
		//展示list数据
	    var template = $.templates("#operListDataTmpl");
	    var htmlOutput = template.render(jsondata);
	    $("#operListData").html(htmlOutput);
	}else{
		$("#operInfo").find("#operListData").html(null);
		$("#operInfo").find(".tishia").hide();
		$("#operInfo").find("#tishia").show();//显示无数据提示
	}
	//设置操作员角色展示区效果
	$("#operRoleInfo").find("#operRoleListData").html(null);
	$("#operRoleInfo").find('#pagination-demo').empty();
	$("#operRoleInfo").find(".tishia").show();
	$("#operRoleInfo").find("#tishia").hide();
}

//查询员工工号列表
function getOperList() {
    var jsonData;
    var url = "${_bathPath}/operManage/queryOperListByStaffno";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "staffnoId": function () {
                return $("input[name='radio_staffnoId']:checked").val();
            }
        },
        message: "正在加载数据...",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
} 

/**添加角色按钮触发事件 弹出对话框*/
var isAddFlag = true;
function addOper(){
	if(isAddFlag){
		isAddFlag = false;
		var operId = $("input[name='radio_staffnoId']:checked").val();
		if(operId != null && operId != 'undefined'){
			$("#operDlgTitle").text("添加操作员");
			$("#operDialog").css('display', 'block');
			$("input[name='operId']").val(null);
			$("input[name='operCode']").val(null);
		    $("input[name='orgType']").val(null);
		    $("input[name='orgId']").val(null);
		    $("input[name='orgName']").val(null);
		}else{
			messageController.alert("请选择员工信息！");
		}
		isAddFlag = true;
	}
}

//角色修改
var isUpdateFlag = true;
function updateOper(operId) {
	if(isUpdateFlag){
		isUpdateFlag = false;
			$("#operDlgTitle").text("修改操作员");
			$("#operDialog").css('display', 'block');
			var operData = gatOperInfo(operId);
			if(operData != null && operData != 'undefined'){
				$("input[name='operId']").val(operData.operId);
				$("input[name='operCode']").val(operData.operCode);
		        $("input[name='orgType']").val(operData.orgType);
		        $("input[name='orgId']").val(operData.orgId);
		        $("input[name='orgName']").val(operData.orgName);
		        setSelectOrgType(operData.orgType);
			}
		isUpdateFlag = true;
	}
}

/** 设置组织类型下拉框默认选择对象 **/
function setSelectOrgType(orgType){
	var orgTypeOptions = document.getElementById("orgType").options;
	for(var i=0;i<orgTypeOptions.length;i++){
		if(orgTypeOptions[i].value == orgType){
			orgTypeOptions[i].selected = true;
		}
	}
}

//删除操作员
function delOper(operId) {
		messageController.confirm("确认要删除该操作员吗？", function(){
			var url = "${_bathPath}/operManage/delOperInfo";
		    ajaxController.ajax({
		        method: "POST",
		        url: url,
		        dataType: "json",
		        showWait: true,
		        async: false,
		        data: {
		            "operId": operId
		        },
		        message: "正在加载数据...",
		        success: function (data) {
		        	messageController.alert(data.statusInfo, function(){
			        	//刷新列表
			        	refreshOperList();
		        	});
		        }
		    });
		});
}

//获取操作员信息 
function gatOperInfo(operId){
	var jsonData={};
	if(operId!=null){
		var url = "${_bathPath}/operManage/getOperInfo";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "operId": operId
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	jsonData = data.data;
	        }
	    });
	}
    return jsonData;
}

</script>