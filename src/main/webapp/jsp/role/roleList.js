$(function(){
	searchRoleData();
});
//渲染日期字段
function renderDate(){
	$('#activeTimePicker').datetimepicker({
    	format: 'yyyy-mm-dd',
		language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        resetBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0//,
		//showMeridian: 1
    }).on("change", function(ev){
    	$('#roleInfo_form').data('bootstrapValidator').updateStatus('activeTime', 'NOT_VALIDATED', null).validateField('activeTime');
    }).on("changeDate", function(ev){
    	$('#inactiveTimePicker').datetimepicker('setStartDate', $("input[name='activeTime']").val());
    });
	$('#inactiveTimePicker').datetimepicker({
		format: 'yyyy-mm-dd',
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        resetBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0//,
        //showMeridian: 1
    }).on("change", function(ev){
    	$('#roleInfo_form').data('bootstrapValidator').updateStatus('inactiveTime', 'NOT_VALIDATED', null).validateField('inactiveTime');
    }).on("changeDate", function (ev) {
    	$('#activeTimePicker').datetimepicker('setEndDate', $("input[name='inactiveTime']").val());
    });
}

//查询按钮
var isSearchFlag = true;
function searchRoleData() {
	if(isSearchFlag){
		isSearchFlag = false;
        $("#pagination-demo").runnerPagination({
    		url: _bathPath + "/roleManage/queryRoleList",
    		method: "POST",
    		dataType: "json",
    	    showWait: true,
    	    async: false,
    	    data: {
    	    	"roleName": function () {
                    return jQuery.trim($("#role_name").val())
                }
    	    },
    	   	pageSize: 5,
    	   	visiblePages:5,
    	    message: "正在为您查询数据..",
    	    render: function (data) {
    	        if(data != null && data != 'undefined' && data.length>0){
    	        	$(".tishia").hide();
    	        	$("#tishia").hide();
    	        	var template = $.templates("#roleListDataTmpl");
    	        	var htmlOutput = template.render(data);
    	        	$("#roleData").html(htmlOutput);
    	        }else{
            		$(".tishia").hide();
            		$("#tishia").show();//显示无数据提示
    	        	$("#roleData").html(null);
            		$('#pagination-demo').empty();
    	        }
    	    }
    	});
        isSearchFlag = true;
	}
}

function checkRoleDialogData(){
	var errorMsg = "";
	//var roleCode = $.trim($("#roleCode").val());	
	var roleName = $.trim($("#roleName").val());	
	//var systemId = $.trim($("#systemId").val());
	//var systemModeId = $.trim($("#systemModeId").val());
	var roleDesc = $.trim($("#roleDesc").val());
	var activeTimeStr = $.trim($("input[name='activeTime']").val());
	var inactiveTimeStr = $.trim($("input[name='inactiveTime']").val());
	/*if(roleCode == null || roleCode == ""){
		errorMsg += "【角色编码】不能为空！<br>";
	}else if(roleCode.length>16){
		errorMsg += "【角色编码】不能大于16个字符！<br>";
	}*/
	if(roleName == null || roleName == ""){
		errorMsg += "【角色名称】不能为空！<br>";
	}else if(roleName.length > 20){
		errorMsg += "【角色名称】不能大于20个字符！<br>";
	}
	/*if(systemId == ""){
		errorMsg += "【系统标识】不能为空！<br>";
	}
	if(systemModeId == ""){ 	
		errorMsg += "【频道名称】不能为空！<br>";
	}*/
	if(roleDesc != null && roleDesc.length >40){
		errorMsg += "【角色描述】不能大于40个字符！<br>";
	}
	if(activeTimeStr == ""){
		errorMsg += "【生效时间】不能为空！<br>";
	}
	if(inactiveTimeStr == ""){
		errorMsg += "【失效时间】不能为空！<br>";
	}
	if(errorMsg == ""){
		return true;
	}else{
		errorMsg = "填写信息错误 ，内容如下：<br>" + errorMsg;
		messageController.alert(errorMsg);
		return false;
	}
}
	
