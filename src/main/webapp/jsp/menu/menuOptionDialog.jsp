<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<!-- 新增、修改-->
<div id=menuDialog class="tanc_cz" style="display: none; z-index: 12; position: fixed;">
	<div class="tanc_bg"></div>
	<div class="tanc_nr">
		<div class="tanc_gb">
			<a href="javascript:void(0);" onclick="cancel()"><img src="${_template_path }/images/close2.png"></a>
		</div>
		<div class="page-header">
            <h4 id="topTitle">菜单管理</h4>
        </div>
			<div class="tanc_ss">
				<input name="menuId" type="hidden" />
                <input type="text" style="display:none" name="menuPid" id="menuPid" readonly/>
				<table>
					<tbody>
						<tr>
							<td class="title">上级菜单：</td>
                    		<td class="value">
		                        <input type="text" class="form-control" name="menuPidName" id="menuPidName" onclick="showPidMenu();" readonly/>
		                    </td>
		                    <td class="title"><i class="x_red">*</i>菜单名称：</td>
		                    <td class="value">
		                        <input type="text" class="form-control" name="menuName" />
		                    </td>
                		</tr>
			    		<tr>
                    		<td class="title">菜单URL：</td>
                    		<td class="value">
		                        <input type="text" class="form-control" name="menuUrl"/>
		                    </td>
                    		<td class="title">菜单描述：</td>
		                    <td class="value">
		                        <input type="text" class="form-control" name="menuDesc"/>
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
									<input class="form-time" type="text" endDate=$dp.$('endDate'); onClick="WdatePicker({onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}'});" id="startDate" name="activeTimeStr" readonly/>
					            	<span class="sp_h"><i class="icon-calendar"></i></span>
					            </span>
					        </td>
					        <td class="title"><i class="x_red">*</i>失效时间：</td>
					        <td class="value">
				                <span class="input-group">
									<input class="form-time" type="text" onClick="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}'});" id="endDate" name="inactiveTimeStr" readonly/>
					            	<span class="sp_h"><i class="icon-calendar"></i></span>
					            </span>
					        </td>
					    </tr>
					</tbody>
				</table>
			</div>
			<div class="tanc_qy">
				<ul>
					<li><button type="button" class="form_btn" onclick="cancel()">关闭</button></li>
					<li><button type="button" class="form_btn" id="submitBtn" onclick="confirmAddMenuDate()">提交</button></li>
				</ul>
			</div>
	</div>
</div>