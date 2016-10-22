$(document).ready(function () {
	refreshMenuList();
});

//渲染日期字段
function renderDate(){
	//时间插件
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
    	$('#addMenuForm').data('bootstrapValidator').updateStatus('activeTimeStr', 'NOT_VALIDATED', null).validateField('activeTimeStr');
    }).on("changeDate", function(ev){
    	$('#inactiveTimePicker').datetimepicker('setStartDate', $("input[name='activeTimeStr']").val());
    });
  	//时间插件
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
    	$('#addMenuForm').data('bootstrapValidator').updateStatus('inactiveTimeStr', 'NOT_VALIDATED', null).validateField('inactiveTimeStr');
    }).on("changeDate", function (ev) {
    	$('#activeTimePicker').datetimepicker('setEndDate', $("input[name='inactiveTimeStr']").val());
    });
}

//查询
var isSearchFlag = true;
function searchMenuList(){
	if(isSearchFlag){
		isSearchFlag = false;
		refreshMenuList();
        isSearchFlag = true;
	}
}

//新增
var isAddFlag = true;
function addMenuOption(){
	if(isAddFlag){
		isAddFlag = false;
		addOrUpdateMenu(0, null,null);
		isAddFlag = true;
	}
}

function refreshMenuList(){
	$("#pagination-demo").runnerPagination({
		url: _bathPath + "/menuManage/queryMeunList",
		method: "POST",
		dataType: "json",
        showWait: true,
        async: false,
        data: {
            "menuName": function () {
                return jQuery.trim($("#menuName").val())
            }
        },
       	pageSize: 5,
       	visiblePages:5,
        message: "正在为您查询数据..",
        render: function (data) {
            if(data != null && data != 'undefined' && data.length>0){
            	var template = $.templates("#menuListDataTmpl");
            	var htmlOutput = template.render(data);
            	$("#menuListData").html(htmlOutput);
            	//隐藏无数据提示
        		$('#menuList_div').find(".tishia").hide();
        		$('#menuList_div').find("#tishia").hide();
            }else{
            	$("#menuListData").html(null);
        		$('#pagination-demo').empty();
        		$('#menuList_div').find(".tishia").hide();
        		$('#menuList_div').find("#tishia").show();//显示无数据提示
            }
        }
	});
}

/**
 * 展示上级菜单列表
 */
function showPidMenu(){
	$("#pMenuList_div").css('display', 'block');
}

