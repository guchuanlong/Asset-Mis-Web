<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="operAreaInfo" style="display: none;">
	<div class="query_list query_list_k_paad" id="roleMenu_roleList">
		<div class="query_title">操作员对应区域列表</div>
		<!--外侧白色背景框架-->
		<div class="query_list_main">
			<!-- 数据区域 -->
			<div id=menuList_div class="list">
				<table width="100%" border="0">
					<thead>
						<tr class="tr_bj">
						<td>区域名称</td>
						<td>生效时间</td>
						<td>失效时间</td>
						<td>操作</td>
						<td style="display: none">操作员ID</td>
						<td style="display: none">区域Code</td>
					</tr>
					<tbody id="operAreaListData"></tbody>
				</table>
				<div class="tishia">
					<p><img src="${_bathPath}/images/this1.png"></p>
					<span>请选择操作员，查看相关信息！</span>
				</div>
				<div id="tishia" class="tishia" style="display: none;">
					<p><img src="${_bathPath}/images/this2.png"></p>
					<span>sorry,没有找到该操作员相关区域!</span>
				</div>
				<!-- 分页信息 -->
				<div>
					<nav style="text-align: center">
						<ul id="pagination-demo-area">
		
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</div>
</div>

<script id="operAreaListDataTmpl" type="text/x-jsrender">
			<tr>
				<td>{{:addressDesc}}</td>
				<td>{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
				<td>{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
				<td><a href="javascript:void(0);" onclick="delOperArea({{:operAreaRelId}})"><i class="icon-remove"></i>删除</a></td>
				<td style="display:none">{{:operId}}</td>
				<td style="display:none">{{:areaCode}}</td>
			</tr>
			</script>
<script type="text/javascript">
	var isSearchOperAreaListFlg = true;
	//刷新列表数据信息
	function searchOperAreaList() {
		if (isSearchOperAreaListFlg) {
			isSearchOperAreaListFlg = false;
			//展现角色区域
			$("#operAreaInfo").show();
			//改变标题
			var operCode = $("#operArea_oper").find(
					"input[name='radio_operId']:checked").attr("operCode");
			$("#operAreaInfo").find(".query_title").text(
					"操作员[" + operCode + "]对应区域列表");
			var url = "${_bathPath}/operAreaManage/queryOperAreaList";
			$("#pagination-demo-area").runnerPagination({
	 			url: url,
	 			method: "POST",
	 			dataType: "json",
	            showWait: true,
	            data: {
	            	"operId" : function() {
						return $("input[name='radio_operId']:checked").val();
					}
	            },
	           	pageSize: 5,
	           	visiblePages:5,
	            message: "正在为您查询数据..",
	            render: function (data) {
	                 if(data != null && data != 'undefined' && data.length>0){
	     	        	$("#operAreaInfo").find(".tishia").hide();
	     	        	$("#operAreaInfo").find("#tishia").hide();
	     	        	var template = $.templates("#operAreaListDataTmpl");
	     	        	var htmlOutput = template.render(data);
	     	        	$("#operAreaListData").html(htmlOutput);
	     	        }else{
	             		$("#operAreaInfo").find(".tishia").hide();
	             		$("#operAreaInfo").find("#tishia").show();//显示无数据提示
	     	        	$("#operAreaListData").html(null);
	             		$('#pagination-demo-area').empty();
	     	        }
	            }
	 		});
		
			
			isSearchOperAreaListFlg = true;
		}
	}

	/* //查询员工工号列表
	function getOperAreaList(currentPage) {
		var jsonData;
		var url = "${_bathPath}/operAreaManage/queryOperAreaList";
		ajaxController.ajax({
			method : "POST",
			url : url,
			dataType : "json",
			showWait : true,
			async : false,
			data : {
				"currentPage" : currentPage,
				"operId" : function() {
					return $("input[name='radio_operId']:checked").val();
				}
			},
			message : "正在加载数据...",
			success : function(data) {
				jsonData = data.data;
			}
		});
		return jsonData;
	} */

	/**
	 * 删除操作员角色关系
	 */
	function delOperArea(operAreaRelId) {
		messageController.confirm("确认要删除该区域吗？", function() {
			var url = "${_bathPath}/operAreaManage/delOperArea";
			ajaxController.ajax({
				method : "POST",
				url : url,
				dataType : "json",
				showWait : true,
				async : false,
				data : {
					"operAreaRelId" : operAreaRelId
				},
				message : "正在加载数据..",
				success : function(data) {
					messageController.alert(data.statusInfo, function() {
						//刷新列表
						searchOperAreaList();
					});
				}
			});
		});
	}
</script>
