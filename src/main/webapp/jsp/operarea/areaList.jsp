<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="areaList_div" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
	<input name="operId" type="hidden">
	<div class="tanc_bg"></div>
	<div class="tanc_nr">
		<div class="tanc_gb">
			<a href="javascript:void(0);"><img src="<%=_bathPath%>/images/close2.png"></a>
		</div>
		<div class="page-header">
           	<h4>操作员赋区域</h4>
       	</div>
		<div >
				<div class="query_tan">
				<ul>
				<li>
					<p class="new_xinz">
						省份：<select class="query_input" id="serch_provinceCode" name="serch_provinceCode" onchange="selectAera('serch_provinceCode','2','serch_cityCode')">
							<option value=''>所在省份</option>
						</select>
					</p>
				</li>	
				<li >
					
					
						城市：<select class="query_input" id="serch_cityCode" name="serch_cityCode">
							<option value=''>所在地市</option>
						</select>
					
				</li>
				<li >
					区域名称：<input type="text" class="query_input" id="serch_areaName">
				</li>
				<li class="query_btn">
				<a href="javascript:searchAddAreaToOper()">查询</a>
				</li>
				</ul>
								
				
				</div>
		</div>
		<div class="table_tc">
			<table border="0" width="100%">
		       	<tr class="tc_tr">
			         <td height="35" align="center" valign="middle">选择</td>
			         <td height="35" align="center" valign="middle">省份</td>
			         <td height="35" align="center" valign="middle">城市</td>
			         <td height="35" align="center" valign="middle">区域</td>
		        </tr>
		        <tbody id="addAreaToOperListData"></tbody>
			</table>
		    <div class="tishia" style="padding-left: 240px;">
				<p>
					<img src="${_bathPath}/images/this1.png">
				</p>
				<span>请点击查询，查看相关信息！</span>
			</div>
			<div id="tishia" class="tishia" style="display: none; padding-left: 240px;">
				<p>
					<img src="${_bathPath}/images/this2.png">
				</p>
				<span>sorry,没有找到符合你要求的信息!</span>
			</div>
			<!-- 分页信息 -->
			<!-- <div class="pagination-sm" id="pagination-demo"></div> -->
	 				<div>
						<nav style="text-align:center">
						<ul id="_pagination-demo">

						</ul>
						</nav>
					</div>
		</div>
		<div class="tanc_qy">
		    <ul>
		        <li><button type="button" class="form_btn" onclick="addAreaToOperSubmit()">提交</button></li>
		    </ul>
		</div>
	</div>
</div>
<!-- 定义JsRender模版 -->
<script id="addAreaToOperDataTmpl" type="text/x-jsrender">
	<tr>
		<td height="35" align="center" valign="middle"><input type="checkbox" name="checkbox" value="{{:areaCode}}"></td>
		<td height="35" align="center" valign="middle">{{:provinceName}}</td>
		<td height="35" align="center" valign="middle">{{:cityName}}</td>
        <td height="35" align="center" valign="middle">{{:areaName}}</td>
		<td style="display:none">{{:provinceCode}}</td>
		<td style="display:none">{{:cityCode}}</td>
	</tr>
</script>
<script type="text/javascript" >
$(document).ready(function(){
	selectAera('', '1', 'serch_provinceCode');
});

/**省市码表展示**/
function selectAera(code, areaLevel, itemName) {
	var parentAreaCode;
	if (code == '') {
		parentAreaCode = "";
	} else {
		parentAreaCode = $("#" + code).val();
	}

	$.ajax({
		url : '${_bathPath}/commonManage/getArea',
		type : "post",
		async : true,
		data : {
			'areaLevel' : areaLevel,
			'parentAreaCode' : parentAreaCode
		},
		dataType : "html",
		timeout : "10000",
		error : function() {
			alert("服务加载出错");
		},
		success : function(data) {
			var json = eval(data);
			var selObj = $("#areaList_div").find("#" + itemName);
			selObj.empty();
			selObj.append("<option value=''>请选择</option>");
			$.each(json, function(index, item) {
				//循环获取数据    
				var areaCode = json[index].areaCode;
				var areaName = json[index].areaName;
				selObj.append("<option value='"+areaCode+"'>" + areaName
						+ "</option>");

			});
		}
	});

}

//赋区域权限弹框
var isAreaToOperFlag = true;
function selectAreaToOper(){
	if(isAreaToOperFlag){
		isAreaToOperFlag = false;
		//修改title
		var operCode = $("#operArea_oper").find("input[name='radio_operId']:checked").attr("operCode");
		$("#areaList_div").find("h4").text("操作员［" + operCode + "］赋区域");
		var operId = $("input[name='radio_operId']:checked").val();
		if(operId != null && operId != 'undefined'){
			$("#areaList_div").css('display', 'block');
		}else{
			messageController.alert("请选择操作员！");
		}
		isAreaToOperFlag = true;
	}
}

//查询按钮
var isAddAreaToOperSearchFlag = true;
function searchAddAreaToOper() {
	if(isAddAreaToOperSearchFlag){
		isAddAreaToOperSearchFlag = false;
    	refreshAddAreaToOperData(1);
        isAddAreaToOperSearchFlag = true;
	}
}

