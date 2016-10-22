var basePath = "";
function setBasePath(path){
	basePath = path;
}
//1.查询订单信息
function getOrderInfo(orderId) {
    var jsonData;
    var url = basePath+"/orderAudit/getOrderInfo";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "orderId": orderId
        },
        message: "正在加载数据..",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}

//2.1获取客户资料
function getOrderCustInfo(orderId) {
    var jsonData;
    var url = basePath+"/orderAudit/getOrderCustInfo";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "orderId": orderId
        },
        message: "正在加载数据..",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}

//2.2获取业务受理信息、受理附加信息
function getOrderBisInfo(orderId) {
    var jsonData;
    var url = basePath+"/orderCommit/getPrintDetail";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "orderId": orderId
        },
        message: "正在加载数据..",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}
//2.3费用信息
function getOrderFeeInfo(orderId) {
    var jsonData;
    var url = basePath+"/orderAudit/getOrderFeeInfo";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "orderId": orderId
        },
        message: "正在加载数据..",
        success: function (data) {
            jsonData = data.data;
        }
    });
    return jsonData;
}

function loadOrderInfo(orderId) {
    if(orderId != null){
		//1.获取订单状态信息
	    var ord_info_jsondata = getOrderInfo(orderId);
	    if(ord_info_jsondata != null && ord_info_jsondata != 'undefined'){
			var ord_info_template = $.templates("#orderStateTmpl");
			var ord_info_outputHtml = ord_info_template.render(ord_info_jsondata);
			$("#chlId").val(ord_info_jsondata.chlId);
			$("#orderStateInfo").html(ord_info_outputHtml);
			
			if(ord_info_jsondata.state != '待派单'){
				$("#sendOrder").attr({'disabled':'disabled','style':'background:gray'});
			}
			
			if(ord_info_jsondata.state!='待施工'){
				$("#constructOrder").attr({'disabled':'disabled','style':'background:gray'});
			}
			
			if(ord_info_jsondata.state!='待服务开通'){
				$("#openOrder").attr({'disabled':'disabled','style':'background:gray'});
			}
			
			$("#busiCode").val(ord_info_jsondata.busiCode);
			if(ord_info_jsondata.busiCode!='kdtw-yysl'){
				$("#restime_info").show();
			}else{
				$("#resTime").val("");
			}
	    }
	  //2.1获取客户资料
	    var ord_cust_jsondata = getOrderCustInfo(orderId);
	    if(ord_cust_jsondata != null && ord_cust_jsondata != 'undefined'){
			var template = $.templates("#orderCustInfoTmpl");
			var outputHtml = template.render(ord_cust_jsondata);
			$("#orderCustInfo").html(outputHtml);
	    }else{
	    	$("#orderCustInfo").html("<div class='box_list xian'>没有相关信息</div>");
	    }
		//2.2获取业务受理信息、受理附加信息
	    var jsonData = getOrderBisInfo(orderId);
		if(jsonData != null && jsonData != 'undefined'){
			/*if(jsonData.buisCode != null && jsonData.buisCode != ''){
				if('kdkt-yysl' != jsonData.buisCode && 'cskt-yysl'!= jsonData.buisCode){
					$("#orderBisInfoTitle").show();
					if(jsonData.bisInfo != null && jsonData.bisInfo != ''){
						var template = $.templates("#orderBisInfoTmpl");
						var outputHtml = template.render(jsonData);
						$("#orderBisInfo").html(outputHtml);
					}else{
						$("#orderBisInfo").html('<div class="box_list xian">没有相关信息</div>');
					}
				}else{
					
				}
			}*/
			$("#orderBisExtInfoTitle").show();
			$("#orderBisInfoTitle").show();
			$("#orderBisAcctInfoTitle").show();
			if(jsonData.bisExtrInfo != null && jsonData.bisExtrInfo != ''){
				var template = $.templates("#orderBisExtInfoTmpl");
				var outputHtml = template.render(jsonData);
				$("#orderBisExtInfo").html(outputHtml);
			}else{
				$("#orderBisExtInfo").html('<div class="box_list xian">没有相关信息</div>');
			}
			if(jsonData.bisInfo != null && jsonData.bisInfo != ''){
				var template = $.templates("#orderBisInfoTmpl");
				var outputHtml = template.render(jsonData);
				$("#orderBisInfo").html(outputHtml);
			}else{
				$("#orderBisInfo").html('<div class="box_list xian">没有相关信息</div>');
			}
			if('fwsl-yysl' == jsonData.buisCode || 'fwdg-yysl' == jsonData.buisCode || 'fwbg-yysl' == jsonData.buisCode || 'fwtd-yysl' == jsonData.buisCode){
				$("#orderBisAcctInfoTitle").css("display","none");
				$("#orderBisAcctInfo").css("display","none");
			}else{
				if(jsonData.bisAcctNo != null && jsonData.bisAcctNo != ''){
					var template = $.templates("#orderBisAcctTmpl");
					var outputHtml = template.render(jsonData);
					$("#orderBisAcctInfo").html(outputHtml);
				}else{
					$("#orderBisAcctInfo").html('<div class="box_list xian">没有相关信息</div>');
				}
			}
		}else{
			$("#orderBisExtInfoTitle").show();
			$("#orderBisInfoTitle").show();
			$("#orderBisExtInfo").html('<div class="box_list xian">没有相关信息</div>');
			$("#orderBisInfo").html('<div class="box_list xian">没有相关信息</div>');
		}
		
		//2.3获取费用信息
	    var ord_fee_jsondata = getOrderFeeInfo(orderId);
	    if(ord_fee_jsondata != null && ord_fee_jsondata != 'undefined'){
	    	if(ord_fee_jsondata.adjustFee <= 0) {
	    		$("#printReceiptBtn").attr({'style':'display:none'});
	    	}
			var template = $.templates("#orderFeeInfoTmpl");
			var outputHtml = template.render(ord_fee_jsondata);
			$("#orderFeeInfo").html(outputHtml);
	    }else{
	    	$("#orderFeeInfo").html("<div class='box_list'>没有相关信息</div>");
	    	$("#printReceiptBtn").attr({'style':'display:none'});
	    }
    }
}