<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<%@ include file="/jsp/common/common.jsp"%>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/calendar.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/index-context.js"></script>
<html>
<head>
<title>宽带在线</title>
</head>

<body>
   
   <div class="wrok">
      <div class="wrok_table">
      <ul>
      <li>最新公告：</li>
      
      <div id="report" style="overflow:hidden;height:30px;width:800px;float:left;">
		<table cellpadding="0" cellspace="0" border="0">
		<tr>
		<td id="report1">
		<table id="noticeTable" width="1500px;" border="0" cellspacing="0" cellpadding="0">
		    <tr id="noticeContent">
		    <!-- 
		      <li><span>[2015-09-23]</span>  关于2015年上半年绩效考核员工自评及经理评估截止时间的提醒</li>
		      <li><span>[2015-09-23]</span>  关于2015年上半年绩效考核员工自评及经理评估截止时间的提醒</li>
		      <li><span>[2015-09-23]</span>  关于2015年上半年绩效考核员工自评及经理评估截止时间的提醒</li>
		       -->
		    </tr>
		</table></td>
		<td id="report2"></td>
		</tr>
		</table>
	</div>

		<li class="right" style="cursor:pointer" onclick="toNoriceQuery()">>></li>

      </ul>
      </div>
      
      <div class="wrok_table_list">
      
      
         <div class="wrok_table_list_left">
         <div class="wrok_table_list_left_Tile">
         <ul>
         <li>工作日志</li>
         <li class="right"><p><img src="${_bathPath}/images/riji.jpg"></p><p><A href="javascript:void(0)" onclick="showOrDiary()">查看历史</A></p></li>
         </ul>
         </div>
         
         
           <input type="hidden" id="orDiaryType" name="orDiaryType" value="1">
           <div id="riji1">
         
          <div class="left_table">
          <ul>
          <li class="xuanz"><a href="#" onMouseDown="a(1)">日计划</a></li>
          <li><a href="#" onMouseDown="a(2)">周计划</a></li>
          <li><a href="#" onMouseDown="a(3)">月计划</a></li>
          </ul>
          </div>                 
          
          <div class="left_table_input">
          <ul>
          <form id="dayPlanForm" method="post">
          <input type="hidden" name="diaryType" value="1">
          <input type="hidden" name="creatDate">
          <input type="hidden" name="diaryId">
          <li><textarea name="workSummary" spellcheck="false" placeholder="填写今日工作总结" class="riji_textarea"> </textarea></li>
          <li><textarea name="workPlan" spellcheck="false" placeholder="填写明日工作计划" class="riji_textarea"> </textarea></li>
          <li><textarea name="workExperience" spellcheck="false" placeholder="填写工作心得体会" class="riji_textarea"> </textarea></li>
          </form>
          <li><input type="button" class="input_btn" onclick="sumbitForm(1)" value="提交"></li>
          </ul>
          </div>
          
          </div>
           <div id="riji2" style=" display:none;">
         
          <div class="left_table">
          <ul>
          <li><a href="#" onMouseDown="a(1)">日计划</a></li>
          <li class="xuanz"><a href="#" onMouseDown="a(2)">周计划</a></li>
          <li><a href="#" onMouseDown="a(3)">月计划</a></li>
          </ul>
          </div>                 
          
          <div class="left_table_input">
          <ul>
          <form id="weekPlanForm" method="post">
          <input type="hidden" name="diaryType" value="2">
          <input type="hidden" name="creatDate">
          <input type="hidden" name="diaryId">
          <li><textarea name="workSummary" spellcheck="false" placeholder="填写本周工作总结" class="riji_textarea"> </textarea></li>
          <li><textarea name="workPlan" spellcheck="false" placeholder="填写下周工作计划" class="riji_textarea"> </textarea></li>
          <li><textarea name="workExperience" spellcheck="false" placeholder="填写本周心得体会" class="riji_textarea"> </textarea></li>
          </form>
          <li><input type="button" class="input_btn" onclick="sumbitForm(2)" value="提交"></li>
          </ul>
          </div>
          
          </div>
           <div id="riji3" style=" display:none;">
         
          <div class="left_table">
          <ul>
          <li><a href="#" onMouseDown="a(1)">日计划</a></li>
          <li><a href="#" onMouseDown="a(2)">周计划</a></li>
          <li class="xuanz"><a href="#" onMouseDown="a(3)">月计划</a></li>
          </ul>
          </div>                 
          
          <div class="left_table_input">
          <ul>
          <form id="mouthPlanForm" method="post">
          <input type="hidden" name="diaryType" value="3">
          <input type="hidden" name="creatDate">
          <input type="hidden" name="diaryId">
          <li><textarea name="workSummary" spellcheck="false" placeholder="填写本月工作总结" class="riji_textarea"> </textarea></li>
          <li><textarea name="workPlan" spellcheck="false" placeholder="填写下月工作计划" class="riji_textarea"> </textarea></li>
          <li><textarea name="workExperience" spellcheck="false" placeholder="填写本月心得体会" class="riji_textarea"> </textarea></li>
          </form>
          <li><input type="button" class="input_btn" onclick="sumbitForm(3)" value="提交"></li>
          </ul>
          </div>
          
          </div>
         
       </div>
       
      
       <div class="wrok_table_list_right">
        <div class="wrok_table_list_left_Tile">
         <ul>
         <li>任务日程</li>
         <li class="right"><p><img src="${_bathPath}/images/riji.jpg"></p><p><A href="javascript:void(0)" onclick="showSchedule()">查看历史</A></p></li>
         </ul>
         </div>
       <input type="hidden" id="dateYear" name="dateYear" value="">
       <input type="hidden" id="dateMoth" name="dateMoth" value="">
       <input type="hidden" id="dateDay" name="dateDay" value="">
       <div class="right_tu">
       		<div class="day">
       			<div class="DaySelect">
			        <i class="lr" onclick="Month('l')" title="上一月"><</i>
			        <div class="select">
			            <div class="stop" id="cal_cy">2013</div>
			            <div class="sbox">
			                <ul id="YearAll">
			                    <li>2013</li>
			                </ul>
			            </div>
			        </div>
			        年
			        <div class="select" id="sm">
			            <div class="stop" id="cal_cm">06</div>
			            <div class="sbox" id="mm">
			                <ul id="DateAll">
			                    <li>01</li>
			                </ul>
			            </div>
			        </div>
			        月
			        <i class="lr" onclick="Month('r')" title="下一月">></i>
			        <i onclick="now()">今天</i>
			    </div>
			    <div id="DayAll"></div>
			</div>
       </div>
        
        <div class="right_input">
        <ul>
        <li><input type="text" class="rt_input" id="schContent" value=""></li>
        <li><input type="button" class="rt_btn" onclick="saveSchedule()" value="保存"></li>
        </ul>
         </div>
       
       <div class="right_checkbox">
       <ul id="scheduleShow">
       <!-- 
       <li>
       <p><input type="checkbox"></p>
       <p>下午14：00开会</p>
       </li>
       <li>
       <p><input type="checkbox"></p>
       <p>下午14：00开会</p>
       </li>
        -->
       </ul>
       
       </div>
       
       
       </div>
      
      
      
      </div>
      
   
   
   </div>


<script id="scheduleDataImpl" type="text/x-jsrender">
	<li>
	   <div style="width:3%;float:left;"><input type="checkbox" onclick="checkSchedule('{{:scheduleId}}')" {{if #data.scheduleStatue == '0'}} checked="checked" disabled="true"{{/if}}></div>
	   <div style="width:95%;float:right;">{{:scheduleContent}}</div>
    </li>
</script>

<!--1-->

</html>