//刷新列表数据、处理分页信息
function refreshAddAreaToOperData(pageNo) {
	  var url = "${_bathPath}/operAreaManage/queryCanAddAreaList";
	$("#_pagination-demo").runnerPagination({
			url: url,
			method: "POST",
			dataType: "json",
        showWait: true,
        data: {
        	"provinceCode":function () {
                return jQuery.trim($("#serch_provinceCode").val())
            },
            "cityCode":function () {
                return jQuery.trim($("#serch_cityCode").val())
            },
            "areaName": function () {
                return jQuery.trim($("#serch_areaName").val())
            },
            "operId":function(){
            	return $("input[name='radio_operId']:checked").val()
            }
        },
       	pageSize: 5,
       	visiblePages:5,
        message: "正在为您查询数据..",
        render: function (data) {
             if(data != null && data != 'undefined' && data.length>0){
  	        	$("#areaList_div").find(".tishia").hide();
  	        	$("#areaList_div").find("#tishia").hide();
  	        	var template = $.templates("#addAreaToOperDataTmpl");
  	        	var htmlOutput = template.render(data);
  	        	$("#addAreaToOperListData").html(htmlOutput);
  	        }else{
  	        	$("#addAreaToOperListData").html(null);
          		$('#_pagination-demo').empty();
          		$("#areaList_div").find(".tishia").hide();
          		$("#areaList_div").find("#tishia").show();//显示无数据提示
  	        }
        }
		});
	//查询数据并展示在页面
   /*  var jsondata = getAddAreaToOperData(pageNo);
	if(jsondata != null && jsondata != 'undefined' && jsondata.result!=null && jsondata.result.length>0){
		//隐藏无数据提示
		$('#areaList_div').find(".tishia").hide();
		$('#areaList_div').find("#tishia").hide();
		
		//展示list数据
	    var template = $.templates("#addAreaToOperDataTmpl");
	    var htmlOutput = template.render(jsondata.result);
	    $("#addAreaToOperListData").html(htmlOutput);
	    
		//处理分页信息
	    var pager = jsondata.pager;
	    $('#areaList_div').find('#pagination-demo').empty();
        $('#areaList_div').find('#pagination-demo').removeData("twbs-pagination");
        $('#areaList_div').find('#pagination-demo').unbind("page");
	    $('#areaList_div').find('#pagination-demo').twbsPagination({
	        totalPages: pager.totalPages,
	        visiblePages: 5,
	        onPageClick: function (event, page) {
	            var jsondata = getAddAreaToOperData(page);
	            var pager = jsondata.pager;
	            var template = $.templates("#addAreaToOperDataTmpl");
	            var htmlOutput = template.render(jsondata.result);
	            $("#addAreaToOperListData").html(htmlOutput);
	        }
	    });
	}else{
		$("#addAreaToOperListData").html(null);
		$('#areaList_div').find('#pagination-demo').empty();
		$('#areaList_div').find(".tishia").hide();
		$('#areaList_div').find("#tishia").show();//显示无数据提示
	} */
}

//获取区域列表
function getAddAreaToOperData(currentPage) {
	var jsonData;
    var url = "${_bathPath}/operAreaManage/queryCanAddAreaList";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "currentPage": currentPage,
            "provinceCode":function () {
                return jQuery.trim($("#serch_provinceCode").val())
            },
            "cityCode":function () {
                return jQuery.trim($("#serch_cityCode").val())
            },
            "areaName": function () {
                return jQuery.trim($("#serch_areaName").val())
            },
            "operId":function(){
            	return $("input[name='radio_operId']:checked").val()
            }
        },
        message: "正在加载数据...",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}

//角色赋菜单权限提交方法
function addAreaToOperSubmit(){
	//获取参数
	var operId = $("input[name='radio_operId']:checked").val();
	//构建参数数组
	var roleMenuParamsList = new Array();
	$("#areaList_div").find("input[name=checkbox]").each(function() {
		if($(this).prop("checked")) {
			var areaCode = $(this).val();
			roleMenuParamsList.push({
				"operId" : operId,
				"areaCode" : areaCode
			});
		}
	});
	
	//发送处理请求
	if (roleMenuParamsList.length == 0) {
		messageController.alert("请选择要添加的数据！");
	} else{
		closeAddAreaToOper();//关闭窗口
		ajaxController.ajax({
	        method: "POST",
	        url: "${_bathPath}/operAreaManage/addAreaToOper",
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "areaOperListStr": JSON.stringify(roleMenuParamsList)
	        },
	        message: "正在加载数据...",
	        success: function (data) {
            	messageController.alert(data.statusInfo,function(){
            		searchOperAreaList(1);
            	});
	        }
	    });
	}
}

/*关闭*/
$('#areaList_div').find(".tanc_gb a").click(function(event) {
	closeAddAreaToOper();
});

function closeAddAreaToOper(){
	$("#areaList_div").css('display', 'none');
    $("#addAreaToOperListData").html(null);
	$('#areaList_div').find('#_pagination-demo').empty();
	$('#areaList_div').find(".tishia").show();
	$('#areaList_div').find("#tishia").hide();
	$("#serch_provinceCode").val(null);
	$("#serch_cityCode").val(null);
	$("#serch_areaName").val(null);
}

</script>