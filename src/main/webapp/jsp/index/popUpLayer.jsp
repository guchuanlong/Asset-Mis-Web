<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ai.runner.bis.web.business.common.model.SelectVo,java.util.List" %>
<script type="text/javascript">
$(function(){
	$(".icon-remove").click(function(event) {
		$("#schedule_tanc_cz").css('display', 'none');
	    });
	$('#scheduleStartDate').datetimepicker({
		language:  'zh-CN-schedule',
	    weekStart: 1,
	    todayBtn:  1,
	    resetBtn:1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0//,
	    //showMeridian: 1
	}).on("changeDate",function(ev){
		$('#endDate').datetimepicker('setStartDate', $('#scheduleDate').val());
	});
	
	$('#diaryStartDate').datetimepicker({
		language:  'zh-CN-schedule',
	    weekStart: 1,
	    todayBtn:  1,
	    resetBtn:1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0//,
	    //showMeridian: 1
	}).on("changeDate",function(ev){
		$('#endDate').datetimepicker('setStartDate', $('#diaryDate').val());
	});
	
	//searchSchedule();
});

function cancel(){
	$("#schedule_tanc_cz").css('display', 'none');
}
function cancelDiary(){
	$("#diary_tanc_cz").css('display', 'none');
}
function cancelNotice(){
	$("#notice_tanc_cz").css('display', 'none');
}
function showSchedule(){
	 $("#schedule_tanc_cz").show();
	 searchSchedule();
}

function showOrDiary(){
	$("#diary_tanc_cz").show();
	searchOrDiary();
}

function searchOrDiary(diaryType){
	
	if(diaryType=="1"){
		$('#diaryDay').attr("class","current");
		$('#diaryWeek').attr("class","");
		$('#diaryMouth').attr("class","");
		$('#diaryAll').attr("class","");
	}else if(diaryType=="2"){
		$('#diaryWeek').attr("class","current");
		$('#diaryDay').attr("class","");
		$('#diaryMouth').attr("class","");
		$('#diaryAll').attr("class","");
	}else if(diaryType=="3"){
		$('#diaryMouth').attr("class","current");
		$('#diaryWeek').attr("class","");
		$('#diaryDay').attr("class","");
		$('#diaryAll').attr("class","");
	}else{
		$('#diaryAll').attr("class","current");
		$('#diaryWeek').attr("class","");
		$('#diaryMouth').attr("class","");
		$('#diaryDay').attr("class","");
	}
	var jsondata = getOrDiaryData(1, diaryType);
	var pager = jsondata.pager;
	var template = $.templates("#diaryDataTmpl");
	var htmlOutput = template.render(jsondata.result);
	$("#oaDiaryList").html(htmlOutput);
	

	$('#pagination-diary').empty();
	$('#pagination-diary').removeData("twbs-pagination");
	$('#pagination-diary').unbind("page");
	var pageData = $('#pagination-diary').data();
	$('#pagination-diary').twbsPagination({
		totalPages : pager.totalPages,
		visiblePages : 5,
		onPageClick : function(event, page) {
			var jsondata = getOrDiaryData(page,diaryType);
			var pager = jsondata.pager;
			var template = $.templates("#diaryDataTmpl");
			var htmlOutput = template.render(jsondata.result);
			$("#oaDiaryList").html(htmlOutput);
		}
	});	
}

function getOrDiaryData(currentPage, diaryType) {
	var jsonData;
	var url = "${_bathPath}/oaDiary/queryOaDiaryList";
	ajaxController.ajax({
		method : "POST",
		url : url,
		dataType : "json",
		showWait : true,
		async : false,
		data : {
			"currentPage" : currentPage,
			"diaryDate":$('#diaryDate').val(),
			"diaryType":diaryType
		},
		message : "正在加载数据..",
		success : function(data) {
			if(data.data!=null&&data.data.result!=null){
	            jsonData = data.data;//转换为json对象;
        		if(jsonData.result.length>0){
        			$(".tishi").css('display','none');
        			
        		}else{
        			$(".tishi").css('display','block');
        			$("#oaDiaryList").html("");
        			$('#pagination-diary').empty();
        			$('#pagination-diary').removeData("twbs-pagination");
        			$('#pagination-diary').unbind("page");
        		}
        	}else{
        		$(".tishi").css('display','block');
        		$("#oaDiaryList").html("");
        		$('#pagination-diary').empty();
    			$('#pagination-diary').removeData("twbs-pagination");
    			$('#pagination-diary').unbind("page");
        	}
		},
		failed : function() {
			bootbox.alert("查询日记列表出错!");
		}
	});
	return jsonData;
}

