var orderid = "";
var tourl = "";
var base_path = "";

function orderCancel(orderId,urll,base){
	$(".cancel").css('display','block');
	orderid = orderId;
	tourl = urll;
	base_path = base;
}

function close_qx(){
	$(".cancel").css('display','none');
	$("textarea[name='reason']").val('');
}

function confirm_qx(){
	var reason = $("textarea[name='reason']").val();
	if($.trim(reason)!=""){
		var byteStr = reason.replace(/[^\x00-\xff]/g, '**');
		if($.trim(byteStr).length>=500){
			messageController.alert("取消原因需在500字节以内");
			return;
		}
		$("textarea[name='reason']").val('');
	}
	$(".cancel").css('display','none');
	var url = base_path+"/order/orderCancel";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "orderId": orderid,
            "cancelReason":reason
        },
        message: "正在加载数据..",
        success: function (data) {
			messageController.alert(data.statusInfo,function(){
				window.location.href = tourl;
			});
        }
    });
}