<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />

<html>
<head>
	<%@ include file="/jsp/commons/common.jsp"%>
	<script src="${_bathPath}/jsp/commons/systemOption.js"></script>
	<script src="${_bathPath}/jsp/commons/systemModeOption.js"></script>
	<script src="${_bathPath}/jsp/role/roleList.js"></script>
	<title>角色管理</title>
</head>
<body>

	<!----2.中部开始---->
	<div class="main_right">
		<div class="main_right_Title">
			<p>角色管理</p>
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<div class="query_title">角色查询</div>
				<!--外侧白色背景框架-->
				<div class="query_list_main">
					<!--查询区域-->
					<div class="query_duan">
						<div class="query_last">
							<ul>
								<li>
									<p class="new_xinz">
										<a id="addRole" href="javascript:addRole();">添加角色</a>
									</p>
								</li>
							</ul>
						</div>
						<div class="query_qeint">
							<ul>
								<li class="jig">角色名称：</li>
								<li><input type="text" class="query_input form-control" id="role_name"></li>
	
								<li class="query_btn"><a href="javascript:searchRoleData();" id="searchRoleData">查询</a></li>
							</ul>
						</div>
					</div>
						
					<!-- 数据区域 -->
					<div id=menuList_div class="list">
						<table width="100%" border="0">
							<thead>
								<tr class="tr_bj">
									<td>角色id</td>
									<td>角色名称</td>
									<td>角色描述</td>
									<td>生效时间</td>
									<td>失效时间</td>
									<td>操作</td>
								</tr>
							</thead>
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
	</div>
	<%@ include file="/jsp/role/roleOptionDialog.jsp" %>
	<!----2.中部结束---->
	
	<!-- 定义JsRender模版 -->
	<script id="roleListDataTmpl" type="text/x-jsrender">
	<tr>			
		<td>{{:roleId}}</td>
		<td>{{:roleName}}</td>
		<td>{{:roleDesc}}</td>	
		<td>{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
		<td>
			<a href="javascript:void(0);" onclick="searchRole({{:roleId}})"><i class="icon-reorder"></i>详情</a>
			<a href="javascript:void(0);" onclick="updateRole({{:roleId}})"><i class="icon-edit"></i>修改</a>
			<a href="javascript:void(0);" onclick="delRole({{:roleId}})"><i class="icon-remove"></i>删除</a>
		</td>
	</tr>
	</script>
</body>
</html>