function showNotice(noticeId){
	
	ajaxController.ajax({
		method : "POST",
		url : "${_bathPath}/notice/getNoticeById",
		dataType : "json",
		showWait : true,
		async : false,
		data : {
			"noticeId" : noticeId,
		},
		message : "正在加载数据..",
		success : function(data) {
			var template = $.templates("#noticeDataTmpl");
			var htmlOutput = template.render(data);
			$("#noticeVo").html(htmlOutput);
			$("#notice_tanc_cz").show();
		},
		failed : function() {
			bootbox.alert("查询出错!");
		}
	});
}

function searchSchedule()
{
	var jsondata = getcustData(1);
	var pager = jsondata.pager;
	var template = $.templates("#custDataTmpl");
	var htmlOutput = template.render(jsondata.result);
	$("#custData").html(htmlOutput);
	

	$('#pagination-cust').empty();
	$('#pagination-cust').removeData("twbs-pagination");
	$('#pagination-cust').unbind("page");
	var pageData = $('#pagination-cust').data();
	$('#pagination-cust').twbsPagination({
		totalPages : pager.totalPages,
		visiblePages : 5,
		onPageClick : function(event, page) {
			var jsondata = getcustData(page);
			var pager = jsondata.pager;
			var template = $.templates("#custDataTmpl");
			var htmlOutput = template.render(jsondata.result);
			$("#custData").html(htmlOutput);
		}
	});	
}

function getcustData(currentPage) {
	var jsonData;
	
	var url = "${_bathPath}/schedule/queryScheduleList";
	ajaxController.ajax({
		method : "POST",
		url : url,
		dataType : "json",
		showWait : true,
		async : false,
		data : {
			"currentPage" : currentPage,
			"scheduleDate":$('#scheduleDate').val(),
		},
		message : "正在加载数据..",
		success : function(data) {
			if(data.data!=null&&data.data.result!=null){
	            jsonData = data.data;//转换为json对象;
        		if(jsonData.result.length>0){
        			$(".tishi").css('display','none');
        			
        		}else{
        			$(".tishi").css('display','block');
        			$("#custData").html("");
        			$('#pagination-cust').empty();
        			$('#pagination-cust').removeData("twbs-pagination");
        			$('#pagination-cust').unbind("page");
        		}
        	}else{
        		$(".tishi").css('display','block');
        		$("#custData").html("");
        		$('#pagination-cust').empty();
    			$('#pagination-cust').removeData("twbs-pagination");
    			$('#pagination-cust').unbind("page");
        	}
		},
		failed : function() {
			bootbox.alert("查询日程列表出错!");
		}
	});
	return jsonData;
}

