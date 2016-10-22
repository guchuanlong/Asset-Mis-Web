<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="show_staffno_list_div" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
	<input name="roleId" type="hidden">
	<input name="roleName" type="hidden">
	<div class="tanc_bg"></div>
	<div class="tanc_nr">
		<div class="tanc_gb">
			<a href="javascript:void(0);"><img src="<%=_bathPath%>/images/close2.png"></a>
		</div>
		<div class="page-header">
           	<h4>选择员工</h4>
       	</div>
		<div class="query_qeint">
			<ul >
				<li>员工工号：</li><li><input class="query_input" type="text" id="serch_staffNo"></li>
				<li class="query_btn">
					<a href="javascript:searchStaffnoListData()">查询</a>
				</li>
			</ul>
		</div>
		<div class="table_tc">
			<table border="0" width="100%">
		       	<tr class="tc_tr">
					<td height="35" align="center" valign="middle">选择</td>
					<td height="35" align="center" valign="middle">员工工号</td>
					<td height="35" align="center" valign="middle">员工名称</td>
					<td height="35" align="center" valign="middle">生效时间</td>	
					<td height="35" align="center" valign="middle">失效时间</td>
				</tr>
		        <tbody id="addMenuToRoleListData"></tbody>
			</table>
		    <div class="tishia" style="padding-left: 240px;">
				<p>
					<img src="${_bathPath}/images/this1.png">
				</p>
				<span>请点击查询，查看菜单相关信息！</span>
			</div>
			<div id="tishia" class="tishia" style="display: none; padding-left: 240px;">
				<p>
					<img src="${_bathPath}/images/this2.png">
				</p>
				<span>sorry,没有找到符合你要求的菜单!</span>
			</div>
			<!-- 分页信息 -->
			<div>
				<nav style="text-align:center">
					<ul id="pagination-demo">
					</ul>
				</nav>
			</div>
	 
		</div>
		<div class="tanc_qy">
		    <ul>
		        <li><button type="button" class="form_btn" onclick="selectStaffnoSubmit()">确定</button></li>
		    </ul>
		</div>
	</div>
</div>
<!-- 定义JsRender模版 -->
<script id="addMenuToRoleDataTmpl" type="text/x-jsrender">
	<tr>
		<td height="35" align="center" valign="middle"><input type="radio" style="width:14px;" name="radio_staffId" onclick="" value="{{:staffId}}" staffName="{{:staffName}}" staffNo="{{:staffNo}}"></td>					
		<td height="35" align="center" valign="middle">{{:staffNo}}</td>
		<td height="35" align="center" valign="middle">{{:staffName}}</td>
		<td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
		<td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
	</tr>
</script>
<script type="text/javascript" >
//展现员工列表
var isMenuToRoleFlag = true;
function showStaffnoList(){
	if(isMenuToRoleFlag){
		isMenuToRoleFlag = false;
		$("#show_staffno_list_div").css('display', 'block');
		isMenuToRoleFlag = true;
	}
}

//查询按钮
var isStaffnoListSearchFlag = true;
function searchStaffnoListData() {
	if(isStaffnoListSearchFlag){
		isStaffnoListSearchFlag = false;
    	refreshStaffnoListData(1);
        isStaffnoListSearchFlag = true;
	}
}

//刷新列表数据、处理分页信息
function refreshStaffnoListData(pageNo) {
	
    $('#show_staffno_list_div').find("#pagination-demo").runnerPagination({
		url: "${_bathPath}/commonManage/queryStaffList",
		method: "POST",
		dataType: "json",
        showWait: true,
        async: false,
        data: {
        	"staffNo": function () {
                return jQuery.trim($("#serch_staffNo").val())
            }
        },
       	pageSize: 5,
       	visiblePages:5,
        message: "正在为您查询数据..",
        render: function (data) {
            if(data != null && data != 'undefined' && data.length>0){
            	var template = $.templates("#addMenuToRoleDataTmpl");
            	var htmlOutput = template.render(data);
            	$("#addMenuToRoleListData").html(htmlOutput);
            	//隐藏无数据提示
        		$('#show_staffno_list_div').find(".tishia").hide();
        		$('#show_staffno_list_div').find("#tishia").hide();
            }else{
            	$("#addMenuToRoleListData").html(null);
        		$('#show_staffno_list_div').find('#pagination-demo').empty();
        		$('#show_staffno_list_div').find(".tishia").hide();
        		$('#show_staffno_list_div').find("#tishia").show();//显示无数据提示
            }
        }
	});
}

//角色赋菜单权限提交方法
function selectStaffnoSubmit(){
	var staffNo = $('#show_staffno_list_div').find("input[name='radio_staffId']:checked").attr("staffNo");
	var staffId = $('#show_staffno_list_div').find("input[name='radio_staffId']:checked").val();
	var staffName = $('#show_staffno_list_div').find("input[name='radio_staffId']:checked").attr("staffName");
	
	if (staffId == null) {
		messageController.alert("请选择数据！");
	} else{
		closeAddMenuToRole();//关闭窗口
		$('#add_update_staffno_view').find("input[name='staffNo']").val(staffNo);
		$('#add_update_staffno_view').find("input[name='staffId']").val(staffId);
		$('#add_update_staffno_view').find("input[name='staffName']").val(staffName);
	}
}

/*关闭*/
$('#show_staffno_list_div').find(".tanc_gb a").click(function(event) {
	closeAddMenuToRole();
});

function closeAddMenuToRole(){
	$("#show_staffno_list_div").css('display', 'none');
    $("#addMenuToRoleListData").html(null);
	$('#show_staffno_list_div').find('#pagination-demo').empty();
	$('#show_staffno_list_div').find(".tishia").show();
	$('#show_staffno_list_div').find("#tishia").hide();
}

</script>