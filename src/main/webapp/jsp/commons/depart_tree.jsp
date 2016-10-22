<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<html>
		<head>
		    <%@ include file="/jsp/commons/common.jsp" %>
		    <script src="${_bathPath}/resources/ztree/js/jquery.ztree.all-3.5.min.js"></script>
		    <link rel="stylesheet" href="${_bathPath}/resources/ztree/css/zTreeStyle/zTreeStyle.css"/>
		    <h5>部门信息</h5>
		</head>
		<body>
			<ul id="treeDemo" class="ztree"></ul>
			<input type="hidden" id="selectDepartId" value=""/>
			<input type="hidden" id="selectDepartName" value=""/>
			<input type="hidden" id="selectDepartProvinceCode" value=""/>
			<script>
			    var setting = {
			        async: {
			            enable: true,
			            url: "${_bathPath}/commonManage/getDepartList",
			            autoParam: ["departId"],
			            otherParam: {"otherParam": "zTreeAsyncTest"},
			            dataFilter: filter
			        },
			        callback: {
			            onClick: function (event, treeId, treeNode) {
			                $("#selectDepartName").val(treeNode.departName);
			                $("#selectDepartId").val(treeNode.departId)
			                $("#selectDepartProvinceCode").val(treeNode.provinceCode)
			            }
			        },
			        view: {
			            showIcon: false
			        }
			    };
			
			    function filter(treeId, parentNode, childNodes) {
			        if (!childNodes) return null
			        for (var i = 0, l = childNodes.length; i < l; i++) {
			            childNodes[i].name = childNodes[i].departName;
			            childNodes[i].isParent = true;
			        }
			
			        return childNodes;
			    }
			
			    $(document).ready(function () {
			        $.fn.zTree.init($("#treeDemo"), setting);
			    });
			
			</script>
		</body>
	</html>
