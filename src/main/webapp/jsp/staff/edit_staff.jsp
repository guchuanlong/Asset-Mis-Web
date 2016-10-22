<%@ page contentType="text/html; charset=UTF-8" language="java" %>
 <%@ taglib prefix="ai" uri="/WEB-INF/tag/ai-tags.tld" %>

<html>
<head>
 <%

String _base = request.getContextPath();
request.setAttribute("_base", _base);
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma", "No-cache");
%>
<script>
	var _base = "${_base}";
</script>
    <title>修改员工</title>
    <script src="${_base }/jsp/staff/edit_staff.js"></script>
    <%-- <script type="text/javascript" src="${_base}/resources/My97DatePicker/WdatePicker.js"></script> --%>
</head>
<body>

<div class="main_right">
		<!--右侧大框架-->
		<!--右侧菜单地址-->
		<div class="main_right_Title">
			<p>员工修改</p>
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<!--外侧白色背景框架-->
				<div class="query_bumen_big">
          			<div class="shenq shenq_noborder">修改员工信息</div> 
              		<div class="query_bumen">
				            <ul>
				                <li ><i class="x_red">*</i>员工名称:
				                	<input type="text" class="query_input_c" id="staffName" name="staffName"  value="${staffVo.staffName}" readonly/>
				                	<input type="hidden" name="staffId" id="staffId" value="${staffVo.staffId}"/>
				                </li>
				            </ul>
				            <ul>
				                <li><i class="x_red">*</i>员工工号:
				                	<input type="text" class="query_input_c" id="staffNo" name="staffNo" value="${staffVo.staffNo}" readonly/>
				                </li>
				            </ul>
				            <ul>
				                <li ><i class="x_red">*</i>所属部门:
				                	<input type="text" class="query_input_c" id="departName" name="departName" onclick="showDepart();"
				                         value="${staffVo.departName}" readonly/>
				                	<input type="hidden" id="departId" name="departId" value="${staffVo.departId}"/>
				                </li>
				            </ul>
				           <ul>
				                <li ><i class="x_red">*</i>生效时间:
				                	<input class="query_input_c" type="text" id="startDate" endDate=$dp.$('endDate'); onClick="WdatePicker({onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}'});" name="activityDate" value="${staffVo.activityDate}" readonly/>
				                </li>
				            </ul>
				            <ul>
				                <li ><i class="x_red">*</i>失效时间:
				                	<input class="query_input_c" type="text" onClick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'});" name="inactivityDate" value="${staffVo.inactivityDate}" id="endDate" readonly/>
				                </li>
				            </ul>
				            <ul>
				               <li> <i class="x_red">*</i>员工类别:
				                  <ai:select dictId="GN_STAFF.STAFF_CLASS" styleClass="query_input_c" id="staffClass"
				                      name="staffClass" defaultValue="${staffVo.staffClass}">
				                  </ai:select>
				               </li>
				            </ul>
				            <ul>
						        <li>员工职位:
						          	<ai:select dictId="GN_STAFF.POSITION_CODE" styleClass="query_input_c" id="positionCode"
						                          name="positionCode" defaultValue="${staffVo.positionCode}" nullOption="true">
						            </ai:select>
						        </li>
					        </ul>
				            <ul>
				                <li >联系电话:
				                	<input type="text" class="query_input_c" name="contactTel"
				                       id="contactTel"  value="${staffVo.contactTel}" maxlength="16"/>
				                </li>
				            </ul>
				            <ul>
				                <li>联系邮箱:
				                	<input type="text" class="query_input_c" id="email" name="email" value="${staffVo.email}" maxlength="64"/>
				                </li>
				            </ul>
				            <ul>
				                <li>联系地址:
				                	<input type="text" class="query_input_c" maxlength="100" name="address" value="${staffVo.address}" id="address"/>
				                </li>
				            </ul>
				            <ul>
				                <li>&nbsp;&nbsp;邮编:&nbsp;&nbsp;&nbsp;&nbsp;
				                	<input type="text"  maxlength="6" name="postcode" value="${staffVo.postcode}" class="query_input_c"
				                       id="postcode" />
				                </li>
				            </ul>
				            <ul>
				            	<li >&nbsp;</li>
				                <li>
				                	<button class="form_btn" type="button" onclick="updateStaff()">提交</button>
				                	&nbsp;  &nbsp;  &nbsp;
				                	<button class="form_btn" type="button"  id="editstaff_back">返回</button>
				               </li>
				            </ul>
        			</div>
            	</div>
        	</div>
    	</div>
	</div>
</body>
</html>