<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 新增、修改、详情窗口 -->
<div id="add_update_staffno_view" class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
	<div class="tanc_bg"></div>
	<div class="tanc_nr">
		<div class="tanc_gb">
			<a href="javascript:void(0);" onclick="cancel()"><img src="${_template_path }/images/close2.png"></a>
		</div>
		<div class="page-header">
            <h4 id="topTitle">工号管理</h4>
        </div>
		<div class="tanc_ss">
			<table>
				<tbody>
					<input name="staffnoId" type="hidden" />
					<tr>
	                    <td class="title"><i class="x_red">*</i>员工工号：</td>
	                    <td class="value">
	                        <input type="text" class="form-control" name="staffNo" onclick="showStaffnoList();" readonly/>
	                    </td>
	                    <td class="title" id="staffPasswd_title"><i class="x_red">*</i>工号密码：</td>
	                    <td class="value">
	                        <input type="text" class="form-control" name="staffPasswd"/>
	                    </td>
	                </tr>
				    <tr>
	                    <td class="title"><i class="x_red">*</i>员工ID：</td>
	                    <td class="value">
	                        <input type="text" class="form-control" name="staffId" onclick="showStaffnoList();" readonly/>
	                    </td>
	                    <td class="title"><i class="x_red">*</i>员工姓名：</td>
	                    <td class="value">
	                        <input type="text" class="form-control" name="staffName" onclick="showStaffnoList();" readonly/>
	                    </td>
	                </tr>
	       			<tr>
			        		<td class="title"><i class="x_red">*</i>生效时间：</td>
					        <td class="value">
                				<span class="input-group">
									<input class="form-time" type="text" endDate=$dp.$('endDate'); onClick="WdatePicker({onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}'});" id="startDate" name="activeTime" readonly/>
					            	<span class="sp_h"><i class="icon-calendar"></i></span>
					            </span>
					        </td>
					        <td class="title"><i class="x_red">*</i>失效时间：</td>
					        <td class="value">
				                <span class="input-group">
									<input class="form-time" type="text" onClick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'});" id="endDate" name="inactiveTime" readonly/>
					            	<span class="sp_h"><i class="icon-calendar"></i></span>
					            </span>
					        </td>
					</tr>
	                <!-- <tr>
				        <td class="title"><i class="x_red">*</i>生效时间：</td>
				        <td class="value">
			                <span id="activeTimePicker" class="input-group" data-link-format="yyyy-mm-dd" data-link-field="activeTime">
			                	<input type="text" class="form-time" name="activeTime"  id="activeTime" readonly/>
			                    <span class="sp_h"><i class="icon-calendar"></i></span>
			                </span>
				        </td>
				        <td class="title"><i class="x_red">*</i>失效时间：</td>
				        <td class="value">
			                <span id="inactiveTimePicker" class="input-group" data-link-format="yyyy-mm-dd" data-link-field="inactiveTime">
			                 	<input type="text" class="form-time" name="inactiveTime" id="inactiveTime" readonly/>
			                    <span class="sp_h"><i class="icon-calendar"></i></span>
			                </span>
				        </td>
				    </tr> -->
				    <tr>
	                    <td class="title">备注：</td>
	                    <td class="value">
	                        <textarea name="remarkStr" style="resize: none;" class="form-control"></textarea>
	                    </td>
	                </tr>
	    		</tbody>
	    	</table>
		</div>
		<div class="tanc_qy">
			<ul>
				<li><button type="button" class="form_btn" onclick="cancel()">关闭</button></li>
				<li><button type="button" class="form_btn" id="staffNo_submitBtn" onclick="confirmUpdateStaffNoDate(1)">提交</button></li>
				<li><button type="button" class="form_btn" id="staffNo_modifyBtn" onclick="confirmUpdateStaffNoDate(2)">提交</button></li>
			</ul>
		</div>
	</div>
</div>
<%@ include file="/jsp/staffno/selectStaff_list.jsp"%>

<script type="text/javascript">

function renderDialogDate(){
	//时间插件
    $('#activeTimePicker').datetimepicker({
    	//format: 'yyyy-mm-dd',
		language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        resetBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    }).on("change", function(ev){
    	$('#addStaffNoForm').data('bootstrapValidator').updateStatus('activeTime', 'NOT_VALIDATED', null).validateField('activeTime');
    }).on("changeDate", function(ev){
    	$('#inactiveTimePicker').datetimepicker('setStartDate', $("#activeTime").val());
    });
  	//时间插件
	$('#inactiveTimePicker').datetimepicker({
		//format: 'yyyy-mm-dd',
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        resetBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    }).on("change", function(ev){
    	$('#addStaffNoForm').data('bootstrapValidator').updateStatus('inactiveTime', 'NOT_VALIDATED', null).validateField('inactiveTime');
    }).on("changeDate", function (ev) {
    	$('#activeTimePicker').datetimepicker('setEndDate', $("#inactiveTime").val());
    });
}

