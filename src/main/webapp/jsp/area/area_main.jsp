<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="ai" uri="/WEB-INF/tag/ai-tags.tld" %>
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
    <title>小区管理</title>
     <script src="${_base}/jsp/area/area_main.js"></script>
</head>
	
<body>
<div class="main_right">
		<!--右侧大框架-->
		<!--右侧菜单地址-->
		<div class="main_right_Title">
			<p>小区查询</p>
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<!--外侧白色背景框架-->
				<div class="query_list_main">
					<div class="query_qeint">
		            	<ul>
	                         <li>小区编码:</li>
	                          <li> 
	                          	<input type="text" class="query_input form-control" id="areaCode"  name="areaCode"/>
	                          </li>
	                         <li>小区名称:</li>
	                          <li> 
	                          	<input type="text" class="query_input form-control" id="areaName" name="areaName"/>
	                          </li>
	                         <li>上级区域:</li>
	                         <li>
	                         	<input type="text" class="query_input form-control" id="parentAreaName" onclick="showArea();" readonly/>
	                        	<input type="hidden" id="parentAreaCode" value=""/>
	                        </li>
			               	<li class="query_btn">
			               		<a href="javascript:void(0)" id="searchAreaData">查询</a>
			               	</li>
		               </ul>
		        	</div>
				</div>
				<!--查询区域-->
				<div class="query_duan">
					<ul>
						<li>
							<p class="new_duan_xinz">
								<a href="javascript:void(0)" onclick="deleteArea()">删除</a>
							</p>
						</li>
						<li>
							<p class="new_duan_xinz">
								<a href="javascript:void(0)" onclick="addArea()">新增</a>
							</p>
						</li>
						
					</ul>
				</div>
	        	<div class="list">
	           		<!--列表区域-->
					<table width="100%" border="0">
	             		<thead>
		                	<tr  class="tr_bj">
			                 	<td>全选</td> 
			                    <td>小区编码</td>
			                    <td>小区名称</td>
			                    <td>上级区域</td>
			                    <td>省份</td>
			                    <td>地市</td>
			                     <td>操作</td>
		                	</tr>
	             		</thead>
	             		<tbody id="areaData" >             	
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
<script id="areaDataTmpl" type="text/x-jsrender">
        <tr>
            <td> <input type="checkbox" name="areaCodeCheck" value="{{:areaCode}}"/></td>
            <td>{{:areaCode}}</td>
            <td>{{:areaName}}</td>
            <td>{{:parentAreaName}}</td>
            <td>{{:provinceName}}</td>
            <td>{{:cityName}}</td>
			<td><a  href="javascript:void(0);" onclick="editArea('{{:areaCode}}')"><i class="icon-edit"></i>修改</a></td>
        </tr>
    </script>
</body>
</html>