function getRoleDialogData(){
	return {
    	"roleId": function () {return $.trim($("#roleId").val())},
    	//"roleCode": function () {return $.trim($("#roleCode").val())},	
    	"roleName": function () {return $.trim($("#roleName").val())},	
    	"roleDesc": function () {return $.trim($("#roleDesc").val())},	
    	"rolePid": -1,
    	//"systemModeId": function () {return $.trim($("#systemModeId").val())},
    	"activeTimeStr": function () {return $.trim($("input[name='activeTime']").val())},
    	"inactiveTimeStr": function () {return $.trim($("input[name='inactiveTime']").val())}
    	//"systemId":function() {return $.trim($("#systemId").val())}
    };
}

/**添加角色按钮触发事件 弹出对话框*/
var isAddFlag = true;
function addRole(){
	if(isAddFlag){
		isAddFlag = false;
		$("#topTitle").text("角色添加");
		$(".tanc_cz").css('display', 'block');
		$("#submitBtn").css('display', 'block');
		
		$("input[name='roleId']").removeAttr("disabled");
		//$("input[name='roleCode']").removeAttr("disabled");
        $("input[name='roleName']").removeAttr("disabled");
        $("input[name='roleDesc']").removeAttr("disabled");
        $("input[name='activeTime']").removeAttr('disabled');
        $("input[name='inactiveTime']").removeAttr('disabled');
        //$("#systemModeId").removeAttr("disabled");
	    //$("#systemId").removeAttr('disabled');

		//渲染对话框日期字段
		//renderDate();
		//激活禁用的时间插件
		//$('#activeTimePicker').datetimepicker('hide');
		//$('#inactiveTimePicker').datetimepicker('hide');
		$("input[name='roleId']").val(null);
		//$("input[name='roleCode']").val(null);
	    $("input[name='roleName']").val(null);
	    $("input[name='roleDesc']").val(null);
	    //$("#systemModeId").val(null);
	    $("input[name='activeTime']").val(null);
	    $("input[name='inactiveTime']").val(null);
		//$("#systemId").val(null);
		//getSystemOption("systemId",null,"请选择");
		//设置频道编码下拉框
        //getSystemModeOption("systemModeId",null,"请选择");
		isAddFlag = true;
	}
}

/*取消弹框*/
function cancelRole(){
	$("input[name='roleId']").val(null);
	//$("input[name='roleCode']").val(null);
    $("input[name='roleName']").val(null);
    $("input[name='roleDesc']").val(null);
    //$("#systemModeId").val(null);
    $("input[name='activeTime']").val(null);
    $("input[name='inactiveTime']").val(null);
	//$("#systemId").val(null);
	$(".tanc_cz").css('display', 'none');
}

/*点击弹框上的关闭*/
$(".tanc_gb a").click(function(event) {
    $(".tanc_cz").css('display', 'none');
});

/* 新增或更新角色信息*/
function addOrUpdateRoleData(){
	if(checkRoleDialogData()){
		$(".tanc_cz").css('display', 'none');
		var url = _bathPath + "/roleManage/addOrUpDateRoleInfo";
		var data = getRoleDialogData();
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
		        	//刷新列表
	        		searchRoleData();
	        	});
	        }
	    });
	}
}

//获取角色信息 
function gatRoleInfo(roleId){
	var jsonData={};
	if(roleId!=null){
		var url = _bathPath + "/roleManage/getRoleInfo";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "roleId": roleId,
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	jsonData = data.data;
	        }
	    });
	}
    return jsonData;
}

