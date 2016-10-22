<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
		<div class="tanc_bg"></div>
		<div class="tanc_nr">
			<div class="tanc_gb">
				<a href="javascript:cancelRole();"><img src="${_template_path }/images/close2.png"></a>
			</div>
			<div class="page-header">
            	<h4 id="topTitle">角色管理</h4>
        	</div>
				<div class="tanc_ss">
					<input id="roleId" name="roleId" type="hidden"/>
					<table>
						<tbody>
							<tr>
						        <td class="title"><i class="x_red">*</i>角色名称：</td>
						        <td class="value">
						            <input type="text" class="form-control" name="roleName" id="roleName"/>
						        </td>
						        <td class="title">角色描述：</td>
						        <td class="value">
						            <input type="text" class="form-control" name="roleDesc" id="roleDesc"/>
						        </td>
					    	</tr>
							<!-- <tr>
								<td class="title"><i class="x_red">*</i>系统标识：</td>
		                    	<td class="value">
		                    		<select class="form-control" name="systemId" id="systemId"><option value=''>请选择</option></select>
		                    	</td>
						        <td class="title"><i class="x_red">*</i>频道名称：</td>
						        <td class="value">
						            <select class="form-control" name="systemModeId" id="systemModeId"><option value=''>请选择</option></select>
						        </td>
						    </tr> -->
						    <tr>
				        		<td class="title"><i class="x_red">*</i>生效时间：</td>
						        <td class="value">
	                				<span class="input-group">
										<input class="form-time" type="text" endDate=$dp.$('endDate'); onClick="WdatePicker({onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}'});" id="startDate" name="activeTime" readonly/>
						            	<span class="sp_h"><i class="icon-calendar"></i></span>
						            </span>
						        </td>
						        <td class="title"><i class="x_red">*</i>失效时间：</td>
						        <td class="value">
					                <span class="input-group">
										<input class="form-time" type="text" onClick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'});" id="endDate" name="inactiveTime" readonly/>
						            	<span class="sp_h"><i class="icon-calendar"></i></span>
						            </span>
						        </td>
						    </tr>
							<!-- <tr>
						        <td class="title"><i class="x_red">*</i>生效时间：</td>
						        <td class="value">
					                <span class="input-group" data-link-format="yyyy-mm-dd" data-link-field="activeTime" id="activeTimePicker">
					                	<input type="text" class="form-time" name="activeTime" id="activeTime" readonly/>
					                    <span class="sp_h"><i class="icon-calendar"></i></span>
					                </span>
						        </td>
						        <td class="title"><i class="x_red">*</i>失效时间：</td>
						        <td class="value">
						                <span class="input-group" data-link-format="yyyy-mm-dd" data-link-field="inactiveTime" id="inactiveTimePicker">
						                	<input type="text" class="form-time" name="inactiveTime" id="inactiveTime" readonly/>
						                    <span class="sp_h"><i class="icon-calendar"></i></span>
						                </span>
						        </td>
					    	</tr> -->
						</tbody>
					</table>
				</div>
				<div class="tanc_qy">
					<ul>
						<li><button id="cancelBtn" type="button" class="form_btn" onclick="cancelRole()">关闭</button></li>
						<li><button id="submitBtn" type="button" class="form_btn" onclick="addOrUpdateRoleData()">提交</button></li>
					</ul>
				</div>
		</div>
	</div>