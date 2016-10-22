<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<div class="col-md-3">
    <div class="list-group">
        <h3 id="menuHead">
            <a href="javascript:void(0);" class="list-group-item active">
                主数据管理
            </a>
        </h3>
        <script id="menuDataTmpl" type="text/x-jsrender">
             <a href="${_base}/{{:menuUrl}}" class="list-group-item">{{:menuName}}</a>
        </script>
    </div>
</div>
<script>
    $(document).ready(function () {
        var url = "${_base}/menu/all"
        ajaxController.ajax({
            method: "POST",
            url: url,
            dataType: "json",
            showWait: true,
            message: "正在加载数据..",
            success: function (data) {
                var jsonData = data.data;//转换为json对象;
                var template = $.templates("#menuDataTmpl");
                var htmlOutput = template.render(jsonData);
                $("#menuHead").after(htmlOutput);
            }
        }); //end of ajax
    });
</script>