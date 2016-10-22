$(document).ready(function () {
            $("#editstaff_back").click(function () {                
                window.location.href = _base+"/maintain/staff/main";
            });
        });

        function showDepart() {
            messageController.showWait("正在加载部门树...");
            $.get(_base+'/maintain/depart/tree', function (data) {
                messageController.hideWait();
                var options={
                		title : "部门",
        				message : data,
        				callback:function () {
        					    var departName = $("#selectDepartName").val();
        	                    $("#departName").val(departName);
        	                    var departId = $("#selectDepartId").val();
        	                    $("#departId").val(departId);
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
