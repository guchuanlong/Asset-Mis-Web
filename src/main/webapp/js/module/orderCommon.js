/**
 * 商品查询
 */
var searchProduct = function() {
	var project = $('#project option:selected').val();
	if (project == "") {
		bootbox.alert("请选择项目!", function() {
			var country = document.getElementsByName('serproductType');
			for (var i = 0; i < country.length; i++) {
				if (country[i].checked) {
					country[i].checked = false; // 不选中
				}
			}
		});

	} else {
		refreshOrderData();
	}
}
/**
 * 刷新商品
 */
var refreshOrderData = function() {
	var jsondata = getOrderData(1);
	if (jsondata != undefined) {
		if (jsondata.result == null) {
			$('#pagination-demo').empty();
			$('#pagination-demo').removeData("twbs-pagination");
			$('#pagination-demo').unbind("page");
			$("#orderData").html("");
		}
		var pager = jsondata.pager;
		if (pager != null && pager.totalPages == '0') {
			$('#pagination-demo').empty();
			$('#pagination-demo').removeData("twbs-pagination");
			$('#pagination-demo').unbind("page");
			$("#orderData").html("");
		}
		if (pager != null && pager.totalPages != '0') {
			$('#pagination-demo').empty();
			$('#pagination-demo').removeData("twbs-pagination");
			$('#pagination-demo').unbind("page");
			var template = $.templates("#orderDataTmpl");
			var htmlOutput = template.render(jsondata.result);
			$("#orderData").html(htmlOutput);
			$('#pagination-demo').twbsPagination({
				totalPages : pager.totalPages,
				visiblePages : 5,
				onPageClick : function(event, page) {
					var jsondata = getOrderData(page);
					var pager = jsondata.pager;
					var template = $.templates("#orderDataTmpl");
					var htmlOutput = template.render(jsondata.result);
					$("#orderData").html(htmlOutput);
				}
			});
		}
	}
}

/**
 * 获取数据
 */
var getOrderData = function(currentPage) {
	var jsonData;
	var productType = $('input:radio[name="serproductType"]:checked').val();
	if (productType == undefined) {
		productType = "";
	}
	var url = webpath + "/order/queryProductList";
	ajaxController.ajax({
		method : "POST",
		url : url,
		dataType : "json",
		showWait : true,
		async : false,
		data : {
			"currentPage" : currentPage,
			"productName" : function() {
				return jQuery.trim($("#serproductName").val())
			},
			"productType" : productType,
			"productCata" : '1',
			"shareFlag" : $('input:radio[name="combined"]:checked').val(),
			"personalFlag" : $('#custType option:selected').val(),
			"project" : $('#project option:selected').val()
		},
		message : "正在加载数据..",
		success : function(data) {
			jsonData = data.data;// 转换为json对象;
		},
		failed : function() {
			bootbox.alert("请求产品列表出错!");
		}
	});
	return jsonData;
}

/**
 * 图片预览
 */
