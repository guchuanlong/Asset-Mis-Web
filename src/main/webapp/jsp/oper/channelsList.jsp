<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="channelsList_div" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
	<div class="tanc_bg"></div>
	<div class="tanc_nr">
		<div class="tanc_gb">
			<a href="javascript:closeChannelsDailog();"><img src="<%=_bathPath%>/images/close2.png"></a>
		</div>
		<div class="page-header" style="padding-bottom: 0px; margin: 0px 0 0px;">
           	<h4>渠道信息</h4>
       	</div>
       	<div  class="query_2params">
		<table>
			<tr>
				<td class="title">省份：</td>
				<td class="value">
						<select class="query_input" id="serch_provinceCode" name="serch_provinceCode" onchange="selectAera('serch_provinceCode','2','serch_cityCode')">
							<option value=''>所在省份</option>
						</select>
				</td>
				<td class="title">城市：</td>
				<td class="value">
						<select class="query_input" id="serch_cityCode" name="serch_cityCode">
							<option value=''>所在地市</option>
						</select>
				</td>
				<td></td>
				</tr>
				<tr>
				<td class="title">渠道编码：</td>
				<td class="value">
						<input id="serch_chnlId" type="text" class="query_input">
				</td>
				<td class="title">渠道名称：</td>
				<td class="value">
						<input id="serch_chnlName" type="text" class="query_input">
				</td>
				<td class="value">
					<p class="query_btn">
						<a  href="javascript:searchChannelsData()">查询</a>
					</p>
				</td>
			</tr>
			<!-- <div style="width:100%;text-align:center;">
				<button id="searchStaffNoBtn" class="form_btn" onclick="searchChannelsData()" style="padding-top:6px;"><i class="icon-search"></i>查询</button>
			</div> -->
		</table>
		</div>
		<div class="table_tc">
			<table border="0" width="100%">
		       	<tr class="tc_tr">
			         <td height="35" align="center" valign="middle">选择</td>
			         <td height="35" align="center" valign="middle">渠道编码</td>
			         <td height="35" align="center" valign="middle">渠道名称</td>
			         <td height="35" align="center" valign="middle">省份</td>
			         <td height="35" align="center" valign="middle">城市</td>
		        </tr>
		        <tbody id="channelsListData"></tbody>
			</table>
		    <div class="tishia" style="padding-left: 240px;">
				<p>
					<img src="${_bathPath}/images/this1.png">
				</p>
				<span>请点击查询，查看渠道相关信息！</span>
			</div>
			<div id="tishia" class="tishia" style="display: none; padding-left: 240px;">
				<p>
					<img src="${_bathPath}/images/this2.png">
				</p>
				<span>sorry,没有找到符合你要求的渠道!</span>
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
		        <li><button type="button" class="form_btn" onclick="setChannelSubmit()">确定</button></li>
		    </ul>
		</div>
	</div>
</div>
<!-- 定义JsRender模版 -->
<script id="channelsListDataTmpl" type="text/x-jsrender">
	<tr>
		<td height="35" align="center" valign="middle"><input type="radio" name="radio_chnlId" id="radio_chnlId" value="{{:chnlId}}" value_mn="{{:chnlName}}"></td>
        <td height="35" align="center" valign="middle">{{:chnlId}}</td>
        <td height="35" align="center" valign="middle">{{:chnlName}}</td>
        <td height="35" align="center" valign="middle">{{:provinceCode}}</td>
        <td height="35" align="center" valign="middle">{{:cityCode}}</td>
	</tr>
</script>
<script type="text/javascript" >
$(document).ready(function(){
	selectAera('', '1', 'serch_provinceCode');
});

//查询按钮
var isSearchChannelsFlag = true;
function searchChannelsData() {
	if(isSearchChannelsFlag){
		isSearchChannelsFlag = false;
    	refreshChannelsData(1);
    	isSearchChannelsFlag = true;
	}
}

//刷新列表数据、处理分页信息
function refreshChannelsData(pageNo) {
	
	$('#channelsList_div').find("#pagination-demo").runnerPagination({
		url: "${_bathPath}/commonManage/getChildrenChannels",
		method: "POST",
		dataType: "json",
	    showWait: true,
	    async: false,
	    data: {
	    	"provinceCode": function () {
            	return jQuery.trim($("#serch_provinceCode").val());
            },
		    "cityCode": function () {
		        return jQuery.trim($("#serch_cityCode").val())
		    },
		    "chnlId": function () {
		        return jQuery.trim($("#serch_chnlId").val())
		    },
		    "chnlName": function () {
		        return jQuery.trim($("#serch_chnlName").val())
		    }
	    },
	   	pageSize: 5,
	   	visiblePages:5,
	    message: "正在为您查询数据..",
	    render: function (data) {
	        if(data != null && data != 'undefined' && data.length>0){
	        	$('#channelsList_div').find(".tishia").hide();
	        	$('#channelsList_div').find("#tishia").hide();
	        	var template = $.templates("#channelsListDataTmpl");
	        	var htmlOutput = template.render(data);
	        	$("#channelsListData").html(htmlOutput);
	        }else{
        		$('#channelsList_div').find(".tishia").hide();
        		$('#channelsList_div').find("#tishia").show();//显示无数据提示
	        	$("#channelsListData").html(null);
        		$('#pagination-demo').empty();
	        }
	    }
	});
}


//设置渠道信息
function setChannelSubmit(){
	var chnlId = $("input[name='radio_chnlId']:checked").val();
	if(chnlId==null){
		messageController.alert("请选择数据！");
	}else{
		var chnlName = $("input[name='radio_chnlId']:checked").attr('value_mn');
		$("#operDialog").find("#orgId").val(chnlId);
		$("#operDialog").find("#orgName").val(chnlName);
		closeChannelsDailog();
	}
}

/**关闭对话框**/
function closeChannelsDailog(){
	$("#channelsList_div").css('display', 'none');
    $("#channelsListData").html(null);
	$('#channelsList_div').find('#pagination-demo').empty();
	$('#channelsList_div').find(".tishia").show();
	$('#channelsList_div').find("#tishia").hide();
   	$("#serch_provinceCode").val(null);
	$("#serch_cityCode").val(null);
	$("#serch_chnlId").val(null);
	$("#serch_chnlName").val(null);
}

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
			var selObj = $("#channelsList_div").find("#" + itemName);
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

</script>