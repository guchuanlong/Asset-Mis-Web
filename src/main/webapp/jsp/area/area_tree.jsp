<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/jsp/commons/common.jsp" %>
    <script src="${_base}/resources/bower_components/ztree/js/jquery.ztree.all-3.5.min.js"></script>
    <link rel="stylesheet" href="${_base}/resources/bower_components/ztree/css/zTreeStyle/zTreeStyle.css"/>
</head>
<body>
<div style="height:400px; overflow: auto;">
<ul id="treeDemo" class="ztree"></ul>
<input type="hidden" id="selectAreaCode" value=""/>
<input type="hidden" id="selectAreaName" value=""/>
<input type="hidden" id="selectProvinceCode" value=""/>
<input type="hidden" id="selectCityCode" value=""/>
</div>
<script>
    
    var setting = {
        async: {
            enable: true,
            url: "${_base}/maintain/area/listarea",
            autoParam: ["areaCode","areaLevel","provinceCode","cityCode"],
            otherParam: {"otherParam": "zTreeAsyncTest"},
            dataFilter: filter
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                $("#selectAreaName").val(treeNode.areaName);
                $("#selectAreaCode").val(treeNode.areaCode)
                $("#selectProvinceCode").val(treeNode.provinceCode)
                $("#selectCityCode").val(treeNode.cityCode)
            }
        },
        view: {
            showIcon: true
        }
    };

    function filter(treeId, parentNode, childNodes) {
        if (!childNodes) return null
        for (var i = 0, l = childNodes.length; i < l; i++) {
            childNodes[i].name = childNodes[i].areaName;
            if(childNodes[i].areaLevel==5){
                childNodes[i].isParent = false;            	
            }
            else{
            	childNodes[i].isParent = true;
            }
        }
        return childNodes;
    }

    $(document).ready(function () {
        $.fn.zTree.init($("#treeDemo"), setting);
    });
  

</script>
</body>
</html>
