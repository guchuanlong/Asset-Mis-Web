<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="pMenuList_div" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
	<div class="tanc_bg"></div>
	<div class="tanc_nr">
		<div class="tanc_gb">
			<a href="javascript:closePidMenuDialog();"><img src="${_template_path }/images/close2.png"></a>
		</div>
		<div class="page-header">
           	<h4>选择上级菜单</h4>
       	</div>
       	
       	<!--查询区域-->
		<div class="query_qeint">
			<ul >
				<li>菜单名称：</li><li><input class="query_input" type="text" id="menuPName"></li>
				<li class="query_btn">
					<a href="javascript:searchMenuPid()">查询</a>
				</li>
			
			</ul>
		</div>
		
		<!-- 数据区域 -->
		<div class="table_tc">
			<table border="0" width="100%">
		       	<tr class="tc_tr">
				         <td height="35" align="center" valign="middle">选择</td>
				         <td height="35" align="center" valign="middle">菜单ID</td>
				         <td height="35" align="center" valign="middle">菜单名称</td>
				         <td height="35" align="center" valign="middle">菜单URL</td>
				         <td height="35" align="center" valign="middle">生效时间</td>
				         <td height="35" align="center" valign="middle">失效时间</td>
		        </tr>
		        <tbody id="menuPidListData"></tbody>
			</table>
		    <div class="tishia" style="padding-left: 240px;">
				<p>
					<img src="${_template_path}/images/this1.png">
				</p>
				<span>请点击查询，查看菜单相关信息！</span>
			</div>
			<div id="tishia" class="tishia" style="display: none; padding-left: 240px;">
				<p>
					<img src="${_template_path}/images/this2.png">
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
		        <li><button type="button" class="form_btn" onclick="closePidMenuDialog()">取消</button></li>
		        <li><button type="button" class="form_btn" onclick="setPidMenu()">确定</button></li>
		    </ul>
		</div>
	</div>
</div>
<!-- 定义JsRender模版 -->
<script id="menuPidListDataTmpl" type="text/x-jsrender">
	<tr>
		<td height="35" align="center" valign="middle"><input type="radio" name="radio_MenuPidCode" id="radio_MenuPidCode" value="{{:menuId}}" value_menuPidName={{:menuName}}></td>
        <td height="35" align="center" valign="middle">{{:menuId}}</td>
        <td height="35" align="center" valign="middle">{{:menuName}}</td>
        <td height="35" align="center" valign="middle">{{:~subStrLessThan30(menuUrl)}}</td>
        <td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', activeTime)}}</td>
        <td height="35" align="center" valign="middle">{{:~timestampToDate('yyyy-MM-dd', inactiveTime)}}</td>
	</tr>
</script>
<script type="text/javascript" >
// 选择上级菜单
function setPidMenu(){
	var menuPid = $("input[name='radio_MenuPidCode']:checked").val();
	var menuPidName=$("input[name='radio_MenuPidCode']:checked").attr('value_menuPidName')
	$("#menuDialog").find("#menuPid").val(menuPid);
	$("#menuDialog").find("#menuPidName").val(menuPidName);
	closePidMenuDialog();
}
//查询按钮
var isMenuPidSearchFlag = true;
function searchMenuPid() {
	if(isMenuPidSearchFlag){
		isMenuPidSearchFlag = false;
		refreshMenuPidList();
		isMenuPidSearchFlag = true;
	}
}

//刷新列表数据、处理分页信息
function refreshMenuPidList() {
	
	$('#pMenuList_div').find("#pagination-demo").runnerPagination({
		url: "${_bathPath}/menuManage/queryMeunList",
		method: "POST",
		dataType: "json",
        showWait: true,
        async: false,
        data: {
        	"menuName": function () {
                return jQuery.trim($("#menuPName").val())
            }
        },
       	pageSize: 5,
       	visiblePages:5,
        message: "正在为您查询数据..",
        render: function (data) {
            if(data != null && data != 'undefined' && data.length>0){
            	var template = $.templates("#menuPidListDataTmpl");
            	var htmlOutput = template.render(data);
            	$("#menuPidListData").html(htmlOutput);
            	//隐藏无数据提示
        		$('#pMenuList_div').find(".tishia").hide();
        		$('#pMenuList_div').find("#tishia").hide();
            }else{
            	$("#menuPidListData").html(null);
        		$('#pMenuList_div').find('#pagination-demo').empty();
        		$('#pMenuList_div').find(".tishia").hide();
        		$('#pMenuList_div').find("#tishia").show();//显示无数据提示
            }
        }
	});
}

function getMenuPidList(currentPage) {
    var jsonData;
    var url = "${_bathPath}/menuManage/queryMeunList";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "currentPage": currentPage,
            "menuName": function () {
                return jQuery.trim($("#menuPName").val())
            }
        },
        message: "正在加载数据...",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}


function closePidMenuDialog(){
	$("#pMenuList_div").css('display', 'none');
    $("#menuPidData").html(null);
	$('#pMenuList_div').find('#pagination-demo').empty();
	$('#pMenuList_div').find("#tishia").hide();
	$("input[name='radio_MenuPidCode']:checked").removeAttr('checked')
}

</script>