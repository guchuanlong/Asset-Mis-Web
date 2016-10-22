<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="ai" uri="/WEB-INF/tag/ai-tags.tld"%>
<!DOCTYPE html>
<html>
<head>
	<%
		String _base = request.getContextPath();
		request.setAttribute("_base", _base);
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "No-cache");
	%>
	<script>
		var _base = "${_base}";
	</script>
	<script src="${_base}/jsp/depart/depart_main.js"></script>
<title>部门管理</title>
</head>
<body>
	<div class="main_right">
		<!--右侧大框架-->
		<!--右侧菜单地址-->
		<div class="main_right_Title">
			<p>部门查询</p>
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<!--外侧白色背景框架-->
				<div class="query_list_main">
					
						<div class="query_qeint">
							<ul>
								<li class="jig">部门名称:</li>
								<li><input type="text" class="query_input form-control" id="departName"></li>

								<li class="jig">上级部门:</li>
								<li>
									<input type="text" class="query_input form-control" id="parentName" onclick="showDepart();" readonly>
									<input type="hidden" id="searchDepartId"/>
								</li>
								<li class="query_btn"><a href="javascript:void(0)" id="searchDepartData">查询</a></li>
							</ul>
						</div>
					</div>
					<!--查询区域-->
					<div class="query_duan">
						<ul>
							<li>
								<p class="new_duan_xinz">
									<a href="javascript:void(0)" onclick="deleteDepart()">删除</a>
								</p>
							</li>
							<li>
								<p class="new_duan_xinz">
									<a href="javascript:void(0)" onclick="addDepart()">新增</a>
								</p>
							</li>
							
						</ul>
					</div>
					<div class="list">
						
						<!--列表区域-->
						<table width="100%" border="0" class="bk">
							<thead>
							<tr class="tr_bj">
								<td>全选</td> 
			                    <td>部门名称</td>
			                    <td>部门类型</td>
			                    <td>部门级别</td>
			                    <td>上级部门</td>
			                    <td>归属省份</td>
			                    <td>归属地市</td>
			                    <td>联系人</td>
			                    <td>联系电话</td>
			                     <td>操作</td>
							</tr>
							</thead> 
							<tbody id="DepartList">
							</tbody>
						</table>
						<%@ include file="/jsp/commons/tableTip.jsp"%>
						<!--分页-->
						<div>
						   <nav style="text-align:center">
								<ul id="pagination-test">
								  
								</ul>
							</nav>
						</div>
					</div>
				</div>
		</div>
	</div>



	<script id="departDataTmpl" type="text/x-jsrender">
        <tr>
            <td> <input type="checkbox" name="departIdCheck" value="{{:departId}}"/></td>
            <td>{{:departName}}</td>
            <td>{{:departKindType}}</td>
            <td>{{:departLevel}}</td>
            <td>{{:parentDepartName}}</td>
			<td>{{:provinceCode}}</td>
			<td>{{:cityCode}}</td>
            <td>{{:contactName}}</td>
            <td>{{:contactTel}}</td>
			<td><a  href="javascript:void(0);" onclick="editDepart('{{:departId}}')"><i class="icon-edit"></i>修改</a></td>
        </tr>
    </script>
    
</body>
</html>
