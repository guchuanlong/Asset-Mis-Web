<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />

<html>
<head>
    <%@ include file="/jsp/commons/common.jsp"%>
    <script src="${_bathPath}/jsp/menu/menuList.js"></script>
    <script src="${_bathPath}/jsp/commons/systemOption.js"></script>
    <script src="${_bathPath}/jsp/commons/systemModeOption.js"></script>
    <title>菜单管理</title>
</head>

<body>
 
<!----2.中部开始---->
<div class="main_right">
	<div class="main_right_Title">
		<p>菜单管理</p>
	</div>
	<!--查询列表区域-->
	<div class="col-xs-12">
		<div class="query_list query_list_k_paad">
			<div class="query_title">菜单查询</div>
			<!--外侧白色背景框架-->
			<div class="query_list_main">
				<!--查询区域-->
				<div class="query_duan">
					<div class="query_last">
						<ul>
							<li>
								<p class="new_xinz">
									<a id="addMenuBtn" href="javascript:addMenuOption();">新增</a>
								</p>
							</li>
						</ul>
					</div>
					<div class="query_qeint">
						<ul>
							<li class="jig">菜单名称：</li>
							<li><input type="text" class="query_input form-control" id="menuName"></li>

							<li class="query_btn"><a href="javascript:searchMenuList();" id="searchMenuBtn">查询</a></li>
						</ul>
					</div>
				</div>
				<!-- 数据区域 -->
				<div id=menuList_div class="list">
					<table width="100%" border="0">
						<thead>
							<tr class="tr_bj">
								<td>菜单ID</td>
								<td>菜单名称</td>
								<td>菜单URL</td>
								<td>生效时间</td>	
								<td>失效时间</td>
								<td>操作</td>
							</tr>
						</thead>
						<tbody id="menuListData"></tbody>
					</table>
						
					<div class="tishia">
						<p><img src="${_template_path}/images/this1.png"></p>
						<span>请点击查询，查看菜单相关信息！</span>
					</div>
					<div id="tishia" class="tishia" style="display: none;">
						<p><img src="${_template_path}/images/this2.png"></p>
						<span>sorry,没有找到符合你要求的菜单!</span>
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
	</div>


</div>

<!-- 新增修改弹出框 -->
<%@	include file="/jsp/menu/menuOptionDialog.jsp"%>
<!-- 菜单查询弹出框 -->
<%@	include file="/jsp/menu/selectMenuDialog.jsp"%>
<!----2.中部结束---->

<!-- 定义JsRender模版 -->
<script id="menuListDataTmpl" type="text/x-jsrender">
	<tr>
		<td>{{:menuId}}</td>
		<td>{{:menuName}}</td>
		<td>{{:~subStrLessThan30(menuUrl)}}</td>
		<td>{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td><!--yyyy-MM-dd hh:mm:ss-->
		<td>{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
		<td>
			<a href="javascript:void(0);" onclick="addOrUpdateMenu(1, {{:menuId}},{{:menuPid}})"><i class="icon-reorder"></i>详情</a>
			<a href="javascript:void(0);" onclick="addOrUpdateMenu(2, {{:menuId}},{{:menuPid}})"><i class="icon-edit"></i>修改</a>
			<a href="javascript:void(0);" onclick="delMenu({{:menuId}})"><i class="icon-remove"></i>删除</a>
		</td>
	</tr>
</script>
<!-- <script type="text/javascript">
$(document).ready(function () {
	//表单验证
	//renderCheckMenu();
	//渲染日期字段
	//renderDate();
});
</script> -->

</body>
</html>