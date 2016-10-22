<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="ai" uri="/WEB-INF/tag/ai-tags.tld" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
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
    <title>编辑小区</title>
    <script src="${_base }/jsp/area/area_form.js"></script>
</head>
<body>
<div class="main_right">
		<!--右侧大框架-->
		<!--右侧菜单地址-->
		<div class="main_right_Title">
			<c:if test="${empty areaDto.areaCode }">
				<p>小区新增</p>
			</c:if>
			<c:if test="${not empty areaDto.areaCode }">
				<p>小区修改</p>
			</c:if>
			
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<!--外侧白色背景框架-->
				<div class="query_bumen_big">
			    	<div class="shenq shenq_noborder">
				    	<c:if test="${empty areaDto.areaCode }">
				    		新增小区信息
				    	</c:if>
				    	<c:if test="${not empty areaDto.areaCode }">
				    		修改小区信息
				    	</c:if>
			    	</div>
		    		<div class="query_bumen">
				        <form data-toggle="validator" role="form" method="post" name="saveAreaForm" id="saveAreaForm">
				        	<!-- 主键  依此判断是新增还是修改 -->
				        	<input type="hidden" name="areaCode" id="areaCode"  value="${areaDto.areaCode }">
				            <ul >
				                <li><i class="x_red">*</i>归属省份:
				                	<ai:gnProvince styleClass="query_input_c" name="provinceCode" id="provinceCode" nullText="--请选择--"
				                               notnull="true" onchange="updateCity();" defaultValue="${areaDto.provinceCode }"/>
				                </li>
				            </ul>
				            <ul>
				               <li> <i class="x_red">*</i>归属地市:
				               	<select class="query_input_c" id="cityCode" name="cityCode" onchange="updateCounty();" defaultValue="${areaDto.cityCode }">
				                    <option>--请选择--</option>
				                </select>
				                <script id="cityCodeTmpl" type="text/x-jsrender">
                    				<option value="{{:areaCode}}">{{:areaName}}</option>
                				</script>
              					</li>
				            </ul>
				            <ul>
				               <li> <i class="x_red">*</i>归属区县:
				               	<select class="query_input_c" id="countyCode" name="countyCode" onchange="updateStreet();" defaultValue="${areaDto.countyCode }">
				                    <option>--请选择--</option>
				                </select>
				                <script id="countyCodeTmpl" type="text/x-jsrender">
                    				<option value="{{:areaCode}}">{{:areaName}}</option>
                				</script>
              					</li>
				            </ul>
				            <ul>
				               <li> <i class="x_red">*</i>归属街道:
				               	<select class="query_input_c" id="streetCode" name="streetCode" defaultValue="${areaDto.streetCode }">
				                    <option>--请选择--</option>
				                </select>
				                <script id="streetCodeTmpl" type="text/x-jsrender">
                    				<option value="{{:areaCode}}">{{:areaName}}</option>
                				</script>
						       </li>
				            </ul>
				            <ul>
				               <li>小区名称:
				               	<input type="text" class="query_input_c" name="areaName" id="areaName" maxlength="500" value="${areaDto.areaName }">
				               </li>
				            </ul>
				            <ul>
				            	 <li>&nbsp;</li>
				            	 <li>&nbsp;</li>
				            	 <li>&nbsp;</li>
				                 <li>
				                 	<button type="button" class="form_btn" id="btnSaveArea">提交</button>
				                 	&nbsp;  &nbsp;  &nbsp;
				                 	<button type="button" class="form_btn" id="add_back">返回</button>
				                 </li>
				            </ul>
				        </form>
					 </div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>