//查看角色详情
var isSearchFlag=true;
function searchRole(roleId) {
	if(isSearchFlag){
		isSearchFlag = false;
		$("#topTitle").text("角色详情");
		$(".tanc_cz").css('display', 'block');
		$("#submitBtn").css('display', 'none');
		
		$("input[name='roleId']").attr("disabled","disabled");
		//$("input[name='roleCode']").attr("disabled","disabled");
        $("input[name='roleName']").attr("disabled","disabled");
        $("input[name='roleDesc']").attr("disabled","disabled");
        $("input[name='activeTime']").attr('disabled', 'disabled');
        $("input[name='inactiveTime']").attr('disabled', 'disabled');
        //$("#systemModeId").attr("disabled","disabled");
        //$("#systemId").attr('disabled', 'disabled');
		
		//禁用时间插件（查看用）
		//$('#activeTimePicker').datetimepicker('remove');
		//$('#inactiveTimePicker').datetimepicker('remove');
		var roleData = gatRoleInfo(roleId);
		var systemId = null;
		var systemModeId = null;
		if(roleData != null && roleData != 'undefined'){
			$("input[name='roleId']").val(roleData.roleId);
			//$("input[name='roleCode']").val(roleData.roleCode);
	        $("input[name='roleName']").val(roleData.roleName);
	        $("input[name='roleDesc']").val(roleData.roleDesc);
	        $("input[name='activeTime']").val(timestampToDate('yyyy-MM-dd', roleData.activeTime));
	        $("input[name='inactiveTime']").val(timestampToDate('yyyy-MM-dd', roleData.inactiveTime));
	        //systemId = roleData.systemId;
	        //$("#systemId").val(systemId);
	        //systemModeId = roleData.systemModeId;
	        //$("#systemModeId").val(systemModeId);
		}
        //设置系统模块下拉框
        //getSystemOption("systemId",systemId,null);
        //设置频道编码下拉框
        //getSystemModeOption("systemModeId",systemModeId,null);
		isSearchFlag = true;
	}
}

//角色修改
var isUpdateFlag = true;
function updateRole(roleId) {
	if(isUpdateFlag){
		isUpdateFlag = false;
		$("#topTitle").text("角色修改");
		$(".tanc_cz").css('display', 'block');
		$("#submitBtn").css('display', 'block');
		
		$("input[name='roleId']").removeAttr("disabled");
		$("input[name='roleCode']").removeAttr("disabled");
        $("input[name='roleName']").removeAttr("disabled");
        $("input[name='roleDesc']").removeAttr("disabled");
        $("input[name='activeTime']").removeAttr('disabled');
        $("input[name='inactiveTime']").removeAttr('disabled');
        $("#systemModeId").removeAttr("disabled");
        $("#systemId").removeAttr('disabled');
        
		//渲染对话框日期字段
		//renderDate();
		//激活禁用的时间插件（修改用）
		//$('#activeTimePicker').datetimepicker('hide');
		//$('#inactiveTimePicker').datetimepicker('hide');
		var roleData = gatRoleInfo(roleId);
		var systemId= null;
		if(roleData != null && roleData != 'undefined'){
			$("input[name='roleId']").val(roleData.roleId);
			$("input[name='roleCode']").val(roleData.roleCode);
	        $("input[name='roleName']").val(roleData.roleName);
	        $("input[name='roleDesc']").val(roleData.roleDesc);
	        $("input[name='activeTime']").val(timestampToDate('yyyy-MM-dd', roleData.activeTime));
	        $("input[name='inactiveTime']").val(timestampToDate('yyyy-MM-dd', roleData.inactiveTime));
	        systemId = roleData.systemId;
	        $("#systemId").val(systemId);
	        systemModeId = roleData.systemModeId;
	        $("#systemModeId").val(systemModeId);
		}
		//设置系统模块下拉框
        getSystemOption("systemId",systemId,"请选择");
        //设置频道编码下拉框
        getSystemModeOption("systemModeId",systemModeId,"请选择");
		isUpdateFlag = true;
	}
}

//角色删除
function delRole(roleId) {
	messageController.confirm("确认要删除该角色吗？", function(){
		var url = _bathPath + "/roleManage/delRoleInfo";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	        	 "roleId": roleId
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	messageController.alert(data.statusInfo, function(){
		        	//刷新列表
	        		searchRoleData();
	        	});
	        }
	    });
	});
	
}
