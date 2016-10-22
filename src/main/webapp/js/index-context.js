
$(document).ready(function(){
	var speed=30;
	var MyMar=setInterval(Marquee,speed);
	
	var url = webpath+"/notice/getNoticeList";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        message: "正在加载数据..",
        success: function (data) {
           data = data.data;
           var divObj = $("#noticeContent");
           var html = "";
           if(data!=null){
        	   var size = data.length>5?5:data.length;
        	   $('#noticeTable').attr("width",(size*600)>900?(size*600):900+"px;");
        	   for(var i in data){
        		   if(i<5){
        			   html += "<li style='min-width:500px;cursor:pointer;' onclick='showNotice(\""+data[i].bulletinId+"\")'><span>["+(new Date(parseFloat(data[i].activeTime))).format("yyyy-MM-dd")+
    			   				"]</span>  "+data[i].bulletinTitle+"</li>";
        		   }
        	   }
           }
           divObj.html(html);
           document.getElementById('report2').innerHTML=document.getElementById('report1').innerHTML;
	       document.getElementById('report').onmouseover=function() {clearInterval(MyMar)};
	       document.getElementById('report').onmouseout=function() {MyMar=setInterval(Marquee,speed)};
        }
    });
});

function Marquee(){
	if(document.getElementById('report2').offsetWidth-document.getElementById('report').scrollLeft<=0)
	   document.getElementById('report').scrollLeft-=document.getElementById('report1').offsetWidth
	else{
	   document.getElementById('report').scrollLeft++
	}
	}

function toNoriceQuery(){
	window.location.href = webpath+"/notice/toNoticeQueryIndex?queryType=1";
}