var previewImage = function(file, asdiv) {
	var MAXWIDTH = 180;
	var MAXHEIGHT = 110;
	var img;
	var path1 = "";
	if (file.value == "") {
		return;
	}
	if (asdiv == 1) {
		img = document.getElementById("frontage-cert-img");
		path1 = $("#image_file1").val();
	}
	if (asdiv == 2) {
		img = document.getElementById("posen-cert-img");
		path1 = $("#image_file2").val();
	}
	if (asdiv == 3) {
		img = document.getElementById("img-yyzj");
		path1 = $("#image_file3").val();
		MAXWIDTH = 250;
		MAXHEIGHT = 160;
	}
	var allowType = {
		".bmp" : 1,
		".png" : 1,
		".jpeg" : 1,
		".jpg" : 1
	};
	var ext = path1.substr(path1.lastIndexOf(".")).toLowerCase();
	var res = allowType[ext];
	if (res != 1) {
		bootbox.alert("身份证照片格式不正确，请选择正确的身份证图片！");
		return;
	}

	var fileSize = 0;
	if (file.files && file.files[0]) {
		fileSize = file.files[0].size;
		var size = fileSize / 1024 * 1024;
		if (size > (1024 * 2048)) {
			bootbox.alert("图片大小不能超过2MB！");
			return;
		}
		img.onload = function() {
			var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth,
					img.offsetHeight);
			img.width = rect.width;
			img.height = rect.height;
			// img.style.marginTop = rect.top + 'px';
		}
		var reader = new FileReader();
		reader.onload = function(evt) {
			img.src = evt.target.result;
		}
		reader.readAsDataURL(file.files[0]);
	} else {
		file.select();
		file.blur();
		var reallocalpath = document.selection.createRange().text;
		// 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
		var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
		var fileImg = fileSystem.GetFile(reallocalpath);
		fileSize = fileImg.Size;
		var size = fileSize / 1024 * 1024;
		if (size > (1024 * 2048)) {
			bootbox.alert("图片大小不能超过2MB！");
			return;
		}
		// 获取欲上传的文件路径
		img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src=\""
				+ reallocalpath + "\")";
		// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
		img.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
	}

}
/**
 * 图片显示
 */
var clacImgZoomParam = function(maxWidth, maxHeight, width, height) {
	var param = {
		top : 0,
		left : 0,
		width : width,
		height : height
	};
	if (width > maxWidth || height > maxHeight) {
		rateWidth = width / maxWidth;
		rateHeight = height / maxHeight;

		if (rateWidth > rateHeight) {
			param.width = maxWidth;
			param.height = Math.round(height / rateWidth);
		} else {
			param.width = Math.round(width / rateHeight);
			param.height = maxHeight;
		}
	}
	param.left = Math.round((maxWidth - param.width) / 2);
	param.top = Math.round((maxHeight - param.height) / 2);
	return param;
}

// 获取渠道类型
var getchlType = function() {
	$.ajax({
		url : webpath + '/param/getchlKinds',
		type : "post",
		async : true,
		dataType : "html",
		timeout : "10000",
		error : function() {
			alert("服务加载出错");
		},
		success : function(data) {
			var json = eval(data);
			var selObj = $("#chnlKindId");
			selObj.empty();
			selObj.append("<option value=''>请选择</option>");
			$.each(json, function(index, item) {
				// 循环获取数据
				var kindId = json[index].kindId;
				var kindValue = json[index].kindValue;
				selObj.append("<option value='" + kindId + "'>" + kindValue
						+ "</option>");

			});
		}
	});
}
/**
 * 获取项目
 */
var getProject = function() {
	$.ajax({
		url : webpath + '/param/getmanagerAddress',
		type : "post",
		async : true,
		dataType : "html",
		timeout : "10000",
		error : function() {
			alert("服务加载出错");
		},
		success : function(data) {
			var json = eval(data);
			var selObj = $("#project");
			selObj.empty();
			selObj.append("<option value=''>请选择</option>");
			$.each(json, function(index, item) {
				// 循环获取数据
				var kindId = json[index].areaCode;
				var kindValue = json[index].areaName;
				selObj.append("<option value='" + kindId + "'>" + kindValue
						+ "</option>");
			});
		}
	});
}

// 获取设备型号
var getProductCategory = function() {
	$
			.ajax({
				url : webpath + '/param/getProductCategory',
				type : "post",
				async : true,
				dataType : "html",
				timeout : "10000",
				error : function() {
					alert("服务加载出错");
				},
				success : function(data) {
					var json = eval(data);
					equipJson = json;
					$
							.each(
									json,
									function(index, item) {
										// 循环获取数据
										var productId = json[index].productId;
										var productName = json[index].productName;
										$("#equip")
												.append(
														" <span class='er_radio'><input name='equipType' id='equipType' type='radio' value='"
																+ productId
																+ "' onclick=setEquipName(this,'"
																+ productId
																+ "')></span><span class='er_wena'>"
																+ productName
																+ "</span>");
									});
				}
			});
}
