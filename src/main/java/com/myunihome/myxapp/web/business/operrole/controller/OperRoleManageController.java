package com.myunihome.myxapp.web.business.operrole.controller;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.sys.api.sysoperrole.interfaces.IGnOperRoleManageSV;
import com.myunihome.myxapp.sys.api.sysoperrole.param.GnOperRoleParamsVO;
import com.myunihome.myxapp.sys.api.sysoperrole.param.GnOperRoleResponseVO;
import com.myunihome.myxapp.sys.api.sysoperrole.param.GnOperRoleVO;
import com.myunihome.myxapp.sys.api.sysrole.interfaces.IGnRoleManageSV;
import com.myunihome.myxapp.sys.api.sysrole.param.GnRoleParamsVO;
import com.myunihome.myxapp.sys.api.sysrole.param.GnRoleVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.business.oper.controller.OperManageController;
import com.myunihome.myxapp.web.system.constants.Constants;
import com.myunihome.myxapp.web.system.util.DateTimeUtil;

@Controller
@RequestMapping(value = "/operRoleManage")
public class OperRoleManageController {
	private static final Logger LOGGER = Logger.getLogger(OperManageController.class);

	private static final int PAGESIZE = 5;// 单页大小

	/**
	 * 操作员赋予角色操作
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/addRoleToOper", method = RequestMethod.POST)
	public ResponseData<String> addRoleToOper(HttpServletRequest request, @RequestParam("operRoleList") String operRoleList) {
		ResponseData<String> responseData = null;
		String statusCode = null;// responseData statusCode
		String statusInfo = null;// responseData statusInfo
		String dataInfo = null;// responseData data
		try {
			Type listType = new TypeToken<LinkedList<GnOperRoleParamsVO>>() {
			}.getType();
			Gson gson = new Gson();
			operRoleList = StringEscapeUtils.unescapeHtml4(operRoleList);
			LinkedList<GnOperRoleParamsVO> operRoleVOList = gson.fromJson(operRoleList, listType);
			if (operRoleVOList != null && operRoleVOList.size() > 0) {
				IGnOperRoleManageSV operRoleManageSV = DubboConsumerFactory.getService("gnOperRoleManageSV");
				UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
				for (GnOperRoleParamsVO operRoleVO : operRoleVOList) {
					setOperRoleDefValues(operRoleVO, user);
					GnOperRoleResponseVO operRoleResponse = operRoleManageSV.addRoleToOper(operRoleVO);
					String responseCode = operRoleResponse.responseCode;
					if (ResponseData.AJAX_STATUS_SUCCESS.equals(responseCode)) {
						statusCode = ResponseData.AJAX_STATUS_SUCCESS;
						statusInfo = "操作员添加角色权限成功";
						dataInfo = operRoleResponse.respondeDesc;
					} else {
						statusCode = ResponseData.AJAX_STATUS_FAILURE;
						statusInfo = "操作员添加角色权限失败";
						dataInfo = operRoleResponse.respondeDesc;
						break;
					}
				}
			} else {
				statusCode = ResponseData.AJAX_STATUS_FAILURE;
				statusInfo = "添加失败，无操作员添加角色权限的相关数据！";
				dataInfo = "添加失败，无操作员添加角色权限的相关数据！";
			}
		} catch (Exception e) {
			LOGGER.error("添加角色信息失败！错误信息：", e);
			statusCode = ResponseData.AJAX_STATUS_FAILURE;
			statusInfo = "操作员添加角色权限失败！";
			dataInfo = "添加失败，无操作员添加角色权限的相关数据！";
		} finally {
			responseData = new ResponseData<String>(statusCode, statusInfo, dataInfo);
		}
		return responseData;
	}

	/**
	 * 设置对象默认值
	 * 
	 * @param operRole
	 * @throws Exception
	 */
	private void setOperRoleDefValues(GnOperRoleParamsVO operRoleVO, UserLoginVo user) throws Exception {
		operRoleVO.setTenantId(user.getTenantId());
		operRoleVO.setCreateOperId(user.getOperId());
		Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
		String timstampStr = DateTimeUtil.timstamp3String(currTimestamp);
		operRoleVO.setActiveTime(DateTimeUtil.stringToTimstamp(timstampStr));
		operRoleVO.setInactiveTime(DateTimeUtil.stringToTimstamp("2099-12-31"));
		operRoleVO.setCreateType("1");
	}