//打开操作页面（type：0新增，1查看，2修改）
function addOrUpdateMenu(type, menuId,menuPid) {
		
	if(type == 1){
		//禁用时间插件（查看用）
//		$('#activeTimePicker').datetimepicker('remove');
//		$('#inactiveTimePicker').datetimepicker('remove');
		var meunVo = eval(getMenuData(menuId));		
		var menuPidVo=eval(getMenuData(menuPid));
		if(menuPidVo!=null){
			$("input[name='menuPidName']").val(menuPidVo.menuName).attr('disabled', 'disabled');
		}else{
			$("input[name='menuPidName']").val("").attr('disabled', 'disabled');
		}	
		$("input[name='menuPid']").val(meunVo.menuPid).attr('disabled', 'disabled');
        $("input[name='menuName']").val(meunVo.menuName).attr('disabled', 'disabled');
        $("input[name='menuDesc']").val(meunVo.menuDesc).attr('disabled', 'disabled');
        $("input[name='menuUrl']").val(meunVo.menuUrl).attr('disabled', 'disabled');
        $("#systemModeId").val(meunVo.systemModeId).attr('disabled', 'disabled');
        $("input[name='activeTimeStr']").val(timestampToDate('yyyy-MM-dd', meunVo.activeTime)).attr('disabled', 'disabled');
        $("input[name='inactiveTimeStr']").val(timestampToDate('yyyy-MM-dd', meunVo.inactiveTime)).attr('disabled', 'disabled');
        $("#systemId").val(meunVo.systemId).attr('disabled', 'disabled');
        //设置系统标识下拉框
        //var systemId = meunVo.systemId;
        //getSystemOption("systemId",systemId,null);
        //设置频道编码下拉框
        //var systemModeId = meunVo.systemModeId;
        //getSystemModeOption("systemModeId",systemModeId,null);
        
        $("#menuDialog").css('display', 'block');
		$("#topTitle").text("菜单详情");
		$("#submitBtn").css('display', 'none');
	}else if(type == 2){
		//renderDate();
		var meunVo = eval(getMenuData(menuId));
		var menuPidVo=eval(getMenuData(menuPid));
		$("input[name='menuId']").val(meunVo.menuId);
		if(menuPidVo!=null){
			$("input[name='menuPidName']").val(menuPidVo.menuName).removeAttr('disabled');
		}else{
			$("input[name='menuPidName']").val("").removeAttr('disabled');
		}		
		$("input[name='menuPid']").val(meunVo.menuPid).removeAttr('disabled', 'disabled');
        $("input[name='menuName']").val(meunVo.menuName).removeAttr('disabled');
        $("input[name='menuDesc']").val(meunVo.menuDesc).removeAttr('disabled');
        $("input[name='menuUrl']").val(meunVo.menuUrl).removeAttr('disabled');
        $("#systemModeId").val(meunVo.systemModeId).removeAttr('disabled');
        $("input[name='activeTimeStr']").val(timestampToDate('yyyy-MM-dd', meunVo.activeTime)).removeAttr('disabled');
        $("input[name='inactiveTimeStr']").val(timestampToDate('yyyy-MM-dd', meunVo.inactiveTime)).removeAttr('disabled');
        $("#systemId").val(meunVo.systemId).removeAttr('disabled');
      	//设置系统模块下拉框
        //var systemId = meunVo.systemId;
        //getSystemOption("systemId",systemId,"请选择");
        //设置频道编码下拉框
        //var systemModeId = meunVo.systemModeId;
        //getSystemModeOption("systemModeId",systemModeId,"请选择");
        
        $("#menuDialog").css('display', 'block');
		$("#topTitle").text("修改菜单");
		$("#submitBtn").css('display', 'block');
	}else{
		//renderDate();
		$("input[name='menuId']").val(null);
		$("input[name='menuPid']").val(null).removeAttr('disabled');
		$("input[name='menuPidName']").val(null).removeAttr('disabled');
        $("input[name='menuName']").val(null).removeAttr('disabled');
        $("input[name='menuDesc']").val(null).removeAttr('disabled');
        $("input[name='menuUrl']").val(null).removeAttr('disabled');
        //$("#systemModeId").val(null).removeAttr('disabled');
        $("input[name='activeTimeStr']").val(null).removeAttr('disabled');
        $("input[name='inactiveTimeStr']").val(null).removeAttr('disabled');
        //$("#systemId").val(null).removeAttr('disabled');
        
      	//设置系统模块下拉框
        //getSystemOption("systemId",null,"请选择");
        //设置频道编码下拉框
        //getSystemModeOption("systemModeId",null,"请选择");
      	
        $("#menuDialog").css('display', 'block');
		$("#topTitle").text("新增菜单");
		$("#submitBtn").css('display', 'block');
	}
}

//获取单条数据
function getMenuData(menuId) {
    var jsonData;
    var url = _bathPath + "/menuManage/getMenuData";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "menuId": menuId
        },
        message: "正在加载数据...",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}

//删除菜单
function delMenu(menuId) {
	messageController.confirm("确认要删除该菜单吗？", function(){
		var url = _bathPath + "/menuManage/delMenu";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "menuId": menuId
	        },
	        message: "正在加载数据...",
	        success: function (data) {
	        	messageController.alert(data.statusInfo, function(){
		        	//刷新列表
		        	refreshMenuList();
	        	});
	        }
	    });
	});
}

//新增、修改表单提交
function confirmAddMenuDate(){
	if(checkDataBefUpdate()){
		$(".tanc_cz").css('display', 'none');
	    var url = _bathPath + "/menuManage/addOrUpdateMenu";
	    var menudata = getUpdateMenuData();
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: menudata,
	        message: "正在加载数据...",
	        success: function (data) {
	            messageController.alert(data.statusInfo, function(){
	            	//刷新列表
	            	refreshMenuList();
	            });
	        }
	    });
	}
}

