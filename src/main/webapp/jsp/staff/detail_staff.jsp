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
    <script src="${_base }/jsp/staff/detail_staff.js"></script>
</head>
<body>

<div class="main_right">
		<!--右侧大框架-->
		<!--右侧菜单地址-->
		<div class="main_right_Title">
			<p>员工查看</p>
		</div>
		<!--查询列表区域-->
		<div class="col-xs-12">
			<div class="query_list query_list_k_paad">
				<!--外侧白色背景框架-->
				<div class="query_bumen_big">
          			<div class="shenq shenq_noborder">查看员工信息</div> 
              		<div class="query_bumen">
				            <ul>
				                <li ><i class="x_red">*</i>员工名称:${staffVo.staffName}
				                	<input type="hidden" name="staffId" id="staffId" value="${staffVo.staffId}"/>
				                </li>
				            </ul>
				            <ul>
				                <li><i class="x_red">*</i>员工工号:${staffVo.staffNo}
				                </li>
				            </ul>
				            <ul>
				                <li ><i class="x_red">*</i>所属部门:${staffVo.departName}
				                </li>
				            </ul>
				           <ul>
				                <li ><i class="x_red">*</i>生效时间:${staffVo.activityDate}
				                </li>
				            </ul>
				            <ul>
				                <li ><i class="x_red">*</i>失效时间:${staffVo.inactivityDate}
				                </li>
				            </ul>
				            <ul>
				               <li> <i class="x_red">*</i>员工类别:${staffVo.staffClass}
				               </li>
				            </ul>
				            <ul>
						        <li>员工职位:${staffVo.positionCode}
						        </li>
					        </ul>
				            <ul>
				                <li >联系电话:${staffVo.contactTel}
				                </li>
				            </ul>
				            <ul>
				                <li>联系邮箱:${staffVo.email}
				                </li>
				            </ul>
				            <ul>
				                <li>联系地址:${staffVo.address}
				                </li>
				            </ul>
				            <ul>
				                <li>邮编:${staffVo.postcode}
				                </li>
				            </ul>
				            <ul>
				                <li>二维码:<img src="${_base}/maintain/staff/getStaffQRCode?staffId=${staffVo.staffId}" id="randomImg">
				                </li>
				            </ul>
				            <ul>
				            	<!-- <li >&nbsp;</li> -->
				                <li>
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