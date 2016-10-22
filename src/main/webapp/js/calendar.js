var SY,SM,SD,cy,cm
SY = new Date().getFullYear();
SM = new Date().getMonth()+1;
SD = new Date().getDate();

window.onload = function(){
	
	cy = document.getElementById("cal_cy")
	cm = document.getElementById("cal_cm")
//	cy.innerHTML = SY
//	cy.innerHTML = SM
	document.getElementById("YearAll").innerHTML = YearAll(SY)
	document.getElementById("DateAll").innerHTML = DateAll(SY,SM)
	$("#dateYear").val(SY);
	$("#dateMoth").val(SM);
	$("#dateDay").val(SD);
	now();
}
function YearAll(Y){
	var Ystr = ""
	for (var y = 2015; y <= Y + 10; y++) {
		Ystr += "<li onclick='getym(this,\"cal_cy\")'>"+ y +"</li>"
	}
	return Ystr
}
function DateAll(Y,M){
	var Mstr = "",Mnum = GetDaysInMonth(Y,M)
	for (var m = 1; m <= 12; m++) {
		Mstr += "<li onclick='getym(this,\"cal_cm\")'>"+ (m < 10 ? "0" + m : m) +"</li>"
	}
	return Mstr
}
function getym(o,s){
	document.getElementById(s).innerHTML = parseInt(o.innerHTML)
	getDynamicTable(parseInt(cy.innerHTML),parseInt(cm.innerHTML))
}
function Month(s){
	var y = parseInt(cy.innerHTML),m = parseInt(cm.innerHTML)
	if (s == "l") {
		if (m <= 1) {
			m = 12
			y --
		} else {
			m --
		}
	} else {
		if (m >= 12) {
			m = 1
			y ++
		} else {
			m ++
		}
	}
	cy.innerHTML = y
	cm.innerHTML = m
	getDynamicTable(y,m)
}
function now(){
	$("#dateYear").val(SY);
	$("#dateMoth").val(SM);
	$("#dateDay").val(SD);
	getDynamicTable(SY,SM)
	cy.innerHTML = SY
	cm.innerHTML = SM
	queryOaDiary('1');
}
function getDynamicTable(Y,M, selectedDay){
	var Temp,i,j,preDateStr,subDateStr
	var FirstDate,MonthDate,CirNum,ErtNum,StartDate,EndDate  // '当月第一天为星期几,当月的总天数,表格的单元格数及循环数,表格第一排空格数与当月天数之和
	var pStartDay = -1;  
	var choosedDay = -1;
	FirstDate = GetWeekdayMonthStartsOn(Y,M)// '得到该月的第一天是星期几  0-6
	
	if(FirstDate==0){
		StartDate = getFormatDate(Y+"-"+M+"-1");
	}else{
		var pY = M==1?(Y-1):Y;
		var pM = M==1?12:(M-1);
		var prMonthDate = GetDaysInMonth(pY,pM);
		var pStartDay = prMonthDate-FirstDate+1;
		preDateStr = pY+"-"+pM+"-";
		StartDate = getFormatDate(pY+"-"+pM+"-"+pStartDay);
	}
	MonthDate = GetDaysInMonth(Y,M)// '得到该月的总天数 30
	ErtNum = FirstDate + MonthDate// -1 
	Temp = ""
	TDstyle = " "   
	if (ErtNum > 35){
   		CirNum = 42
	}else if (ErtNum == 28){
   		CirNum = 28 
	}else{
   		CirNum = 35
	}
	if(ErtNum==CirNum){
		EndDate = getFormatDate(Y+"-"+M+"-"+MonthDate);
	}else{
		var pY = M==12?(Y+1):Y;
		var pM = M==12?1:(M+1);
		subDateStr = pY+"-"+pM+"-";
		EndDate = getFormatDate(pY+"-"+pM+"-"+(CirNum-ErtNum));
	}
	
	 ajaxController.ajax({
	        method: "POST",
	        url: webpath+"/schedule/queryScheduleDate",
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "startDate": StartDate,
	            "endDate": EndDate
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	        	
	        	var data = data.data;
	        		
            	$("#dateYear").val(SY);
            	$("#dateMoth").val(SM);
            	
            	j=1
            	var subDay = 1;
            	//alert(Y+","+M)
            	//alert("第一天:"+FirstDate+"; 总天数:"+MonthDate+"; ErtNum:"+ErtNum+"; 表格行数:"+(CirNum/7))
            	for (i = 1; i <= CirNum; i++){
            		if (i == 1){
            			Temp += "<table><tr><th>SUN</th><th>MON</th><th>TUE</th><th>WED</th><th>THU</th><th>FRI</th><th>SAT</th></tr><tr>"
            		}
                	if (i < FirstDate + 1){
                		var mark = "";
            			if(data!=null){
                			mark = data.indexOf(preDateStr+pStartDay)>=0?"<i></i>":"";
            			}
                   		Temp += "<td style='cursor:pointer' onclick='setQueryDateDay("+Y+","+M+","+pStartDay+",-1)'>"+mark+"<font style='color: #bfbfbf;'>"+pStartDay+"</font>"+"</td>";
                   		pStartDay++;
            		}else if(i > ErtNum){
            			var mark = "";
            			if(data!=null){
                			mark = data.indexOf(subDateStr+"0"+subDay)>=0?"<i></i>":"";
            			}
            			Temp += "<td style='cursor:pointer' onclick='setQueryDateDay("+Y+","+M+","+subDay+",1)'>"+mark+"<font style='color: #bfbfbf;'>"+subDay+"</font>"+"</td>";
            			subDay++;
            		}else{
            			var mark = "";
            			if(data!=null){
            				var markDay = (j+"").length==1? "0"+j:j+"";
            				var preM = (M+"").length==1? "0"+M:M+"";
                			mark = data.indexOf(Y+"-"+preM+"-"+markDay)>=0?"<i></i>":"";
            			}
            			if(selectedDay!=null){
            				Temp += (selectedDay == j ? "<td onclick='setDateDay(this)' style='cursor:pointer' value='"+j+"' class='now'>" : "<td style='cursor:pointer' value='"+j+"' onclick='setDateDay(this)' >") + mark + j +"</td>";
            				choosedDay = selectedDay;
            			}else{
            				Temp += (SY == Y && SM == M && SD == j ? "<td onclick='setDateDay(this)' style='cursor:pointer' value='"+j+"' class='now'>" : "<td style='cursor:pointer' value='"+j+"' onclick='setDateDay(this)' >") + mark + j +"</td>";
            				if(SY == Y && SM == M && SD == j){
            					choosedDay = j;
                			}
            			}
            			
            			
            	   		j = j + 1
            		}
            		if (i % 7 == 0 && i < ErtNum){
            			Temp += "</tr><tr>"
            		}
            		if (i == CirNum){
            			Temp += "</tr></table>"
            		}
            	}
            	document.getElementById("DayAll").innerHTML = Temp
            	
            	$("#dateYear").val(Y+"");
            	$("#dateMoth").val(M+"");
            	if(choosedDay!=-1){
            		$("#dateDay").val(choosedDay+"");
            	}else{
            		$("#dateDay").val("-1");
            	}
            	querySchedule();
	        }
	    });
}
function GetDaysInMonth(Y,M){				//'得到该月的总天数
	if (M==1||M==3||M==5||M==7||M==8||M==10||M==12)
		return 31;
	else if (M==4||M==6||M==9||M==11)
		return 30;
	else if (M==2)
		if((Y%4==0 && Y%100!=0)||(Y%100==0 && Y%400==0))
			return 29;
		else
			return 28;
	else
		return 28;
}
function GetWeekdayMonthStartsOn(Y,M){		//'得到该月的第一天是星期几
	var date = new Date(Y,M-1,1)
	return date.getDay()
}

