$(document).ready(function(){
	$("#searchAreaData").bind("click",function(){
		$("#pagination-test").runnerPagination({
 			url: _base+"/maintain/area/query",
 			method: "POST",
 			dataType: "json",
            showWait: true,
            data: {
                "areaCode": $.trim($("#areaCode").val()),
                "areaName": $.trim($("#areaName").val()),
                "parentAreaCode": $.trim($("#parentAreaCode").val())
            },
           	pageSize: 10,
           	visiblePages:5,
            message: "正在为您查询数据..",
            render: function (data) {
            	if(data != null && data != 'undefined' && data.length>0){
            		var template = $.templates("#areaDataTmpl");
                    var htmlOutput = template.render(data);
                    $("#areaData").html(htmlOutput);
                   //隐藏无数据提示
	            	$("#queryTip").hide();
	            	$("#noDataTip").hide();
            	}else{
            		$("#areaData").html(null);
            		$('#pagination-test').empty();
            		$("#queryTip").hide();
            		$("#noDataTip").show();//显示无数据提示
            	}
               
            }
 		});
	});
})		
		function addArea(){
			window.location.href = _base+"/maintain/area/addPage";
		}		
		
		function editArea(areaCode){
		    window.location.href = _base+"/maintain/area/editPage?areaCode="
		            + areaCode;
		}
		
		function deleteArea(){
			if ($("input[type=checkbox][name='areaCodeCheck']:checked").length < 1) {
		        bootbox.alert("至少选择一个小区进行操作");
		        return;
		    }
		
		    var areaCodes = [];
		    $("input[type=checkbox][name='areaCodeCheck']:checked").each(function (index) {
		        areaCodes[index] = ($(this).val());
		    });
		    ajaxController.ajax({
		        method: "POST",
		        url: _base+"/maintain/area/delArea",
		        dataType: "json",
		        showWait: true,
		        data: {
		            "areaCodes": areaCodes
		        },
		        message: "正在加载数据..",
		        success: function () {
		            refreshAreaData();
		        }
		    }); //end of ajax
		
		}

        function showArea() {
            messageController.showWait("正在加载树...");
            $.get(_base+'/maintain/area/area_tree', function (data) {
                messageController.hideWait();
                var options={
                		title : "区域",
        				message : data,
        				callback:function () {
        					var areaName = $("#selectAreaName").val();
        		            $("#parentAreaName").val(areaName);
        		            var areaCode = $("#selectAreaCode").val();
        		            $("#parentAreaCode").val(areaCode);
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

        function refreshAreaData() {
        	$("#pagination-test").runnerPagination({
     			url: _base+"/maintain/area/query",
     			method: "POST",
     			dataType: "json",
                showWait: true,
                data: {
                    "areaCode": $.trim($("#areaCode").val()),
                    "areaName": $.trim($("#areaName").val()),
                    "parentAreaCode": $.trim($("#parentAreaCode").val())
                },
               	pageSize:10,
               	visiblePages:5,
                message: "正在为您查询数据..",
                render: function (data) {
                	if(data != null && data != 'undefined' && data.length>0){
                		var template = $.templates("#areaDataTmpl");
                        var htmlOutput = template.render(data);
                        $("#areaData").html(htmlOutput);
                       //隐藏无数据提示
    	            	$("#queryTip").hide();
    	            	$("#noDataTip").hide();
                	}else{
                		$("#areaData").html(null);
                		$('#pagination-test').empty();
                		$("#queryTip").hide();
                		$("#noDataTip").show();//显示无数据提示
                	}
                }
     		});
        }