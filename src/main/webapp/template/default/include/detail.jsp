<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="er_box">
	<div class="box_title">客户资料</div>
	<div id="orderCustInfo"></div>

	<div id="orderBisInfoTitle" class="box_title" style="display:none">业务受理信息</div>
	<div class="box_form xian" >
		<ul id="orderBisInfo"></ul>
	</div>
	
	<div id="orderBisExtInfoTitle" class="box_title" style="display:none">受理附加信息</div>
	<div class="box_form xian" >
		<ul id="orderBisExtInfo"></ul>
	</div>
	
 	<div id="orderBisAcctInfoTitle" class="box_title" style="display:none">宽带账号</div>
	<div id="orderBisAcctInfo"></div>
	
	<div class="box_title">费用信息</div>
	<div id="orderFeeInfo"></div>
</div>
<script id="orderStateTmpl" type="text/x-jsrender">{{:state }}</script>
<script id="orderCustInfoTmpl" type="text/x-jsrender">
	<div class="box_list xian">
		<span class="quan">{{:custBaseInfo.custName}}  {{:custBaseInfo.custType}}</span>
		<span>身份信息：{{:custBaseInfo.certType}}  {{:custBaseInfo.certNum}}</span>
		<P>
			{{if custBaseInfo.custType == '个人客户' }}
				<span>联系人：{{:custPersonal.contactName}}</span>
				<span>联系电话：{{:custPersonal.contactTel}}</span>
				<span>联系人邮箱：{{:custPersonal.contactEmail}}</span>
				<span>联系人微信号：{{:custPersonal.contactWxId}}</span>
			{{else custBaseInfo.custType == '单位客户'}}
				<span>联系人：{{:custGroup.contactName}}</span>
				<span>联系电话：{{:custGroup.contactOfficeTel}}</span>
				<span>联系人邮箱：{{:custGroup.contactEmail}}</span>
				<span>联系人微信号：{{:custGroup.contactWxId}}</span>
			{{/if}}
		</P>
	</div>
</script>
<script type="text/x-jsrender" id="orderBisExtInfoTmpl">
		{{if bisExtrInfo != null}}
		    {{for bisExtrInfo}}
				<li class='yiban'><span class="er_name">{{:#data.attrTitle}}:</span><span>{{:#data.attrContent}}</span></li>
			{{/for}}
		{{/if}}
</script>
<script type="text/x-jsrender" id="orderBisInfoTmpl">
		{{if bisInfo != null}}
		    {{for bisInfo}}
				{{if #data.attrTitle == '' || #data.attrTitle == 'undefined'}}
		 			<li class='yiban' style='width:100%;'><span class="er_name">&nbsp;</span><span>{{:#data.attrContent}} </span></li>
				{{else}}
		 			<li class='yiban' style='width:100%;'><span class="er_name">{{:#data.attrTitle}}:</span><span>{{:#data.attrContent}} </span></li>
				{{/if}}
				
			{{/for}}
		{{/if}}
</script>
<script type="text/x-jsrender" id="orderBisAcctTmpl">
<div class="box_form xian">
		  <ul>
		 	<li class="yiban"><span class="er_name">{{:bisAcctNo.attrTitle}}：</span><span>{{:bisAcctNo.attrContent}} </span></li>
		  </ul>
	</div>
</script>
<script id="orderFeeInfoTmpl" type="text/x-jsrender">
	<div class="tablea">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		   <tr style="background:#d9d9db;">
		    <td>费用名称</td>					
		    <td>费用金额（元）</td>
		    <td>减免金额（元）</td>
		    <td>实际应收金额（元）</td>	
		    <td>减免原因</td>
		   </tr>
		   {{if feeDetails != null}}
		   {{for feeDetails}}
		   <tr>
		    <td>{{:#data.feeItemId}}</td>	
			<td><span class="quan">{{:~liToYuan(#data.totalFee, 2)}}</span></td>
			<td><span class="quan">{{:~liToYuan(#data.operDiscountFee, 2)}}</span></td>
			<td><span class="quan">{{:~liToYuan(#data.adjustFee, 2)}}</span></td>
			<td>{{:#data.discountDesc}}</td>
		   </tr>
		   {{/for}}
		   {{/if}}
		</table>
	</div>
	<div class="tableb">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		   <tr>
		    <td width="90%" align="right">金额总计：</td>
		    <td>¥{{:~liToYuan(totalFee)}}</td>
		   </tr>
		   <!--<tr>
		    <td width="90%" align="right">-优惠：</td>
		    <td>¥{{:~liToYuan(discountFee, 2)}}</td>
		   </tr>-->
		   <tr>
		    <td width="90%" align="right">减免：</td>
		    <td>¥{{:~liToYuan(operDiscountFee, 2)}}</td>
		   </tr>
		   <tr>
		    <td width="90%" align="right">＋运费：</td>
		    <td>¥0.00</td>
		   </tr>
		   <tr style="color:#f00;">
		    <td width="90%" align="right">应收金额：</td>
		    <td>¥{{:~liToYuan(adjustFee, 2)}}</td>
		   </tr>
		</table>
	</div>
</script>