function setDateDay(obj){
	$("#DayAll").find(".now").removeAttr("class");
	$(obj).attr("class","now");
	$("#dateDay").val($(obj).attr("value"));
	querySchedule();
	queryOaDiary($("#orDiaryType").val());
}

function setQueryDateDay(Y, M, day, flag){
	var pY,pM
	if(-1 == flag){
		pY = M==1?(Y-1):Y;
		pM = M==1?12:(M-1);
	}else{
		pY = M==12?(Y+1):Y;
		pM = M==12?1:(M+1);
	}
	$("#dateYear").val(pY);
	$("#dateMoth").val(pM);
	$("#dateDay").val(day);
	cy.innerHTML = pY;
	cm.innerHTML = pM;
	getDynamicTable(pY,pM,day);
	querySchedule();
	queryOaDiary("1");
}

function saveSchedule(){
	if($('#schContent').val()=="" || $('#schContent').val()==null){
		bootbox.alert("请填写日程内容!");
		return false;
	}else if($('#schContent').val().length>100){
		bootbox.alert("日程字数限制在100字以内!");
		return false;
	}
	if($("#dateDay").val()=='-1'){
		bootbox.alert("请选择日期!");
		return false;
	}
	var	scheduleDate = $("#dateYear").val()+"-"+$("#dateMoth").val()+"-"+$("#dateDay").val();
	if(new Date()>parseDate(scheduleDate)){
		bootbox.alert("今天之前的日程不能增加!");
		return false;
	}
	scheduleDate = getFormatDate(scheduleDate);
	var content = $('#schContent').val();
	var url = webpath+"/schedule/saveSchedule";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        showWait: true,
	        async: false,
	        data: {
	            "scheduleDate": scheduleDate,
	            "content": content
	        },
	        message: "正在加载数据..",
	        success: function (data) {
	            if(data.success){
	            	bootbox.alert(data.msg);
	            	$('#schContent').val("");
	            	querySchedule();
	            }else{
	            	bootbox.alert(data.msg);
	            }
	        }
	    });
}