//打开操作页面（type：0新增，1查看，2修改）
function addOrUpdateStaffNoFun(type, staffnoId) {
	$("#add_update_staffno_view").css('display', 'block');
	
	if(type == 1){
		$("#topTitle").text("工号详情");
		$("#staffNo_submitBtn").css('display', 'none');
		$("#staffNo_modifyBtn").css('display', 'none');
		$("input[name='activeTime']").attr('disabled', 'disabled');
        $("input[name='inactiveTime']").attr('disabled', 'disabled');
		
		//$('#activeTimePicker').datetimepicker('remove');
		//$('#inactiveTimePicker').datetimepicker('remove');
		
		var staffNoVo = eval(getStaffNoData(staffnoId));
		if(staffNoVo != null){
			$("input[name='staffNo']").val(staffNoVo.staffNo).attr('disabled', 'disabled');
			
	        $("input[name='staffPasswd']").val(staffNoVo.staffPasswd).attr('disabled', 'disabled');
	        $("#staffPasswd_title").hide();
	        $("input[name='staffPasswd']").hide();
	        
	        $("input[name='staffId']").val(staffNoVo.staffId).attr('disabled', 'disabled');
	        $("input[name='staffName']").val(staffNoVo.staffName).attr('disabled', 'disabled');
	        $("textarea[name='remarkStr']").val(staffNoVo.remark).attr('disabled', 'disabled');
	        $("input[name='activeTime']").val(timestampToDate('yyyy-MM-dd', staffNoVo.activeTime));
	        $("input[name='inactiveTime']").val(timestampToDate('yyyy-MM-dd', staffNoVo.inactiveTime));
		}
	}else if(type == 2){
		$("#topTitle").text("修改工号");
		$("#staffNo_submitBtn").css('display', 'none');
		$("#staffNo_modifyBtn").css('display', 'block');
		$("input[name='activeTime']").removeAttr('disabled');
        $("input[name='inactiveTime']").removeAttr('disabled');

		//渲染时间控件
		//renderDialogDate();
		//$('#activeTimePicker').datetimepicker('hide');
		//$('#inactiveTimePicker').datetimepicker('hide');
		
		var staffNoVo = eval(getStaffNoData(staffnoId));
		if(staffNoVo != null){
			$("input[name='staffnoId']").val(staffNoVo.staffnoId);
			$("input[name='staffNo']").val(staffNoVo.staffNo).attr('disabled', 'disabled');
			
	        $("input[name='staffPasswd']").val(staffNoVo.staffPasswd).removeAttr('disabled');
	        $("#staffPasswd_title").hide();
	        $("input[name='staffPasswd']").hide();
	        
	        $("input[name='staffId']").val(staffNoVo.staffId).attr('disabled', 'disabled');
	        $("input[name='staffName']").val(staffNoVo.staffName).attr('disabled', 'disabled');
	        $("textarea[name='remarkStr']").val(staffNoVo.remark).removeAttr('disabled');
	        $("input[name='activeTime']").val(timestampToDate('yyyy-MM-dd', staffNoVo.activeTime));
	        $("input[name='inactiveTime']").val(timestampToDate('yyyy-MM-dd', staffNoVo.inactiveTime));
		}
	}else{
		$("#topTitle").text("新增工号");
		$("#staffNo_submitBtn").css('display', 'block');
		$("#staffNo_modifyBtn").css('display', 'none');
		$("input[name='activeTime']").removeAttr('disabled');
        $("input[name='inactiveTime']").removeAttr('disabled');
		
		//渲染时间控件
		//renderDialogDate();
		//$('#activeTimePicker').datetimepicker('hide');
		//$('#inactiveTimePicker').datetimepicker('hide');
		
		$("input[name='staffnoId']").val(null);
		$("input[name='staffNo']").val(null).removeAttr('disabled');
		
		$("#staffPasswd_title").show();
        $("input[name='staffPasswd']").show().val(null).removeAttr('disabled');
        
        $("input[name='staffId']").val(null).removeAttr('disabled');
        $("input[name='staffName']").val(null).removeAttr('disabled');
        $("textarea[name='remarkStr']").val(null).removeAttr('disabled');
        $("input[name='activeTime']").val(null);
        $("input[name='inactiveTime']").val(null);
	}
}