	@ResponseBody
	@RequestMapping("/queryOperRoleList")
	public ResponseData<PagerResult<GnOperRoleVO>> queryOperRoleList(HttpServletRequest request, GnOperRoleParamsVO gnOperRoleParamsVO,
			@RequestParam("currentPage") Integer currentPage) {

		ResponseData<PagerResult<GnOperRoleVO>> responseData = null;
		try {

			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			gnOperRoleParamsVO.setTenantId(user.getTenantId()); // 租户ID

			PageInfo<GnOperRoleVO> pageInfo = new PageInfo<GnOperRoleVO>();
			pageInfo.setPageNo(currentPage);
			pageInfo.setPageSize(PAGESIZE);
			gnOperRoleParamsVO.setPageInfo(pageInfo); // 分页信息
			// 设置生效失效过滤条件 只查有效数据
			Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
			//gnOperRoleParamsVO.setActiveTime(currTimestamp);
			gnOperRoleParamsVO.setInactiveTime(currTimestamp);

			// 角色信息查询服务
			IGnOperRoleManageSV gnOperRoleManageSV = DubboConsumerFactory.getService("gnOperRoleManageSV");
			PageInfo<GnOperRoleVO> results = gnOperRoleManageSV.queryRoleByOperId(gnOperRoleParamsVO);

			PagerResult<GnOperRoleVO> pagerResult = new PagerResult<GnOperRoleVO>();
			Pager pager = new Pager(currentPage, results != null ? results.getCount() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			pagerResult.setResult(results != null ? results.getResult() : null);

			responseData = new ResponseData<PagerResult<GnOperRoleVO>>(ResponseData.AJAX_STATUS_SUCCESS, "角色列表查询成功", pagerResult);
		} catch (CallerException e) {
			LOGGER.error("角色列表查询失败，错误信息：", e);
			responseData = new ResponseData<PagerResult<GnOperRoleVO>>(ResponseData.AJAX_STATUS_FAILURE, "角色列表查询失败", null);
		}
		return responseData;
	}

	/**
	 * 
	 * @param request
	 * @param gnOperRoleParamsVO
	 * @param gnRoleParamsVO
	 * @param currentPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryCanAddRoleList")
	public ResponseData<PagerResult<GnRoleVO>> queryCanAddRoleList(HttpServletRequest request, GnOperRoleParamsVO gnOperRoleParamsVO,
			GnRoleParamsVO gnRoleParamsVO, @RequestParam("currentPage") Integer currentPage) {

		ResponseData<PagerResult<GnRoleVO>> responseData = null;
		try {
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
			gnOperRoleParamsVO.setTenantId(user.getTenantId());
			// 设置生效失效过滤条件 只查有效数据
			//gnOperRoleParamsVO.setActiveTime(currTimestamp);
			gnOperRoleParamsVO.setInactiveTime(currTimestamp);
			// 操作员角色信息查询服务 查询该操作员拥有的角色
			IGnOperRoleManageSV operRoleManageSV = DubboConsumerFactory.getService("gnOperRoleManageSV");
			List<GnOperRoleVO> operRoleList = operRoleManageSV.queryOperRoleListByOperId(gnOperRoleParamsVO);
			//设置过滤的角色id
			if(operRoleList!=null && operRoleList.size()>0){
				List<Long> roleIdList = new LinkedList<Long>();
				for(GnOperRoleVO operRoleVO : operRoleList){
					Long roleId = operRoleVO.getRoleId();
					if(roleId != null){
						roleIdList.add(roleId);
					}
				}
				gnRoleParamsVO.setFilterRoleIdList(roleIdList);
			}
			// 角色过滤条件
			PageInfo<GnRoleVO> pageInfo = new PageInfo<GnRoleVO>();
			pageInfo.setPageNo(currentPage);
			pageInfo.setPageSize(PAGESIZE);
			gnRoleParamsVO.setPageInfo(pageInfo); // 分页信息
			gnRoleParamsVO.setTenantId(user.getTenantId()); // 租户ID
			//gnRoleParamsVO.setSystemId(Constants.SYSTEM_ID); // 系统ID
			// 只查有效数据
			gnRoleParamsVO.setInactiveTime(currTimestamp);
			//gnRoleParamsVO.setActiveTime(currTimestamp);

			// 角色信息查询服务
			IGnRoleManageSV roleManageSV = DubboConsumerFactory.getService("gnRoleManageSV");
			PageInfo<GnRoleVO> results = roleManageSV.queryRoleFilterIds(gnRoleParamsVO);

			PagerResult<GnRoleVO> pagerResult = new PagerResult<GnRoleVO>();
			Pager pager = new Pager(currentPage, results != null ? results.getCount() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			pagerResult.setResult(results != null ? results.getResult() : null);

			responseData = new ResponseData<PagerResult<GnRoleVO>>(ResponseData.AJAX_STATUS_SUCCESS, "角色列表查询成功", pagerResult);
		} catch (CallerException e) {
			LOGGER.error("角色列表查询失败，错误信息：", e);
			responseData = new ResponseData<PagerResult<GnRoleVO>>(ResponseData.AJAX_STATUS_FAILURE, "角色列表查询失败", null);
		}
		return responseData;
	}

	/**
	 * 删除操作眼角色关系
	 * @param request
	 * @param gnOperRoleParamsVO
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("delOperRole")
	public ResponseData<String> delOperRole(HttpServletRequest request, GnOperRoleParamsVO gnOperRoleParamsVO) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

		ResponseData<String> responseData = null;
		try {
			String tenantId = user.getTenantId();
			gnOperRoleParamsVO.setTenantId(tenantId);
			gnOperRoleParamsVO.setUpdateTime(DateTimeUtil.getCurrTimestamp());
			gnOperRoleParamsVO.setUpdateOperId(user.getOperId());

			IGnOperRoleManageSV operRoleSV = DubboConsumerFactory.getService("gnOperRoleManageSV");
			GnOperRoleResponseVO gnOperRoleResponseVO = operRoleSV.deleteOperRole(gnOperRoleParamsVO);
			if (gnOperRoleResponseVO != null && ResponseData.AJAX_STATUS_SUCCESS.equals(gnOperRoleResponseVO.responseCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "操作员角色关系删除成功", gnOperRoleResponseVO.respondeDesc);
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员角色关系删除失败", gnOperRoleResponseVO == null?"操作员角色关系删除失败":gnOperRoleResponseVO.respondeDesc);
			}
		} catch (CallerException e) {
			LOGGER.error("操作员角色关系删除失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员角色关系删除失败", null);
		}
		return responseData;
	}

}
