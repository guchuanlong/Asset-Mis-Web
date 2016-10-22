//$(document).ready(function(){
//  $(".yax").mouseover(function(){
//  $(".yax ul").show();
//  });
//  $(".yax").mouseout(function(){
//  $(".yax ul").hide();
//  });
//});

$(document).ready(function(){
  $(".user1").mouseover(function(){
  $(".user1 ul").show();
  $(".user1").addClass("usera");
  });
  $(".user1").mouseout(function(){
  $(".user1 ul").hide();
  $(".user1").removeClass("usera");
  });
});


$(document).ready(function(){
  $(".ruin ul li").mouseover(function () {
	  $(this).children('dl').show();
	  });
  $(".ruin ul li").mouseout(function () {
	  $(this).children('dl').hide();
	  });
});


$(document).ready(function(){
  $(".ruina").mouseover(function () {
	  $(".ruina ul").show(0);
	  $(".ruina p").removeClass("reorder");
	  $(".ruina p").addClass("remove");
	  });
   $(".ruina").mouseout(function () {
	  $(".ruina ul").hide(0);
	  $(".ruina p").removeClass("remove");
	  $(".ruina p").addClass("reorder");
	  });
  
});



$(document).ready(function(){
  $(".ruin p").click(function () {
	  $(".ruin ul").slideToggle(100);
	  $(".ruin p").toggleClass("reorder remove");
	  });
});



$(function(){
$(".login_title li").click(function () {
                $(".login_title li").each(function () {
                    $(this).removeClass("current");
                });
                $(this).addClass("current");
            });
$('.login_title li').click(function(){
  var index=$('.login_title li').index(this);
      if(index==0){
      $('.form1').show();
      $('.form2').hide();
   }
   if(index==1){
     $('.form1').hide();
     $('.form2').show();
   }
  }) ;
});

$(function(){
$(".table_lista li").click(function () {
                $(".table_lista li").each(function () {
                    $(this).removeClass("current");
                });
                $(this).addClass("current");
            });
$('.table_lista li').click(function(){
  var index=$('.table_lista li').index(this);
      if(index==0){
      $('.test1').show();
      $('.test2').hide();
   }
   if(index==1){
     $('.test1').hide();
     $('.test2').show();
   }
  }) ;
});



	$(document).ready(function(){
  $(".dage  a").click(function () {
	  $(".dage_none ").slideToggle(100);
	  $(".dage ").toggleClass("reorder remove");
	  });
});


$(document).ready(function(){
$(window).bind('scroll resize',function(){
        var iMaxScroll = document.body.scrollHeight-document.documentElement.clientHeight-150; //距离底部 500px的
        var scrollT = document.documentElement.scrollTop||document.body.scrollTop;   //滚动条
        if(scrollT>=iMaxScroll){ 
            $('.xuanfu').css('top','10px')  // 滚动条滚动到距离底部500px的时候 改变样式
        }
    })
});


        function a(i){
	a(i);
}
   function a(i){
	   switch(i){
            case 1:
       document.getElementById("riji1").style.display="block";
       document.getElementById("riji2").style.display="none";
	   document.getElementById("riji3").style.display="none";
	   $("#orDiaryType").val('1');
	   queryOaDiary("1");
       break;
            case 2:
       document.getElementById("riji1").style.display="none";
       document.getElementById("riji2").style.display="block";
	   document.getElementById("riji3").style.display="none";
	   $("#orDiaryType").val('2');
	   queryOaDiary("2");
      break;
	  case 3:
       document.getElementById("riji1").style.display="none";
       document.getElementById("riji2").style.display="none";
	   document.getElementById("riji3").style.display="block";
	   $("#orDiaryType").val('3');
	   queryOaDiary("3");
      break;
 }
}

 $(function () {
    var st = 100;
    $('.code_l').mouseenter(function () {
		$('.code_l p').show(1);
    })
		$(".code_l p").click(function () {
                $(this).hide(1);
            });
			
		$('.code_l').mouseleave(function () {
        $('.code_l p').hide(1);
    });	
  });

//查询-订单详情
 function viewOrder(basepath,orderId) {
 	var url = basepath+"/order/toViewOrder?orderId=" + orderId;
 	window.open(url,'订单详情','height=600, width=1000, top=20, left=160, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
 }

 function printAutoOrderInfo(basepath,orderId){
 	var url = basepath+"/orderAutoInput/toOrderAutoInputOrder?orderId=" + orderId;
 	window.open(url,'订单详情','height=600, width=1000, top=20, left=160, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');

 }

 Date.prototype.Format = function (fmt) { //author: meizz
     var o = {
         "M+": this.getMonth() + 1, //月份
         "d+": this.getDate(), //日
         "h+": this.getHours(), //小时
         "m+": this.getMinutes(), //分
         "s+": this.getSeconds(), //秒
         "q+": Math.floor((this.getMonth() + 3) / 3), //季度
         "S": this.getMilliseconds() //毫秒
     };
     if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
     for (var k in o)
     if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
     return fmt;
 }
