function getSystemOption(itemName, value, defmsg){
	$.ajax({
		url : _bathPath+"/commonManage/querySystemList",
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
				var systemId = json[index].systemId;
				var systemName = json[index].systemName;
				if(value != null && value == systemId){
					selObj.append("<option value='"+systemId+"' selected>" + systemName
							+ "</option>");
				}else{
					selObj.append("<option value='"+systemId+"'>" + systemName
							+ "</option>");
				}
			});
		}
	});
}
