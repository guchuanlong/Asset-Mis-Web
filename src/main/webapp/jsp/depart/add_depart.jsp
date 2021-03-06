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
    <title>添加机构</title>
    <script src="${_base }/jsp/depart/add_depart.js"></script>
</head>
<body>
 <div class="main_right">
		<!--右侧大框架-->
		<!--右侧菜单地址-->
		<div class="main_right_Title">
			<p>部门添加</p>
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<!--外侧白色背景框架-->
				<div class="query_bumen_big">
          			<div class="shenq shenq_noborder">新增部门信息</div> 
              		<div class="query_bumen">
				            <ul>
				               <li ><i class="x_red">*</i> 部门名称:
				               	<input type="text" class="query_input_c" id="departName" name="departName"
				                       required maxlength="30"/>
				               </li>
				            </ul>
				            <ul>
				               <li> <i class="x_red">*</i>部门类型:
				               	<ai:select dictId="GN_DEPART.DEPART_KIND_TYPE" styleClass="query_input_c" id="departKindType"
				                           name="departKindType">
				                </ai:select>
				               </li>
				            </ul>
				            <ul>
				                <li >&nbsp;上级部门:
				                	<input type="text" class="query_input_c" id="parentDepartName" name="parentDepartName"
				                       onclick="showDepart();" readonly >
				                	<input type="hidden" id="parentDepartId" name="parentDepartId" value=""/>
				               </li>
				            </ul>
				            <ul >
				                <li ><i class="x_red">*</i>归属省份:
				                	<ai:gnProvince styleClass="query_input_c" name="provinceSelect" id="provinceSelect" nullText="请选择"
				                               notnull="true" onchange="updateCity();"/>
				                	<input type="hidden" id="provinceCode" name="provinceCode" value="" />
				                </li>
				            </ul>
				            <ul>
				               <li> <i class="x_red">*</i>归属地市:
				               	<select class="query_input_c" id="cityCode" name="cityCode" onchange="updateDomainCode();">
				                    <option>请选择</option>
				                </select>
				                <script id="cityCodeTmpl" type="text/x-jsrender">
                    						<option value="{{:areaCode}}">{{:areaName}}</option>
                						</script>
						        <script id="cityCodeSpecialTmpl" type="text/x-jsrender">
                    						<option value="000">全国</option>
                						</script></li>
				            </ul>
				            <ul>
				               <li><i class="x_red">*</i>归属区域:
				               	<select class="query_input_c" id="areaCode" name="areaCode">
				                    <option>请选择</option>
				                </select>
				                <script id="areaCodeTmpl1" type="text/x-jsrender">
                    					<option value="{{:areaCode}}">{{:areaName}}</option>
                				</script>
              					<script id="areaCodeSpecTmpl" type="text/x-jsrender">
                    					<option value="0000">全国</option>
                				</script>
						       </li>
				            </ul>
				            <ul>
				               <li>&nbsp;联系人:&nbsp;&nbsp;
				               	<input type="text" class="query_input_c" maxlength="14" name="contactName" id="contactName">
				               </li>
				            </ul>
				            <ul>
				               <li> 联系电话:
				               	<input type="text" class="query_input_c" name="contactTel"
				                       id="contactTel" maxlength="16">
				            </ul>
				            <ul>
				               <li> 联系地址:
				               	<input type="text" class="query_input_c" maxlength="100" name="address" id="address"
				                       value="${staffVo.address}">
				               </li>
				            </ul>
				            <ul>
				               <li> &nbsp;&nbsp;&nbsp;&nbsp;邮编:&nbsp;&nbsp;
				               	<input type="text" maxlength="6" name="postcode" class="query_input_c"
				                       id="postcode" >
				               </li>
				            </ul>
				            <ul><li></li></ul>
				            <ul>
				            	 <li>&nbsp;</li>
				                 <li>
				                 	<button type="button" class="form_btn" onclick="addDepart()">提交</button>
				                 	&nbsp;  &nbsp;  &nbsp;
				                 	<button type="button"  class="form_btn" id="add_back">返回</button>
				                </li>
				            </ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>