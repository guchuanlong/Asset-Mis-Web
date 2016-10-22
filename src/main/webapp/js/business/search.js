$(function(){
    //查询按钮
    var isSearchFlag = true;
    $("#searchOrderData").click(function () {
        var searchCondition = getSearchConditions();
        if(isSearchFlag){
            isSearchFlag = false;
            var orderId = jQuery.trim($("#order_id").val());
           
            var jsondata = getOrderData(1);
            if(jsondata != null && jsondata != 'undefined' && jsondata.result!=null && jsondata.result.length>0){
                //隐藏无数据提示
                $(".tishia").hide();
                $("#tishia").hide();

                var template = $.templates("#orderDataTmpl");
                var htmlOutput = template.render(jsondata.result);
                $("#orderData").html(htmlOutput);
                var pageData = $('#pagination-demo').data();

                var pager = jsondata.pager;
                $('#pagination-demo').empty();
                $('#pagination-demo').removeData("twbs-pagination");
                $('#pagination-demo').unbind("page");
                $('#pagination-demo').twbsPagination({
                    totalPages: pager.totalPages,
                    visiblePages: 5,
                    onPageClick: function (event, page) {
                        var currentSearchCondition = getSearchConditions();
                        if(searchCondition!=currentSearchCondition){
                            messageController.alert("查询条件已变化，请点击'查询'按钮重新查询");
                            return false;
                        }else{
                            var jsondata = getOrderData(page);
                            var pager = jsondata.pager;
                            var template = $.templates("#orderDataTmpl");
                            var htmlOutput = template.render(jsondata.result);
                            $("#orderData").html(htmlOutput);

                            $(".tabrow").hover(function() {
                                $(this).addClass('current-color');
                            }, function() {
                                $(this).removeClass('current-color');
                            });
                        }
                    }
                });

                $(".tabrow").hover(function() {
                    $(this).addClass('current-color');
                }, function() {
                    $(this).removeClass('current-color');
                });
            }else{
                $("#orderData").html(null);
                $('#pagination-demo').empty();
                $(".tishia").hide();
                $("#tishia").show();//显示无数据提示
            }
            isSearchFlag = true;
        }
    });
})

//获取查询条件
function getSearchConditions(){
    var conditions = $("input,#order_busiCode,#orderTaskState");
    var cons = "";
    for(var i=0;i<conditions.length;i++){
        cons += conditions[i].value;
    }
    return cons;
}