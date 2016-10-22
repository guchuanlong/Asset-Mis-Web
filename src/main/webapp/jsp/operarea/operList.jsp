<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />

<html>
<head>
	<%@ include file="/jsp/commons/common.jsp"%>
	<title>操作员区域管理</title>
</head>
<body>
	<!----2.中部开始---->
	<div class="main_right">
		<div class="main_right_Title">
			<p>操作员区域管理</p>
		</div>

		<div class="col-xs-12">
			<div class="query_list query_list_k_paad" id="operArea_oper">
			<div class="query_title">操作员查询</div>
				<div class="query_duan">
					<div class="query_last">
						<ul>
							<li>
								<p class="new_xinz">
									<a href="javascript:selectAreaToOper();">添加区域</a>
								</p>
							</li>
						</ul>
					</div>
					<div class="query_qeint">
						<ul>
							<li class="jig">员工姓名：</li>
							<li>
								<input id="staffNameQ" type="text" class="query_input form-control">
							</li>
							<li class="jig">操作员编码：</li>
							<li>
								<input id="operCodeQ" type="text" class="query_input form-control">
							</li>
							<li class="query_btn">
								<a href="javascript:void(0)" id="searchRoleData" onclick="searchOperList()">查询</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="list">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<thead>
							<tr class="tr_bj">
								<td>选择</td>
								<td>操作员ID</td>
								<td>操作员编码</td>
								<td>员工姓名</td>
								<td>归属组织类型</td>
								<td>归属组织</td>
								<td>省份</td>
								<td>城市</td>
								<td style="display: none;">归属组织ID</td>
						</thead>
						</tr>
						<tbody id="operListData"></tbody>
					</table>
					<div class="tishia">
						<p>
							<img src="${_bathPath}/images/this1.png">
						</p>
						<span>请点击查询，查看相关信息！</span>
					</div>
					<div id="tishia1" class="tishia" style="display: none;">
						<p>
							<img src="${_bathPath}/images/this2.png">
						</p>
						<span>sorry,没有找到符合你要求的信息!</span>
					</div>
					<!-- 分页信息 -->
					<div>
						<nav style="text-align:center">
						<ul id="pagination-sm">

						</ul>
						</nav>
					</div>
				</div>
			</div>
			<!-- 操作员拥有区域列表 begin -->
			<%@ include file="/jsp/operarea/operAreaList.jsp"%>
			<!-- 操作员拥有区域列表  end  -->
		</div>
	</div>
	<!----2.中部结束---->

	<!-- 区域列表 begin -->
	<%@ include file="/jsp/operarea/areaList.jsp"%>
	<!-- 区域列表  end  -->


	<!-- 定义JsRender模版 -->
	<script id="operListDataTmpl" type="text/x-jsrender">
	<tr>
		<td><input type="radio" style="width:14px;" name="radio_operId" value="{{:operId}}" onclick="searchOperAreaList()" operCode="{{:operCode}}"></td>					
		<td>{{:operId}}</td>
		<td>{{:operCode}}</td>
		<td>{{:staffName}}</td>
		<td>
			{{if orgType==1}}
				部门
			{{else orgType==2}}
				渠道
			{{/if}}
		</td>
		<td>{{:orgName}}</td>
		<td>{{:provinceName}}</td>
		<td>{{:cityName}}</td>
		<td style="display:none;" >{{:orgId}}</td>
	</tr>
	</script>

	<script type="text/javascript">

		var isSearchFlg = true;
		//刷新列表数据信息
		function searchOperList() {
			if (isSearchFlg == true) {
				isSearchFlg = false;
				
				var url = "${_bathPath}/operManage/queryOperPageInfo";
				$("#pagination-sm").runnerPagination({
		 			url: url,
		 			method: "POST",
		 			dataType: "json",
		            showWait: true,
		            data: {
		            	"operCode" : function() {
							return jQuery.trim($("#operCodeQ").val())
						},
						"staffName" : function() {
							return jQuery.trim($("#staffNameQ").val())
						}
		            },
		           	pageSize: 10,
		           	visiblePages:5,
		            message: "正在为您查询数据..",
		            render: function (data) {
		            	$("#operAreaInfo").hide();
		            	
		                 if(data != null && data != 'undefined' && data.length>0){
		     	        	$("#operArea_oper").find(".tishia").hide();
		     	        	$("#operArea_oper").find("#tishia1").hide();
		     	        	var template = $.templates("#operListDataTmpl");
		     	        	var htmlOutput = template.render(data);
		     	        	$("#operListData").html(htmlOutput);
		     	        }else{
		             		$("#operArea_oper").find(".tishia").hide();
		             		$("#operArea_oper").find("#tishia1").show();//显示无数据提示
		     	        	$("#operListData").html(null);
		             		$('#pagination-sm').empty();
		     	        }
		            }
		 		});
				//查询数据并展示在页面
				isSearchFlg = true;
			}
		}

	</script>

</body>
</html>