function querySchedule(){
	var	scheduleDate = $("#dateYear").val()+"-"+$("#dateMoth").val()+"-"+$("#dateDay").val();
	scheduleDate = getFormatDate(scheduleDate);
	var url = webpath+"/schedule/queryScheduleByDate";
	    ajaxController.ajax({
	        method: "POST",
	        url: url,
	        dataType: "json",
	        data: {
	            "scheduleDate": scheduleDate
	        },
	        success: function (data) {
	            var data = data.data;
	            if(data!=null){
	            	var resultTemplate = $.templates("#scheduleDataImpl");
	            	var resultHtmlOutput = resultTemplate.render(data);
	                $("#scheduleShow").html(resultHtmlOutput);
	            }
	        }
	    });
}

function checkSchedule(scheduleId){
	var url = webpath+"/schedule/modifySchedule";
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "scheduleId": scheduleId,
            "status": 0
        },
        message: "修改数据..",
        success: function (data) {
            if(data.success){
            	bootbox.alert(data.msg);
            	querySchedule();
            }else{
            	bootbox.alert(data.msg);
            }
        }
    });
}

function queryOaDiary(diaryType){
	var url = webpath+"/oaDiary/queryOaDiary";
	var	diaryDate = $("#dateYear").val()+"-"+$("#dateMoth").val()+"-"+$("#dateDay").val();
	diaryDate = getFormatDate(diaryDate);
    ajaxController.ajax({
        method: "POST",
        url: url,
        dataType: "json",
        showWait: true,
        async: false,
        data: {
            "diaryDate": diaryDate,
            "diaryType":diaryType
        },
        message: "查询数据..",
        success: function (data) {
            var data=data.data;
            if(data!=null){
            	if("1"==diaryType){
            		$("#dayPlanForm").find("[name='diaryId']").val(data.diaryId);
            		$("#dayPlanForm").find("[name='workSummary']").val(data.workSummary);
            		$("#dayPlanForm").find("[name='workPlan']").val(data.workPlan);
            		$("#dayPlanForm").find("[name='workExperience']").val(data.workExperience);
            	}else if("2"==diaryType){
            		$("#weekPlanForm").find("[name='diaryId']").val(data.diaryId);
            		$("#weekPlanForm").find("[name='workSummary']").val(data.workSummary);
            		$("#weekPlanForm").find("[name='workPlan']").val(data.workPlan);
            		$("#weekPlanForm").find("[name='workExperience']").val(data.workExperience);
            	}else if("3"==diaryType){
            		$("#mouthPlanForm").find("[name='diaryId']").val(data.diaryId);
            		$("#mouthPlanForm").find("[name='workSummary']").val(data.workSummary);
            		$("#mouthPlanForm").find("[name='workPlan']").val(data.workPlan);
            		$("#mouthPlanForm").find("[name='workExperience']").val(data.workExperience);
            	}
            }else{
            	resetForm($("#dayPlanForm"));
            	resetForm($("#weekPlanForm"));
            	resetForm($("#mouthPlanForm"));
            }
        }
    });
}

