<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- 新增，修改对话框 -->
<div id="operDialog" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
  <div class="tanc_bg"></div>
  <div class="tanc_nr" style="width:600px;">
   <div class="tanc_gb"><a href="javascript:void(0);" onclick="cancelOper()"><img src="<%=_bathPath%>/images/close2.png"></a></div>
   <div class="page-header">
       <h4 id="operDlgTitle">操作员管理</h4>
   </div>
	   <div class="tanc_ss">
	   		<table>
	   			<tbody>
				    <input type="hidden" id="operId" name="operId"/>
		        	<input id="orgId" name="orgId" type="hidden">
				    <tr>
				        <td class="title"><i class="x_red">*</i>操作员编码：</td>
				        <td class="value">
				            <input type="text" class="form-control" id="operCode" name="operCode"  placeholder="操作员编码"/>
				        </td>
					</tr>
				    <tr>
					   	<td class="title"><i class="x_red">*</i>归属组织类型：</td>
				        <td class="value">
							<select class="form-control" id="orgType" name="orgType" onchange="orgTypeChange();">
			                	<option value="">请选择类型 </option>
			               		<option value="1" >部门</option>
			                	<option value="2" >渠道</option>
			            	</select>
				        </td>
				    </tr>
				    <tr>
				        <td class="title"><i class="x_red">*</i>归属组织：</td>
				        <td class="value">
				            <input type="text" class=" form-control" id="orgName" name="orgName"  placeholder="归属组织编码" onclick="setOrgId();" readonly/>
				        </td>
					</tr>
				</tbody>
			</table>
	   </div>

       <div class="tanc_qy">
        <ul>
        	<li><button id="cancelBtn" type="button" class="form_btn" onclick="cancelOper()">关闭</button></li>
          	<li><button id="submitBtn" type="button" class="form_btn" onclick="addOrUpdateOperData()">确认</button></li>
        </ul>
       </div>
  </div>
</div>
<!-- 渠道对话框 begin -->
<%@ include file="/jsp/oper/channelsList.jsp"%>
<!-- 渠道对话框  end  -->
<script type="text/javascript">

/**
 * 根据组织类型，设置不同的组织编码
 */
function setOrgId(){
	var orgType = $("#orgType").val();
	if(orgType==1){
		//类型为部门时 展现部门树
		showDepartTree();
	}else if(orgType==2){
		//类型为渠道时
		showChannelsList();
	}
}

/**
 * 展示渠道列表对话框
 */
function showChannelsList(){
	$("#channelsList_div").css('display', 'block');
}


/**
 * 展现部门树码表
 */
function showDepartTree() {
    messageController.showWait("正在加载部门信息...");
    $.get('${_bathPath}/commonManage/departtree', function (data) {
        messageController.hideWait();
        bootbox.alert(data, function () {
            var departId = $("#selectDepartId").val();
            var departName = $("#selectDepartName").val();
            $("#orgName").val(departName);
            $("#orgId").val(departId);
        });
    });
}


/** 组织类型控制归属组织选码框 **/
function orgTypeChange(){
	$("#orgId").val(null);
	$("#orgName").val(null);
}

function checkOperData(){
	var errorMsg = "";
	var operCode = $.trim($("#operCode").val());
    var orgType = $.trim($("#orgType").val());	
    var orgId = $.trim($("#orgId").val());
    var orgName = $.trim($("#orgName").val());
    if(operCode == null || operCode == ""){
    	errorMsg +="【操作员编码】不能为空！<br>";
    }else if(operCode.length>32){
    	errorMsg +="【操作员编码】不能大于32个字符！<br>";
    }
    if(orgType == null || orgType == ""){
    	errorMsg +="【归属组织类型】不能为空！<br>";
    }
    if(orgId == null || orgId == "" || orgName == null || orgName == ""){
    	errorMsg +="【归属组织】不能为空！<br>";
    }
    if(errorMsg == ""){
    	return true;
    }else{
    	errorMsg = "填写信息错误 ，内容如下：<br>" + errorMsg;
		messageController.alert(errorMsg);
		return false;
    }
}

/* 新增或更新操作员信息*/
function addOrUpdateOperData(){
	if(checkOperData()){
		//$("#operDialog").css('display', 'none');
		var url = "${_bathPath}/operManage/addOrUpDateOperInfo";
		var data = getOperDialogData();
	   	ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: data,
	        message: "正在处理数据..",
	        success: function (data) {
	        	messageController.alert(data.statusInfo,function(){
	        		cancelOper();
		        	//刷新列表
	        		refreshOperList();
	        	});
	        }
	    });
	}
}

function getOperDialogData(){
	 return {
		"operId": function () {return $.trim($("#operId").val())},
		"operCode": function () {return $.trim($("#operCode").val())},
	    "orgType": function () {return $.trim($("#orgType").val())},	
	    "orgId": function () {return $.trim($("#orgId").val())},	
		"staffnoId": function () {return $("input[name='radio_staffnoId']:checked").val()}
	 };
}


/*取消关闭弹框*/
function cancelOper(){
	$("#operDialog").find("#operId").val(null);
	$("#operCode").val(null);
    $("#orgType").val(null);	
    $("#orgId").val(null);
    $("#orgName").val(null);
	$("#operDialog").css('display', 'none');
}
</script>