		$(document).ready(function () {
            $("#provinceSelect option:first").after("<option value='00'>全国</option>");
           
            $("#add_back").click(function () {                
                window.location.href = _base+"/maintain/depart/main";
            });
        })

        function showDepart() {
            messageController.showWait("正在加载部门树...");
            $.get(_base+'/maintain/depart/tree', function (data) {
                messageController.hideWait();
                var options={
                		title : "部门",
        				message : data,
        				callback:function () {
        					    var departName = $("#selectDepartName").val();
        	                    $("#parentDepartName").val(departName);
        	                    var departId = $("#selectDepartId").val();
        	                    $("#parentDepartId").val(departId);
        	                    var provinceCode = $("#selectDepartProvinceCode").val();
        	                    $("#provinceSelect").val(provinceCode);
        	                    if(departId !=""){
        	                    	 $('#provinceSelect').prop('disabled', true);
        	                    	 updateCity();
        	                    }
        	                    
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

        function updateCity() {
            var provinceCode = $("#provinceSelect").val();
            if (provinceCode == ""){
                $("#cityCode").empty();
                $("#cityCode").prop('disabled', true);
                return;
            }
            $("#cityCode").prop('disabled', false);
            if (provinceCode == "00") {
                $('#provinceSelect').prop('disabled', false);
                var template = $.templates("#cityCodeSpecialTmpl");
                var htmlOutput = template.render({});
                $("#cityCode").empty();
                $("#cityCode").append(htmlOutput);
                
                
                $('#cityCode').prop('disabled', false);
                var template1 = $.templates("#areaCodeSpecTmpl");
                var htmlOutput1 = template1.render({});
                $("#areaCode").empty();
                $("#areaCode").append(htmlOutput1);
                return;
            }
            var url = _base+"/maintain/area/listcity"
            ajaxController.ajax({
                method: "POST",
                url: url,
                dataType: "json",
                async: false,
                data: {
                    "parentAreaCode": function () {
                        return $("#provinceSelect").val();
                    }
                },
                message: "正在加载数据..",
                success: function (data) {
                    var template = $.templates("#cityCodeTmpl");
                    var htmlOutput = template.render(data.data);
                    $("#cityCode").empty();
                    $("#cityCode").append(htmlOutput);
                    $("#cityCode option:first").before("<option value='0'>请选择</option>");
                }
            }); //end of ajax
        }
        //区域
        function updateDomainCode() {
            var cityCode = $("#cityCode").val();
            if (cityCode == ""){
                $("#areaCode").empty();
                $("#areaCode").prop('disabled', true);
                return;
            }
            $("#areaCode").prop('disabled', false);
            if (cityCode == "000") {
                $('#cityCode').prop('disabled', false);
                var template = $.templates("#areaCodeSpecTmpl");
                var htmlOutput = template.render({});
                $("#cityCode").empty();
                $("#cityCode").append(htmlOutput);
                return;
            }
            var url = _base+"/maintain/area/listcounty"
            ajaxController.ajax({
                method: "POST",
                url: url,
                dataType: "json",
                async: false,
                data: {
                    "parentAreaCode": function () {
                        return $("#cityCode").val();
                    }
                },
                message: "正在加载数据..",
                success: function (data) {
                    var template = $.templates("#areaCodeTmpl1");
                    var htmlOutput = template.render(data.data);
                   //if(htmlOutput==""){
                	  // $("#areaCode option:first").after("<option value='0000'>请选择</option>");
                	  // $("#areaCode").val("0000");
                   //}else{
                	   $("#areaCode").empty();
                       $("#areaCode").append(htmlOutput);
                       $("#areaCode option:first").before("<option value='0'>请选择</option>");
                   //}
                    
                  
                }
            }); //end of ajax
        }
        
        
function addDepart(){
    		
    		var departName = $("#departName").val();
    		var departKindType = $("#departKindType").val();
    		var parentDepartId= $("#parentDepartId").val();
    		var contactName = $("#contactName").val();
    		var contactTel = $("#contactTel").val();
    		var address = $("#address").val();
    		var postcode = $("#postcode").val();
    		if(departName==""){
    			messageController.alert("部门名称不允许为空！");
    			return false;
    		}
    		 var selectProvinceCode = $("#provinceSelect").val();
             var selctCityCode = $("#cityCode").val();
             var selectAraCode = $("#areaCode").val();
             if(selectProvinceCode==""){
             	messageController.alert("归属省份不能为空！");
             	return false;
             }
             if(selctCityCode=="0" || selctCityCode==null){
             	messageController.alert("归属地市不能为空！");
             	return false;
             }
             if(selectAraCode=="0" || selectAraCode==null){
             	messageController.alert("归属区域不能为空！");
             	return false;
             }
             $("#provinceCode").val(selectProvinceCode);
    		
    		 var mobileReg = /^0?1[3|4|5|8][0-9]\d{8}$/; 
    		 var telReg=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
    		
    		if(contactTel!=""){
    			if (mobileReg.test(contactTel)==false && telReg.test(contactTel)==false){
    				messageController.alert("联系电话格式错误，请重新输入！");
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
 		
    		var url = _base+"/maintain/depart/addDepart";
    		var param;
    		param={
    				departName: $("#departName").val(),
    				departKindType: $("#departKindType").val(),
    				parentDepartId:  $("#parentDepartId").val(),
    				provinceCode:  $("#provinceCode").val(),
    				cityCode:  $("#cityCode").val(),
    				areaCode:  $("#areaCode").val(),
    	    		contactTel : $("#contactTel").val(),
    	    		contactName:$("#contactName").val(),
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
    		        	window.location.href=_base+"/maintain/depart/main";
    		        },
    	             error:function(XMLHttpRequest, textStatus, errorThrown) {
    					bootbox.alert("编辑失败!");
    				}
    		    }); 
    	}
    	