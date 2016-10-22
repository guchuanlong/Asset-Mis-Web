$(document).ready(function () {
            $("#addstaff_back").click(function () {                
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
        function addStaff(){
    		
    		var staffName = $("#staffName").val();
    		var staffNo = $("#staffNo").val();
    		var departId= $("#departId").val();
    		var startDate = $("#startDate").val();
    		var endDate = $("#endDate").val();
    		var staffClass = $("#staffClass").val();
    		var positionCode = $("#positionCode").val();
    		var contactTel = $("#contactTel").val();
    		var email = $("#email").val();
    		var address = $("#address").val();
    		var postcode = $("#postcode").val();
    		if(staffName==""){
    			messageController.alert("员工名称不允许为空！");
    			return false;
    		}else if(staffNo==""){
    			messageController.alert("员工工号不允许为空！");
    			return false;
    		}else if(departId==""){
    			messageController.alert("所属部门不允许为空！");
    			return false;
    		}else if(startDate==""){
    			messageController.alert("生效时间不允许为空！");
    			return false;
    		}else if(endDate==""){
    			messageController.alert("失效时间不允许为空！");
    			return false;
    		}else if(staffClass==""){
    			messageController.alert("员工类别不允许为空！");
    			return false;
    		}
    		
    		 var mobileReg = /^0?1[3|4|5|8][0-9]\d{8}$/; 
    		 var telReg=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
    		
    		if(contactTel!=""){
    			if (mobileReg.test(contactTel)==false && telReg.test(contactTel)==false){
    				messageController.alert("联系电话格式错误，请重新输入！");
    				return false;
    			 }
    			
    		}
    		var res = /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})+$/;
    		if(email!=""){
    			if (res.test(email)==false){
    				messageController.alert("联系邮箱格式错误，请重新输入！");
    				 return false;
    			 }
    		}
    		if(postcode!=""){
 			   var post= /^[1-9][0-9]{5}$/;
 			   if(post.test(postcode)==false){
 				  messageController.alert("邮编格式错误，请重新输入！"); 
 				   return false;
 			   }
 		   }
 		
    		var url = _base+"/maintain/staff/addStaff";
    		var param;
    		param={
    				 staffName: $("#staffName").val(),
    	    		 staffNo: $("#staffNo").val(),
    	    		 departId:  $("#departId").val(),
    	    		 activityDate : $("#startDate").val(),
    	    		 inactivityDate : $("#endDate").val(),
    	    		 staffClass : $("#staffClass").val(),
    	    		 positionCode : $("#positionCode").val(),
    	    		 contactTel : $("#contactTel").val(),
    	    		 email : $("#email").val(),
    	    		 address : $("#address").val(),
    	    		 postcode : $("#postcode").val()
    			};
    		 ajaxController.ajax({
    		        method: "POST",
    		        url: url,
    		        dataType: "json",
    		        showWait: true,
    		        async: false,
    		        data: param,
    		        message: "正在加载数据..",
    		        success: function (data) {
    		        	bootbox.alert(data.statusInfo);
    		        	window.location.href=_base+"/maintain/staff/main";
    		        },
    	             error:function(XMLHttpRequest, textStatus, errorThrown) {
    					bootbox.alert("编辑失败!");
    				}
    		    }); 
    	}
    	