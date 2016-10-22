 $(document).ready(function(){
		$("#searchDepartData").bind("click",function(){
			$("#pagination-test").runnerPagination({
	 			url: _base+"/maintain/depart/query",
	 			method: "POST",
	 			dataType: "json",
	            showWait: true,
	            data: {
	                "departName": $.trim($("#departName").val()),
	                "parentDepartId": $.trim($("#searchDepartId").val())
	            },
	           	pageSize: 10,
	           	visiblePages:5,
	            message: "正在为您查询数据..",
	            render: function (data) {
	            	if(data != null && data != 'undefined' && data.length>0){
	            		var template = $.templates("#departDataTmpl");
	 	                var htmlOutput = template.render(data);
	 	                $("#DepartList").html(htmlOutput);
	            		//隐藏无数据提示
	            		$("#queryTip").hide();
	            		$("#noDataTip").hide();
	            	}else{
	            		$("#DepartList").html(null);
	            		$('#pagination-test').empty();
	            		$("#queryTip").hide();
	            		$("#noDataTip").show();//显示无数据提示
	            	}
	               
	            }
	 		});
		});
		
		//自动查询
		$("#searchDepartData").click();
	})	
	function addDepart(){
		  window.location.href = _base+"/maintain/depart/addPage";
	}
	function editDepart(departId){
	     window.location.href = _base+"/maintain/depart/editPage?departId="
	             +departId;
	}
	
	function deleteDepart(){
		if ($("input[type=checkbox][name='departIdCheck']:checked").length < 1) {
	        bootbox.alert("至少选择一个部门进行操作");
	        return;
	    }
	    var departIds = [];
	    $("input[type=checkbox][name='departIdCheck']:checked").each(function (index) {
	        departIds[index] = ($(this).val());
	    });
	    ajaxController.ajax({
	        method: "POST",
	        url: _base+"/maintain/depart/delDepart",
	        dataType: "json",
	        showWait: true,
	        data: {
	            "departIds": departIds
	        },
	        message: "正在加载数据..",
	        success: function () {
	            refreshDepartData();
	        }
	    }); //end of ajax

}

        function showDepart() {
            messageController.showWait("正在加载部门树...");
            $.get(_base+'/maintain/depart/tree', function (data) {
                messageController.hideWait();
                var options={
                		title : "部门",
        				message : data,
        				callback:function () {
        					 var departName = $("#selectDepartName").val();
    	                     $("#parentName").val(departName);
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

        function refreshDepartData() {
            $("#pagination-test").runnerPagination({
	 			url: _base+"/maintain/depart/query",
	 			method: "POST",
	 			dataType: "json",
	            showWait: true,
	            data: {
	                "departName": $.trim($("#departName").val()),
	                "parentDepartId": $.trim($("#searchDepartId").val())
	            },
	           	pageSize: 10,
	           	visiblePages:5,
	            message: "正在为您查询数据..",
	            render: function (data) {
	            	if(data != null && data != 'undefined' && data.length>0){
	            		var template = $.templates("#departDataTmpl");
	 	                var htmlOutput = template.render(data);
	 	                $("#DepartList").html(htmlOutput);
	            		//隐藏无数据提示
	            		$("#queryTip").hide();
	            		$("#noDataTip").hide();
	            	}else{
	            		$("#DepartList").html(null);
	            		$('#pagination-test').empty();
	            		$("#queryTip").hide();
	            		$("#noDataTip").show();//显示无数据提示
	            	}
	            }
	 		});
        }