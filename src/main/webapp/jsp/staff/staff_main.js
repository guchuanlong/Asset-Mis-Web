   $(document).ready(function(){
		$("#searchStaffData").bind("click",function(){
			$("#pagination-test").runnerPagination({
	 			url: _base+"/maintain/staff/query",
	 			method: "POST",
	 			dataType: "json",
	            showWait: true,
	            data: {
	                "staffName": $.trim($("#staffName").val()),
	                "staffNo": $.trim($("#staffNo").val()),
	                "departId": $.trim($("#searchDepartId").val())
	            },
	           	pageSize: 10,
	           	visiblePages:5,
	            message: "正在为您查询数据..",
	            render: function (data) {
	            	if(data != null && data != 'undefined' && data.length>0){
	            		var template = $.templates("#staffDataTmpl");
	 	                var htmlOutput = template.render(data);
	 	                $("#staffData").html(htmlOutput);
	 	                //隐藏无数据提示
	            		$("#queryTip").hide();
	            		$("#noDataTip").hide();
	            	}else{
	            		$("#staffData").html(null);
	            		$('#pagination-test').empty();
	            		$("#queryTip").hide();
	            		$("#noDataTip").show();//显示无数据提示
	            	}
	            }
	 		});
		});
		 $("#editstaff_back").click(function () {                
             window.location.href = _base+"/maintain/staff/main";
         });
		 
		 //页面自动查询
		 $("#searchStaffData").click();
	})
function showDepart() {
	messageController.showWait("正在加载部门树...");
	$.get(_base+'/maintain/depart/tree', function(data) {
		messageController.hideWait();
		var options={
        		title : "部门",
				message : data,
				callback:function () {
					var departName = $("#selectDepartName").val();
					$("#departName").val(departName);
					var departId = $("#selectDepartId").val();
					$("#searchDepartId").val(departId);
                },
                buttons:{
                	ok:{
                		label:'确定'
                	}
                }
        };
        bootbox.alert(options);
        
	});
}


function addStaff(){
	window.location.href = _base+"/maintain/staff/addPage";
}

function editStaff(staffId){
	window.location.href = _base+"/maintain/staff/editPage?staffId="
			+ staffId;
}
function detailStaff(staffId){
	window.location.href = _base+"/maintain/staff/detailPage?staffId="
	+ staffId;
}


function deleteStaff(){
	if ($("input[type=checkbox][name='staffIdCheck']:checked").length < 1) {
		bootbox.alert("至少选择一个员工进行操作");
		return;
	}
	var staffIds = [];
	$("input[type=checkbox][name='staffIdCheck']:checked").each(
		function(index) {
		   staffIds[index] = ($(this).val());
	});
	ajaxController.ajax({
				method : "POST",
				url : _base+"/maintain/staff/delStaff",
				dataType : "json",
				showWait : true,
				data : {
					"staffIds" : staffIds
				},
				message : "正在加载数据..",
				success : function() {
					refreshStaffData();
				}
			}); //end of ajax
	}


function refreshStaffData() {
	$("#pagination-test").runnerPagination({
			url: _base+"/maintain/staff/query",
			method: "POST",
			dataType: "json",
			showWait: true,
	        data: {
	            "staffName": $.trim($("#staffName").val()),
	            "staffNo": $.trim($("#staffNo").val()),
	            "departId": $.trim($("#searchDepartId").val())
	        },
	       	pageSize: 10,
	       	visiblePages:5,
	        message: "正在为您查询数据..",
	        render: function (data) {
	            if(data != null && data != 'undefined' && data.length>0){
            		var template = $.templates("#staffDataTmpl");
 	                var htmlOutput = template.render(data);
 	                $("#staffData").html(htmlOutput);
 	                //隐藏无数据提示
            		$("#queryTip").hide();
            		$("#noDataTip").hide();
            	}else{
            		$("#staffData").html(null);
            		$('#pagination-test').empty();
            		$("#queryTip").hide();
            		$("#noDataTip").show();//显示无数据提示
            	}
	        }
		});
}