<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- 新增，修改对话框 -->
<div id="addRoleDialog" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
  <div class="tanc_bg"></div>
  <div class="tanc_nr">
   <div class="tanc_gb"><a href="javascript:void(0);" onclick="cancelAddRole()"><img src="<%=_bathPath%>/template/default/images/close2.png"></a></div>
   <div class="page-header">
      <h4>账号赋角色信息</h4>
   </div>
   
   <div class="query_qeint">
		<ul >
			<li>角色名称：</li><li><input class="query_input" type="text" id="serch_roleName"></li>
			<li class="query_btn">
				<a href="javascript:searchRoleData()">查询</a>
			</li>
			
		</ul>
	</div>
     
   <div class="table_tc">
    <table border="0" width="100%">
       	<tr class="tc_tr">
          <td height="35" align="center" valign="middle">选择</td>
          <td height="35" align="center" valign="middle">角色ID</td>
          <td height="35" align="center" valign="middle">角色名称</td>
          <td height="35" align="center" valign="middle">生效时间</td>
          <td height="35" align="center" valign="middle">失效时间</td>
        </tr>
        <tbody id="roleListData"></tbody>
     </table>
     <div class="tishia">
		<p>
			<img src="${_template_path }/images/this1.png">
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
	 
	 <!-- 定义JsRender模版 -->
	 <script id="roleListDataTmpl" type="text/x-jsrender">
	  <tr>
		<td height="35" align="center" valign="middle"><input type="checkbox" name="checkbox" id="checkbox_{{:roleId}}" value="{{:roleId}}" nm_value="{{:roleName}}"></td>
        <td height="35" align="center" valign="middle">{{:roleId}}</td>
        <td height="35" align="center" valign="middle">{{:roleName}}</td>
        <td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
        <td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
	  </tr>
	 </script>
   	</div>
    <div class="tanc_qy">
     <ul>
     
       	<li><button id="cancelAddRoleBtn" type="button" class="form_btn" onclick="cancelAddRole()">取消</button></li>
        <li><button id="submitAddRoleBtn" type="button" class="form_btn" onclick="doAddRole()">确认</button></li>
     </ul>
    </div>
  </div>
</div>

<script type="text/javascript">

/**隐藏对话框 */
function hiddenRoleDialog(){
	$("#addRoleDialog").css('display', 'none');
	$("#addRoleDialog").find("#roleListData").html(null);
	$("#addRoleDialog").find('#pagination-demo').empty();
	$("#addRoleDialog").find(".tishia").show();//显示查询提示
	$("#addRoleDialog").find("#tishia").hide();//隐藏无数据提示
}

/*取消关闭弹框*/
function cancelAddRole(){
	hiddenRoleDialog();
}

/** 给账号赋角色 **/
function doAddRole(){
	var operRoleList = new Array();
	var operId = $("input[name='radio_staffnoId']:checked").val();			
	$("#roleListData").find("input[name=checkbox]").each(function() {
			if ($(this).prop("checked")) {
				var roleId = $(this).val();
				var roleName = $(this).attr("nm_value")
				operRoleList.push({
					"roleId" : roleId,
					"roleName" : roleName,
					"staffnoId" : operId
				});
			}
		});
		if (operRoleList.length == 0) {
			messageController.alert("请选择要添加的数据！");
		} else {
			hiddenRoleDialog();//隐藏对话框
			var data = {
				"operRoleList" : JSON.stringify(operRoleList)
			};
			var url = "${_bathPath}/operRoleManage/addRoleToOper";
			ajaxController.ajax({
				method : "POST",
				url : url,
				dataType : "json",
				showWait : true,
				async : false,
				data : data,
				message : "正在加载数据..",
				success : function(data) {
					messageController.alert(data.statusInfo, function() {
						//刷新账号角色列表
						refreshOperRoleList();
					});
				}
			});
		}
	}

	//查询按钮
	var isSearchRoleFlag = true;
	function searchRoleData() {
		if (isSearchRoleFlag) {
			isSearchRoleFlag = false;

			$("#addRoleDialog").find("#pagination-demo").runnerPagination({
	    		url: "${_bathPath}/operRoleManage/queryCanAddRoleList",
	    		method: "POST",
	    		dataType: "json",
	    	    showWait: true,
	    	    async: false,
	    	    data: {
	    	    	"roleName" : function() {
						return jQuery.trim($("#serch_roleName").val())
					},
					"staffnoId":function(){
						return $("input[name='radio_staffnoId']:checked").val();		
					}
	    	    },
	    	   	pageSize: 5,
	    	   	visiblePages:5,
	    	    message: "正在为您查询数据..",
	    	    render: function (data) {
	    	        if(data != null && data != 'undefined' && data.length>0){
	    	        	$("#addRoleDialog").find(".tishia").hide();
	    	        	$("#addRoleDialog").find("#tishia").hide();
	    	        	var template = $.templates("#roleListDataTmpl");
	    	        	var htmlOutput = template.render(data);
	    	        	$("#addRoleDialog").find("#roleListData").html(htmlOutput);
	    	        }else{
	            		$("#addRoleDialog").find(".tishia").hide();
	            		$("#addRoleDialog").find("#tishia").show();//显示无数据提示
	            		$("#addRoleDialog").find("#roleListData").html(null);
	            		$("#addRoleDialog").find('#pagination-demo').empty();
	    	        }
	    	    }
	    	});
			isSearchRoleFlag = true;
		}
	}
</script>