</script>

 <div id="notice_tanc_cz" class="tanc_cz" style="display:none;z-index:12;position:fixed;top:10px">
 	 <div class="tanc_bg"></div>
 	 <div class="tanc_nr" style="height:600px;margin-top:-260px">
	  <div class="box_form">
	  <ul id="noticeVo">
	  
	   </ul>
	   <div class="tanc_qy">
	    <ul>
	     <li><button class="btnh sc1" style="float:right" onclick="cancelNotice()">关闭</button></li>
	    </ul>
	   </div>
	  </div>
	  </div>
	  
	  <script id="noticeDataTmpl" type="text/x-jsrender">
	   <li><span class="er_name">公告标题：</span><span>{{:title}}</span></li>
	   <li><span class="er_name">公告级别：</span><span>{{if level == '1' }}一般{{else level == '2'}}重要{{else}}紧急{{/if}}</span></li>
	   <li class="tex_to"><span class="er_name">内容：</span><span class="text_xs">{{if content.length > 50 }}{{:content.substring(0,50)}}...{{else}}{{:content}}{{/if}}</span></li>
	   <li><span class="er_name">发布范围：</span><span>{{:departmentName}}</span></li>
	   <li><span class="er_name">开始时间：</span><span>{{:startTime}}</span></li>
	   <li><span class="er_name">结束时间：</span><span>{{:endTime}}</span></li>
	   <li><span class="er_name">发布人：</span><span>{{:userName}}</span></li>
	</script>
 </div>
 
 <div id="diary_tanc_cz" class="tanc_cz" style="display:none;z-index:12;position:fixed;top:10px">
  <div class="tanc_bg"></div>
  <div class="tanc_nr" style="height:650px;margin-top:-300px">
  <div class="box_form">
    <ul>
     <li class="yiban" style="width: 50%;">
	   <span class="er_name">工作日记：</span>
						<span id="diaryStartDate" class="input-group" data-link-format="yyyy-mm-dd" data-link-field="diaryDate" >
							<input id="diaryDate" name="diaryDate" type="text" class="box_input" value="" readonly>
						</span>
						<span class="nr_h"><i class="icon-calendar"></i></span>
						</li>
    <button class="btn" onclick="searchOrDiary()" style="line-height: 5px;margin-left:20px"><i class="icon-search"></i> 查询</button>
    </ul>
   </div>
   <div class="table_tc" style="height:390px;">
   <tr class="tc_tr">
            	<td style="height:15px" align="center" valign="middle"></td>
        </tr>
   	   <table border="0" width="100%">
    	
       <tbody id="diaryData" class="ri_nrc">
		    <div class="ri_nrb">
		     <ul>
		     <input type="hidden" id="diaryType" value="" >
		      <li id="diaryAll" class="current"><a href="javascript:void(0)" onclick="searchOrDiary()">全部</a></li>
		      <li id="diaryDay"><a href="javascript:void(0)" onclick="searchOrDiary('1')">日计划</a></li>
		      <li id="diaryWeek"><a href="javascript:void(0)" onclick="searchOrDiary('2')">周计划</a></li>
		      <li id="diaryMouth"><a href="javascript:void(0)" onclick="searchOrDiary('3')">月计划</a></li>
		     </ul>
		    </div>
		    <div class="ri_nrc" id="oaDiaryList">
		     
		    </div>
       </tbody>
    	</table>
    <div class="tishi" style="display:none">
      	<p><img src="<%=_bathPath%>/images/tanc_sb.png"></p>
      	<span>sorry，我们全程搜索也没有找到他！</span>
	</div>
   </div>
   <script id="diaryDataTmpl" type="text/x-jsrender">
	  <ul style="height: 90px;overflow-y: auto;line-height: 18px;">
		      <li class="rj_a">{{:diaryDate}}</li>
		      <li class="rj_b">【{{if diaryType == '1'}}日{{else diaryType == '2'}}周{{else}}月{{/if}}计划】</li>
		      <li class="rj_c">
		       <p>{{:workSummary}}</p>
		       <p>{{:workPlan}}</p>
			   <p>{{:workExperience}}</p>
		      </li>
		     </ul>
	</script>
   <!-- 分页信息 -->
   <div class="pagination-diary" id="pagination-diary"></div>
   <div class="tanc_qy" style="margin-top:5px;">
    <ul>
     <li><button class="btnh sc1" onclick="cancelDiary()">关闭</button></li>
    </ul>
   </div>
  </div>
 </div>
 
 <div id="schedule_tanc_cz" class="tanc_cz" style="display:none;z-index:12;position:fixed;top:10px">
  <div class="tanc_bg"></div>
  <div class="tanc_nr" style="height:600px;margin-top:-300px">
  <div class="box_form">
    <ul>
     <li class="yiban" style="width: 50%;">
	   <span class="er_name">任务日程：</span>
						<span id="scheduleStartDate" class="input-group" data-link-format="yyyy-mm-dd" data-link-field="scheduleDate" >
							<input id="scheduleDate" name="scheduleDate" type="text" class="box_input" value="" readonly>
						</span>
						<span class="nr_h"><i class="icon-calendar"></i></span>
						</li>
    <button class="btn" onclick="searchSchedule()" style="line-height: 5px;margin-left:20px"><i class="icon-search"></i> 查询</button>
    </ul>
   </div>
   <div class="table_tc" style="height:376px;">
   	   <table border="0" width="100%">
    	<tr class="tc_tr">
            	<td style="height:15px" align="center" valign="middle"></td>
        </tr>
       <tbody id="custData" class="ri_nrc"></tbody>
    	</table>
    <div class="tishi" style="display:none">
      	<p><img src="<%=_bathPath%>/images/tanc_sb.png"></p>
      	<span>sorry，我们全程搜索也没有找到他！</span>
	</div>
   </div>
   <script id="custDataTmpl" type="text/x-jsrender">
	  <ul style="line-height: 18px;">
      <li class="rj_a">{{:dateStr}}</li>
      <li class="rj_c" style="max-height: 60px;overflow-y: auto;width: 570px;">
		{{if #data.contents != null}}
				{{for #data.contents}}
				<p {{if #data.status == 0}}style="text-decoration:line-through;"{{/if}}>{{:content}}</p>
				{{/for}}
				{{/if}}
      </li>
     </ul>
	</script>
	<!-- 分页信息 -->
   <div class="pagination-cust" id="pagination-cust" style="float:left;"></div>
   <div class="tanc_qy" style="margin-top:5px;">
    <ul>
     <li>
     <button class="btnh sc1" onclick="cancel()">关闭</button>
     </li>
    </ul>
   </div>
  </div>
 </div>
 
