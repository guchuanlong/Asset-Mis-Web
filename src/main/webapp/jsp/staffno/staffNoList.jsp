<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<html>
<head>
	<%@ include file="/jsp/commons/common.jsp"%>
    <title>员工账号管理</title>
</head>

<body>
 
	<!----2.中部开始---->
	<div class="main_right">
		<div class="main_right_Title">
			<p>账号管理</p>
		</div>
	
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<div class="query_title">账号查询</div>
				<!--外侧白色背景框架-->
				<div class="query_list_main">
					<!--查询区域-->
					<div class="query_duan">
						<div class="query_last">
							<ul>
								<li>
									<p class="new_xinz">
										<a href="javascript:manageStaffNo(0);">增加</a>
									</p>
									<p class="new_xinz">
										<a href="javascript:operAddRole();">赋角色</a>
									</p>
								</li>
							</ul>
						</div>
						<div class="query_qeint">
							<ul>
								<li class="jig">员工名称：</li>
								<li><input type="text" class="query_input" id="staffName"></li>
								<li class="jig">员工账号：</li>
								<li><input type="text" class="query_input" id="staffNo"></li>
								<li class="query_btn"><a href="javascript:searchStaffNoList();" id="searchStaffNoBtn">查询</a></li>
							</ul>
						</div>
						<!-- <div class="query_duan">
							<ul>
								<li>
									<p class="new_duan_xinz">
										<a href="javascript:resetStaffPasswd();">密码重置</a>
										<a href="javascript:delStaffNo();">删除</a> 
										<a href="javascript:manageStaffNo(2);">修改</a> 
										<a href="javascript:manageStaffNo(0);">增加</a> 
									</p>
								</li>
							</ul>
						</div> -->

					</div>
					
					<!-- 数据区域 -->
					<div id="staffnoInfo" class="list">
						<table width="100%" border="0">
							<thead>
								<tr class="tr_bj">
									<td>选择</td>
									<td>员工账号</td>
									<td>员工名称</td>
									<td>生效时间</td>	
									<td>失效时间</td>
									<!-- <td>状态</td> -->
									<td>操作</td>
								</tr>
							</thead>
							<tbody id="staffNoListData"></tbody>
						</table>
						<div class="tishia">
							<p><img src="${_template_path}/images/this1.png"></p>
							<span>请点击查询，查看员工账号相关信息！</span>
						</div>
						<div id="tishia" class="tishia" style="display: none;">
							<p><img src="${_template_path}/images/this2.png"></p>
							<span>sorry,没有找到符合你要求的员工账号!</span>
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
			<!-- 操作员管理列表 begin -->
			<%@ include file="/jsp/oper/operList.jsp"%>
			<!-- 操作员管理列表  end  -->
			<!-- 操作员角色列表 begin -->
			<%@ include file="/jsp/operrole/operRoleList.jsp"%>
			<!-- 操作员角色列表  end  -->
		</div>
	</div>
	<!-- 新增、修改、详情窗口 begin -->
	<%@ include file="/jsp/staffno/staffNoAddOrUpdateView.jsp"%>
	<!-- 操作员 新增、修改对话框 begin -->
	<%@ include file="/jsp/oper/operDialog.jsp"%>
	<!-- 操作员 新增、修改对话框  end  -->
	<!-- 赋角色对话框 begin -->
	<%@ include file="/jsp/operrole/operAddRoleDialog.jsp"%>
	<!-- 赋角色对话框  end  -->
	<!-- 新增、修改、详情窗口  end  -->
	<!----2.中部结束----> 

	<!-- 定义JsRender模版 -->
	<script id="staffNoListDataTmpl" type="text/x-jsrender">
	<tr>
		<td><input type="radio" style="width:14px;" name="radio_staffnoId" onclick="refreshOperRoleList()" value="{{:staffnoId}}" staffNo="{{:staffNo}}" staffnoId="{{:staffnoId}}"></td>					
		<td>{{:staffNo}}</td>
		<td>{{:staffName}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
		<!--<td>{{if state==01}}
           		生效
			{{else state==02}}
				失效
			{{/if}}
   		</td>-->
        <td>
			 <a href="javascript:void(0);" onclick="manageStaffNo(1, '{{:staffnoId}}')"><i class="icon-reorder"></i>详情</a>
   			 <a href="javascript:void(0);" onclick="manageStaffNo(2,'{{:staffnoId}}')"><i class="icon-edit"></i>修改</a>
			 <a href="javascript:void(0);" onclick="resetStaffPasswd('{{:staffNo}}')"><i class="icon-edit"></i>密码重置</a>
			 <a href="javascript:void(0);" onclick="delStaffNo('{{:staffnoId}}')"><i class="icon-remove"></i>删除</a>
		</td>
	</tr>
	</script>
	
	
	<script type="text/javascript">
	$(document).ready(function () {
		//刷新列表
		searchStaffNoList();
	});
	
	//查询
	var isSearchFlag = true;
	function searchStaffNoList(){
		if(isSearchFlag){
			isSearchFlag = false;
			refreshStaffNoList();
	        isSearchFlag = true;
		}
	}
	
	//刷新列表数据、处理分页信息
	function refreshStaffNoList(pageNo) {
		
		//隐藏操作员,角色信息区域
		$("#operInfo").hide();
		$("#operRoleInfo").hide();
		
		$("#staffnoInfo").find("#pagination-demo").runnerPagination({
    		url: "${_bathPath}/staffNoManage/queryStaffNoList",
    		method: "POST",
    		dataType: "json",
    	    showWait: true,
    	    async: false,
    	    data: {
    	    	"staffName": function () {
	                return jQuery.trim($("#staffName").val())
	            },
    	    	"staffNo": function () {
	                return jQuery.trim($("#staffNo").val())
	            }
    	    },
    	   	pageSize: 5,
    	   	visiblePages:5,
    	    message: "正在为您查询数据..",
    	    render: function (data) {
    	        if(data != null && data != 'undefined' && data.length>0){
    	        	$("#staffnoInfo").find(".tishia").hide();
    				$("#staffnoInfo").find("#tishia").hide();
    	        	var template = $.templates("#staffNoListDataTmpl");
    	        	var htmlOutput = template.render(data);
    	        	$("#staffNoListData").html(htmlOutput);
    	        }else{
            		$("#staffnoInfo").find(".tishia").hide();
            		$("#staffnoInfo").find("#tishia").show();//显示无数据提示
    	        	$("#staffNoListData").html(null);
            		$("#staffnoInfo").find('#pagination-demo').empty();
    	        }
    	    }
    	});
	}
	
	//查询员工账号列表
	function getStaffNoList(pageNo) {
	    var jsonData;
	    var url = "${_bathPath}/staffNoManage/queryStaffNoList";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "currentPage": pageNo,
	            "staffNo": function () {
	                return jQuery.trim($("#staffNo").val())
	            }
	        },
	        message: "正在加载数据...",
	        success: function (data) {
	            jsonData = data.data;
	        }
	    });
	    return jsonData;
	}
	
	var isManageStaffNoFlg = true;
	//新增、修改、查看员工账号（type：0新增，1查看，2修改）
	function manageStaffNo(type,staffnoId) {
		if(isManageStaffNoFlg){
			isManageStaffNoFlg =false;
			if(type == 0){//新增
			   	addOrUpdateStaffNoFun(type, null);
			}else {//修改、查看
				addOrUpdateStaffNoFun(type, staffnoId);
			}
			isManageStaffNoFlg = true;
		}
	}
	
	//删除员工账号
	function delStaffNo(staffnoId) {
			messageController.confirm("确认要删除该员工账号吗？", function(){
			    ajaxController.ajax({
			        method: "POST",
			        url: "${_bathPath}/staffNoManage/delStaffNo",
			        dataType: "json",
			        showWait: true,
			        async: false,
			        data: {
			            "staffnoId": staffnoId
			        },
			        message: "正在加载数据...",
			        success: function (data) {
			        	messageController.alert(data.statusInfo, function(){
				        	//刷新列表
				        	refreshStaffNoList();
			        	});
			        }
			    });
			});
	}
	
	function resetStaffPasswd(staffNo){
			messageController.confirm("确认要重置该员工密码吗？", function(){
			    ajaxController.ajax({
			        method: "POST",
			        url: "${_bathPath}/staffNoManage/resetStaffPasswd",
			        dataType: "json",
			        showWait: true,
			        async: false,
			        data: {
			            "staffNo": staffNo
			        },
			        message: "正在加载数据...",
			        success: function (data) {
			        	messageController.alert(data.statusInfo, function(){
				        	//刷新列表
				        	//refreshStaffNoList();
			        	});
			        }
			    });
			});
	}
	
	
	
	//操作人赋角色
	var isAddRoleFlag = true;
	function operAddRole(){
		if(isAddRoleFlag){
			isAddRoleFlag = false;
			var operId = $("input[name='radio_staffnoId']:checked").val();
			if(operId != null && operId != 'undefined'){
				$("#addRoleDialog").css('display', 'block');
			}else{
				messageController.alert("请选择赋角色的操作员！");
			}
			isAddRoleFlag = true;
		}
	}
	
	</script>

</body>
</html>