function getUpdateMenuData(){
	return {
		"menuId": function () {
	        return jQuery.trim($("input[name='menuId']").val())
	    },
	    "menuPid": function () {
	    	var menuPid=$("input[name='menuPid']").val();
	    	if(menuPid==""){
	    		menuPid =-1;
	    	}
	    	return menuPid;           	
	    },
	    "menuName": function () {
	        return jQuery.trim($("input[name='menuName']").val())
	    },
	    "menuDesc": function () {
	        return jQuery.trim($("input[name='menuDesc']").val())
	    },
	    "menuUrl": function () {
	        return jQuery.trim($("input[name='menuUrl']").val())
	    },
	    "systemId": function () {
	        return jQuery.trim($("#systemId").val())
	    },
	    "systemModeId": function () {
	        return jQuery.trim($("#systemModeId").val())
	    },
	    "activeTimeStr": function () {
	        return jQuery.trim($("input[name='activeTimeStr']").val())
	    },
	    "inactiveTimeStr": function () {
	        return jQuery.trim($("input[name='inactiveTimeStr']").val())
	    }
	}
}

/**
 * 数据检查
 * @returns {Boolean}
 */
function checkDataBefUpdate(){
	var errorMsg = "";
    var menuName = jQuery.trim($("input[name='menuName']").val());
    var menuUrl = jQuery.trim($("input[name='menuUrl']").val());
    var menuDesc = jQuery.trim($("input[name='menuDesc']").val())
    var systemId = jQuery.trim($("#systemId").val());
    var systemModeId = jQuery.trim($("#systemModeId").val());
    var activeTimeStr = jQuery.trim($("input[name='activeTimeStr']").val());
    var inactiveTimeStr = jQuery.trim($("input[name='inactiveTimeStr']").val());
    if(menuName == null || menuName == ""){
    	errorMsg +="【菜单名称】不能为空！<br>";
    }else if(menuName.length>30){
    	errorMsg +="【菜单名称】不能超过30个字符！<br>";
    }
    if(menuUrl != null && menuUrl.length>500){
    	errorMsg +="【菜单URL】不能超过500个字符！<br>";
    }
    if(menuDesc != null && menuDesc.length > 250){
    	errorMsg +="【菜单描述】不能超过250个字符！<br>";
    }
    /*if(systemId == ""){
    	errorMsg +="【系统标识】不能为空！<br>";
    }
    if(systemModeId == ""){
    	errorMsg +="【频道名称】不能为空！<br>";
    }*/
    if(activeTimeStr == ""){
    	errorMsg +="【生效时间】不能为空！<br>";
    }
    if(inactiveTimeStr == ""){
    	errorMsg +="【失效时间】不能为空！<br>";
    }
	if(errorMsg !=""){
		errorMsg = "填写信息错误 ，内容如下：<br>" + errorMsg;
		messageController.alert(errorMsg);
		return false;
	}else{
		return true;
	}
}

//新增、查看、修改表单页面关闭
function cancel(){
	$("input[name='menuId']").val(null);
	$("input[name='menuPid']").val(null);
	$("input[name='menuPidName']").val(null);
    $("input[name='menuName']").val(null);
    $("input[name='menuDesc']").val(null);
    $("input[name='menuUrl']").val(null);
    $("#systemModeId").val(null);
    $("input[name='activeTimeStr']").val(null);
    $("input[name='inactiveTimeStr']").val(null);
    $("#systemId").val(null);
	$(".tanc_cz").css('display', 'none');
}

/*//关闭对话框
function closeMenuDialog(){
	$("#menuInfoDialog").css('display', 'none');
    $("#menuInfoDialog").html(null);
	$('#menuInfoDialog').find('#pagination-demo').empty();
    $("#menuDialog").html(null);
	$('#menuDialog').find('#pagination-demo').empty();
	$('#menuDialog').find("#tishia").hide();
}*/