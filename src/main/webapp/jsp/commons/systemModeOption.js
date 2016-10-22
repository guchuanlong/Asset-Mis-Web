function getSystemModeOption(itemName, value, defmsg){
	$.ajax({
		url : _bathPath+"/commonManage/querySystemModeList",
		type : "post",
		async : true,
		dataType : "html",
		timeout : "10000",
		error : function() {
			alert("服务加载出错");
		},
		success : function(data) {
			var json = eval(data);
			var selObj = $("#" + itemName);
			selObj.empty();
			if(defmsg != null){
				selObj.append("<option value=''>"+defmsg+"</option>");
			}else{
				selObj.append("<option value=''></option>");
			}
			$.each(json, function(index, item) {
				//循环获取数据    
				var systemModeId = json[index].systemModeId;
				var systemModeName = json[index].systemModeName;
				if(value != null && value == systemModeId){
					selObj.append("<option value='"+systemModeId+"' selected>" + systemModeName
							+ "</option>");
				}else{
					selObj.append("<option value='"+systemModeId+"'>" + systemModeName
							+ "</option>");
				}
			});
		}
	});
}
