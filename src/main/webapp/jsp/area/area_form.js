		$(document).ready(function () {
			//当页面为修改时，要手动触发updateCity()
			 var selectProvinceCode=$("#provinceCode").val();
			 if(selectProvinceCode!=undefined&&selectProvinceCode!=''){
				 updateCity();				 
			 }
			 
			 
            //$("#provinceSelect option:first").after("<option value='00'>全国</option>");
			 $("#btnSaveArea").click(function () {                
				 saveArea();
			 });
            $("#add_back").click(function () {                
                window.location.href = _base+"/maintain/area/main";
            });
        })
        
        function saveArea(){
			var url;
			var selectProvinceCode=$("#provinceCode").val();
            var selectCityCode=$("#cityCode").val();
            var selectCountyCode=$("#countyCode").val();
            var selectStreetCode=$("#streetCode").val();
            var areaName=$("#areaName").val();
            
            if(selectProvinceCode=="" || selectProvinceCode==null){
            	messageController.alert("归属省份不能为空！");
            	return false;
            }
            if(selectCityCode=="" || selectCityCode==null){
            	messageController.alert("归属地市不能为空！");
            	return false;
            }
            if(selectCountyCode=="" || selectCountyCode==null){
            	messageController.alert("归属区县不能为空！");
            	return false;
            }
            if(selectStreetCode=="" || selectStreetCode==null){
            	messageController.alert("归属街道不能为空！");
            	return false;
            }
            
            var param={
            		provinceCode:selectProvinceCode,
            		cityCode:selectCityCode,
            		countyCode:selectCountyCode,
            		streetCode:selectStreetCode,
            		areaName:areaName
            };
            //
			var areaCode=$("#areaCode").val();
			if(areaCode!=undefined&&areaCode!=""){
				url=_base+"/maintain/area/editArea";
				param.areaCode=areaCode;
			}else{
				 url = _base+"/maintain/area/addArea";
			}
            
			ajaxController.ajax({
		        method: "POST",
		        url: url,
		        dataType: "json",
		        showWait: true,
		        async: false,
		        data: param,
		        message: "正在加载数据..",
		        success: function (data) {
		        	//bootbox.alert(data.statusInfo);
		        	window.location.href=_base+"/maintain/area/main";;
		        },
	             error:function(XMLHttpRequest, textStatus, errorThrown) {
					bootbox.alert("编辑失败!");
				}
		    }); 
			
			
			
		}
        
        function updateCity() {
            var provinceCode = $("#provinceCode").val();
            if (provinceCode == ""){
                $("#cityCode").empty();
                $("#cityCode").prop('disabled', true);
                return;
            }
            $("#cityCode").prop('disabled', false);
            var url = _base+"/maintain/area/listcity"
            ajaxController.ajax({
                method: "POST",
                url: url,
                dataType: "json",
                async: false,
                data: {
                    "parentAreaCode": function () {
                        return $("#provinceCode").val();
                    }
                },
                message: "正在加载数据..",
                success: function (data) {
                    var template = $.templates("#cityCodeTmpl");
                    var htmlOutput = template.render(data.data);
                    $("#cityCode").empty();
                    $("#cityCode").append("<option value=''>--请选择--</option>");
                    $("#cityCode").append(htmlOutput);
                    
                   //处理修改时的回显
                    var defaultValue=$("#cityCode").attr("defaultValue");
                    if(defaultValue!=undefined&&defaultValue!=''){
                    	$("#cityCode").val(defaultValue);
                    	updateCounty();                    	
                    }
                    
                    
                }
            }); //end of ajax
        }
		
		function updateCounty() {
            var cityCode = $("#cityCode").val();
            if (cityCode == ""){
                $("#countyCode").empty();
                $("#countyCode").prop('disabled', true);
                return;
            }
            $("#countyCode").prop('disabled', false);
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
                    var template = $.templates("#countyCodeTmpl");
                    var htmlOutput = template.render(data.data);
                    $("#countyCode").empty();
                    $("#countyCode").append("<option value=''>--请选择--</option>");
                    $("#countyCode").append(htmlOutput);
                    //处理修改时的回显
                    var defaultValue=$("#countyCode").attr("defaultValue");
                    if(defaultValue!=undefined&&defaultValue!=''){
                    	$("#countyCode").val(defaultValue);
                    	updateStreet();                    	
                    }
                    //$("#countyCode option:first").before("<option value=''>--请选择--</option>");
                }
            }); //end of ajax
        }
		function updateStreet() {
            var countyCode = $("#countyCode").val();
            if (countyCode == ""){
                $("#streetCode").empty();
                $("#streetCode").prop('disabled', true);
                return;
            }
            $("#streetCode").prop('disabled', false);
            var url = _base+"/maintain/area/liststreet"
            ajaxController.ajax({
                method: "POST",
                url: url,
                dataType: "json",
                async: false,
                data: {
                    "parentAreaCode": function () {
                        return $("#countyCode").val();
                    }
                },
                message: "正在加载数据..",
                success: function (data) {
                    var template = $.templates("#streetCodeTmpl");
                    var htmlOutput = template.render(data.data);
                    $("#streetCode").empty();
                    $("#streetCode").append("<option value=''>--请选择--</option>");
                    $("#streetCode").append(htmlOutput);
                    
                   //处理修改时的回显
                    var defaultValue=$("#streetCode").attr("defaultValue");
                    if(defaultValue!=undefined&&defaultValue!=''){
                    	$("#streetCode").val(defaultValue);
                    }
                }
            }); //end of ajax
        }
        