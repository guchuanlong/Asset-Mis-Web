<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
 <script src="${_base}/jsp/staff/staff_main.js"></script>
    <title>员工管理</title>
</head>
<body>
	<div class="main_right">
		<!--右侧大框架-->
		<!--右侧菜单地址-->
		<div class="main_right_Title">
			<p>员工查询</p>
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<!--外侧白色背景框架-->
				<div class="query_list_main">
						<div class="query_qeint">
		                    <ul>
		                        <li >员工工号:</li>
		                        <li><input type="text" class="query_input" id="staffNo"/></li>
		                        <li >员工名称:</li>
		                        <li><input type="text" class="query_input" id="staffName"/></li>
		                        <li >所属部门:</li>
		                        <li><input type="text" class="query_input" id="departName" onclick="showDepart();" readonly/>
		                        	<input type="hidden" id="searchDepartId" value=""/>
		                        	<input type="hidden" id="searchStaffName" name="searchStaffName" value=""/>
			                		<input type="hidden" id="searchStaffNo" name="searchStaffNo" value=""/>
		                        </li>
			                	<li class="query_btn"><a href="javascript:void(0)" id="searchStaffData">查询</a></li>
		                    </ul>
		               </div>
		        	</div>
		        
	        		<!--查询区域-->
	              <div class="query_duan">
						<ul>
							<li>
								<p class="new_duan_xinz">
									<a href="javascript:void(0)" onclick="deleteStaff()">删除</a>
								</p>
							</li>
							<li>
								<p class="new_duan_xinz">
									<a href="javascript:void(0)" onclick="addStaff()">新增</a>
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
		                    <td>员工工号</td>
		                    <td>员工姓名</td>
		                    <td>所属部门</td>
		                    <td>归属省份</td>
		                    <td>归属地市</td>
		                    <td>员工类别</td>
		                    <td>联系电话</td>
		                    <td>联系邮箱</td>
		                     <td>操作</td>
		                </tr>
		              </thead>
		              <tbody id="staffData" ></tbody>
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
	
	 <script id="staffDataTmpl" type="text/x-jsrender">
         <tr>
            <td> <input type="checkbox" name="staffIdCheck" value="{{:staffId}}"/></td>
            <td>{{:staffNo}}</td>
            <td>{{:staffName}}</td>
            <td>{{:departName}}</td>
 			<td>{{:postcode}}</td>
 			<td>{{:positionCode}}</td>
            <td>{{:staffClass}}</td>
            <td>{{:contactTel}}</td>
            <td>{{:email}}</td>
			<td><a  href="javascript:void(0);" onclick="detailStaff('{{:staffId}}')"><i class="icon-edit"></i>查看</a> <a  href="javascript:void(0);" onclick="editStaff('{{:staffId}}')"><i class="icon-edit"></i>修改</a></td>
         </tr>
     </script>
	
	
</body>
</html>