//重置已保存的产品信息
function resetForm(obj){
	$(':input', $(obj)).each(function() {
		var type = this.type;
	    var tag = this.tagName.toLowerCase();
	    if(this.name != 'diaryType'){
	    	if (type == 'text' || type == 'password' || tag == 'textarea' ||type == 'hidden')
	  	      this.value = "";
	  	    else if (type == 'checkbox' || type == 'radio')
	  	      this.checked = false;
	  	    else if (tag == 'select')
	  	      this.selectedIndex = -1;
	    }
	 });
}

function sumbitForm(diaryType){
	var form,msg;
	switch(diaryType){
	    case 1:
	    form = $('#dayPlanForm');
	    msg = "日计划";
		break;
		case 2:
		form = $('#weekPlanForm');
	    msg = "周计划";
		break;
		case 3:
		form = $('#mouthPlanForm');
	    msg = "月计划";
		break;
	}
	
	var	diaryDate = $("#dateYear").val()+"-"+$("#dateMoth").val()+"-"+$("#dateDay").val();
	diaryDate = getFormatDate(diaryDate);
	$(form).find("[name='creatDate']").val(diaryDate);
	
	var url = webpath+"/oaDiary/saveOaDiary";
	if($(form).find("[name='diaryId']").val()!=null && $(form).find("[name='diaryId']").val().length>0){
		url = webpath+"/oaDiary/modifyOaDiary";
	}else{
		var d = new Date(); 
		d = d.format('yyyy-MM-dd');
		if(diaryDate!=d){
			bootbox.alert("只能新增当天的日志");
			return false;
		}
	}
	if(isEmpty($(form).find("[name='workSummary']").val()) && isEmpty($(form).find("[name='workPlan']").val()) && isEmpty($(form).find("[name='workExperience']").val())){
		bootbox.alert("未填写任何信息");
		return false;
	}
	if($(form).find("[name='workSummary']").val().length>200){
		bootbox.alert("工作总结不能超过200字");
		return false;
	}
	if($(form).find("[name='workPlan']").val().length>200){
		bootbox.alert("工作计划不能超过200字");
		return false;
	}
	if($(form).find("[name='workExperience']").val().length>200){
		bootbox.alert("心得体会不能超过200字");
		return false;
	}
	
	$.ajax({
        method: "POST",
        url: url,
        dataType: "",
        showWait: true,
        async: false,
        data: $(form).serialize(),
        message: "操作数据..",
        success: function (data) {
        	if(data.success){
            	bootbox.alert(msg+data.msg);
            	queryOaDiary(diaryType);
            }else{
            	bootbox.alert(msg+data.msg);
            }
        }
    });
}

/**
 * @param dateStr
 * @returns
 */
function getFormatDate(dateStr){
	var a=dateStr.split('-');
	for(var i=0;i<a.length;i++) {
		if(a[i].length==1) 
			a[i]='0'+a[i];
	}
	return a.join('-');
}

function isEmpty(Str){
	if(Str==null || Str.length==0){
		return true;
	}
	return false;
}

function parseDate(dateStr) {
	dateStr += " 23:59:59";
	dateStr = dateStr.replace(/-/g,"/");
    return new Date(dateStr);
}