//获取单条数据
function getStaffNoData(staffnoId) {
    var jsonData;
    var url = "${_bathPath}/staffNoManage/getStaffNoData";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "staffnoId": staffnoId
        },
        message: "正在加载数据...",
        success: function (data) {
            if(data.statusCode == "1"){
	            jsonData = data.data;
            }else{
            	messageController.alert(data.statusInfo);
            }
        }
    });
    return jsonData;
}

/**
 * type 1:新增  2：修改
 */
function checkStaffNoData(type){
	var errorMsg = "";
    var staffNo = $.trim($("input[name='staffNo']").val());
    var staffId = $.trim($("input[name='staffId']").val());
    var staffName = $.trim($("input[name='staffName']").val());
    var activeTimeStr = $.trim($("input[name='activeTime']").val());
    var inactiveTimeStr = $.trim($("input[name='inactiveTime']").val());
    var remark = $.trim($("textarea[name='remarkStr']").val());
    if(staffNo == null || staffNo == ""){
    	errorMsg += "【员工工号】不能为空！<br>";
    }
    if(type == 1){
	    var staffPasswd = $.trim($("input[name='staffPasswd']").val());
	    if(staffPasswd == null || staffPasswd == ""){
	    	errorMsg += "【工号密码】不能为空！<br>";
	    }else if(staffPasswd.length > 20){
	    	errorMsg += "【工号密码】不能超过20个字符！<br>";
	    }
    }
    if(staffId == null || staffId == ""){
    	errorMsg += "【员工ID】不能为空！<br>";
    }
    if(staffName == null || staffName == ""){
    	errorMsg += "【员工姓名】不能为空！<br>";
    }
    if(activeTimeStr == null || activeTimeStr == ""){
    	errorMsg += "【生效时间】不能为空！<br>";
    }
    if(inactiveTimeStr == null || inactiveTimeStr == ""){
    	errorMsg += "【失效时间】不能为空！<br>";
    }
    if(remark != null && remark.length>500){
    	errorMsg += "【备注】内容过长！不应超过500个字符！<br>";
    }
    if(errorMsg == ""){
    	return true;
    }else{
    	errorMsg = "填写信息错误 ，内容如下：<br>" + errorMsg;
		messageController.alert(errorMsg);
		return false;
    }
}

//新增、修改表单提交 type: 1 新增 2 修改
function confirmUpdateStaffNoDate(type){
	if(checkStaffNoData(type)){
	    var url = "${_bathPath}/staffNoManage/addOrUpdateStaffNo";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "staffnoId": function () {
	                return jQuery.trim($("input[name='staffnoId']").val())
	            },
	            "staffNo": function () {
	                return jQuery.trim($("input[name='staffNo']").val())
	            },
	            "staffPasswd": function () {
	                return jQuery.trim($("input[name='staffPasswd']").val())
	            },
	            "staffId": function () {
	                return jQuery.trim($("input[name='staffId']").val())
	            },
	            "staffName": function () {
	                return jQuery.trim($("input[name='staffName']").val())
	            },
	            "remark": function () {
	                return jQuery.trim($("textarea[name='remarkStr']").val())
	            },
	            "activeTimeStr": function () {
	                return jQuery.trim($("input[name='activeTime']").val())
	            },
	            "inactiveTimeStr": function () {
	                return jQuery.trim($("input[name='inactiveTime']").val())
	            }
	            /* ,
	            "state": function () {
	                return jQuery.trim($("#state").val())
	            } */
	        },
	        message: "正在加载数据...",
	        success: function (data) {
	            messageController.alert(data.statusInfo, function(){
	            	//刷新列表
	            	refreshStaffNoList(1);
	            	cancel();
	            });
	        }
	    });
	}
}

//新增、查看、修改表单页面关闭
function cancel(){
	$("input[name='staffnoId']").val(null);
	$("input[name='staffNo']").val(null);
    $("input[name='staffPasswd']").val(null);
    $("input[name='staffId']").val(null);
    $("input[name='staffName']").val(null);
    $("textarea[name='remarkStr']").val(null);
    $("input[name='activeTime']").val(null);
    $("input[name='inactiveTime']").val(null);
	$("#add_update_staffno_view").css('display', 'none');
}

</script>
