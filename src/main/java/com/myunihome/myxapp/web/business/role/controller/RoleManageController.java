package com.myunihome.myxapp.web.business.role.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.sys.api.sysrole.interfaces.IGnRoleManageSV;
import com.myunihome.myxapp.sys.api.sysrole.param.GnRoleParamsVO;
import com.myunihome.myxapp.sys.api.sysrole.param.GnRoleResponseVO;
import com.myunihome.myxapp.sys.api.sysrole.param.GnRoleVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.system.constants.Constants;
import com.myunihome.myxapp.web.system.util.DateTimeUtil;

/**
 * 角色管理 Date: 2015年9月24日 <br>
 * Copyright (c) 2015 asiainfo.com <br>
 * 
 * @author jiaxs
 *
 */
@Controller
@RequestMapping(value = "/roleManage")
public class RoleManageController {
	private static final Logger LOGGER = Logger.getLogger(RoleManageController.class);

	/**
	 * 分页大小
	 */
	private static final int PAGESIZE = 5;

	/**
	 * 跳转页面－角色管理
	 */
	@RequestMapping(value = "/toRoleList")
	public ModelAndView toMenuList(HttpServletRequest request, HttpServletResponse response) {

		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		request.setAttribute("tenantId", user.getTenantId());// 租户ID

		return new ModelAndView("role/roleList");
	}

	/**
	 * 跳转页面－权限管理（角色赋菜单权限）
	 */
	@RequestMapping(value = "/toRoleMenuList")
	public ModelAndView toMenuMenuList(HttpServletRequest request, HttpServletResponse response) {

		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		request.setAttribute("tenantId", user.getTenantId());// 租户ID

		return new ModelAndView("rolemenu/roleList");
	}

	@ResponseBody
	@RequestMapping("/queryRoleList")
	public ResponseData<PagerResult<GnRoleVO>> queryOrderAuditList(HttpServletRequest request, GnRoleParamsVO gnRoleParamsVO,
			@RequestParam("currentPage") Integer currentPage) {

		ResponseData<PagerResult<GnRoleVO>> responseData = null;
		try {

			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			gnRoleParamsVO.setTenantId(user.getTenantId()); // 租户ID
			// gnRoleParamsVO.setSystemId(Constants.SYSTEM_ID); // 系统ID
			Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
			//gnRoleParamsVO.setActiveTime(currTimestamp);
			gnRoleParamsVO.setInactiveTime(currTimestamp);

			PageInfo<GnRoleVO> pageInfo = new PageInfo<GnRoleVO>();
			pageInfo.setPageNo(currentPage);
			pageInfo.setPageSize(PAGESIZE);
			gnRoleParamsVO.setPageInfo(pageInfo); // 分页信息

			// 角色信息查询服务
			IGnRoleManageSV gnRoleManageSV = DubboConsumerFactory.getService("gnRoleManageSV");
			PageInfo<GnRoleVO> results = gnRoleManageSV.queryRole(gnRoleParamsVO);

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
	 * 获取角色信息
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/getRoleInfo", method = RequestMethod.POST)
	public ResponseData<GnRoleVO> getRoleInfo(HttpServletRequest request, GnRoleParamsVO gnRoleParamsVO) {

		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<GnRoleVO> responseData = null;
		try {
			String tenantId = user.getTenantId();
			Long roleId = gnRoleParamsVO.getRoleId();
			GnRoleVO queryRoleInfo = new GnRoleVO();
			if (roleId != null) {
				GnRoleParamsVO queryBaseInfo = new GnRoleParamsVO();
				// queryBaseInfo.setSystemId(Constants.SYSTEM_ID);
				queryBaseInfo.setRoleId(roleId);
				queryBaseInfo.setTenantId(tenantId);

				// 角色查询服务
				IGnRoleManageSV roleQuerySV = DubboConsumerFactory.getService("gnRoleManageSV");
				queryRoleInfo = roleQuerySV.queryRoleById(queryBaseInfo);
			}
			responseData = new ResponseData<GnRoleVO>(ResponseData.AJAX_STATUS_SUCCESS, "获取角色信息成功", queryRoleInfo);
		} catch (CallerException e) {
			LOGGER.error("获取角色信息失败！错误信息：", e);
			responseData = new ResponseData<GnRoleVO>(ResponseData.AJAX_STATUS_FAILURE, "获取角色信息失败", null);
		}
		return responseData;
	}

	/**
	 * 新增或更新角色信息
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrUpDateRoleInfo", method = RequestMethod.POST)
	public ResponseData<String> addOrUpDateRoleInfo(HttpServletRequest request, GnRoleParamsVO gnRoleParamsVO,
			@RequestParam("activeTimeStr") String activeTimeStr, @RequestParam("inactiveTimeStr") String inactiveTimeStr) {
		Long roleid = gnRoleParamsVO.getRoleId();
		gnRoleParamsVO.setSystemId(Constants.SYSTEM_ID);
		gnRoleParamsVO.setSystemModeId(Constants.SYSTEM_MODEL_ID);
		if (roleid == null) {
			return addRoleInfo(request, gnRoleParamsVO, activeTimeStr, inactiveTimeStr);
		} else {
			return updateRoleInfo(request, gnRoleParamsVO, activeTimeStr, inactiveTimeStr);
		}
	}

	/**
	 * 新增角色信息
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/addRoleInfo", method = RequestMethod.POST)
	public ResponseData<String> addRoleInfo(HttpServletRequest request, GnRoleParamsVO gnRoleParamsVO, @RequestParam("activeTimeStr") String activeTimeStr,
			@RequestParam("inactiveTimeStr") String inactiveTimeStr) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<String> responseData = null;
		try {
			// 添加默认值
			setRoleDefValues(activeTimeStr, inactiveTimeStr, user, gnRoleParamsVO, 1);

			// 角色管理服务
			IGnRoleManageSV gnRoleManageSV = DubboConsumerFactory.getService("gnRoleManageSV");
			Long roleId = gnRoleManageSV.addRole(gnRoleParamsVO);
			if (roleId != null) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "角色创建成功", roleId.toString());
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "角色创建失败", null);
			}
		} catch (Exception e) {
			LOGGER.error("添加角色信息失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "角色创建失败", null);
		}
		return responseData;
	}

	/**
	 * 添加默认值
	 * 
	 * @param activeTimeStr
	 * @param inactiveTimeStr
	 * @param user
	 * @param addBaseInfo
	 * @param mold
	 *            :1添加，2：修改
	 * @throws Exception
	 */
	private void setRoleDefValues(String activeTimeStr, String inactiveTimeStr, UserLoginVo user, GnRoleParamsVO gnRoleParamsVO, int mold) throws Exception {
		String tenantId = user.getTenantId();// 租户
		gnRoleParamsVO.setTenantId(tenantId);
		long operId = user.getOperId();// 操作人id
		if ((mold & 1) == 1) {// 添加
			gnRoleParamsVO.setCreateOperId(operId);
			gnRoleParamsVO.setCreateType(Constants.CREAT_TYPE_SELF);
		} else if ((mold & 2) == 2)// 修改
		{
			gnRoleParamsVO.setUpdateOperId(operId);
		}
		gnRoleParamsVO.setActiveTime(DateTimeUtil.stringToTimstamp(activeTimeStr));
		gnRoleParamsVO.setInactiveTime(DateTimeUtil.stringToTimstamp(inactiveTimeStr));
	}

	/**
	 * 更新角色信息
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/updateRoleInfo", method = RequestMethod.POST)
	public ResponseData<String> updateRoleInfo(HttpServletRequest request, GnRoleParamsVO gnRoleParamsVO, @RequestParam("activeTimeStr") String activeTimeStr,
			@RequestParam("inactiveTimeStr") String inactiveTimeStr) {

		ResponseData<String> responseData = null;
		GnRoleResponseVO gnRoleResponseVO = new GnRoleResponseVO();
		try {
			// 角色管理服务
			IGnRoleManageSV gnRoleManageSV = DubboConsumerFactory.getService("gnRoleManageSV");
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			// 添加默认值
			setRoleDefValues(activeTimeStr, inactiveTimeStr, user, gnRoleParamsVO, 2);
			gnRoleResponseVO = gnRoleManageSV.updateRole(gnRoleParamsVO);
			if (ResponseData.AJAX_STATUS_SUCCESS.equals(gnRoleResponseVO.responseCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "角色更新成功", gnRoleResponseVO.respondeDesc);
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "角色更新失败", gnRoleResponseVO.respondeDesc);
			}
		} catch (Exception e) {
			LOGGER.error("更新角色信息失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "角色更新失败", gnRoleResponseVO.respondeDesc);
		}
		return responseData;
	}

	/**
	 * 删除角色信息
	 * 
	 * @param request
	 * @param gnRoleParamsVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delRoleInfo")
	public ResponseData<String> delRoleInfo(HttpServletRequest request, GnRoleParamsVO gnRoleParamsVO) {

		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

		ResponseData<String> responseData = null;
		try {
			String tenantId = user.getTenantId();
			gnRoleParamsVO.setTenantId(tenantId);
			gnRoleParamsVO.setUpdateOperId(user.getOperId());

			// 角色管理服务
			IGnRoleManageSV roleQuerySV = DubboConsumerFactory.getService("gnRoleManageSV");
			GnRoleResponseVO gnRoleResponseVO = roleQuerySV.delRoleByID(gnRoleParamsVO);
			if (gnRoleResponseVO != null && ResponseData.AJAX_STATUS_SUCCESS.equals(gnRoleResponseVO.responseCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "删除角色信息成功", gnRoleResponseVO.respondeDesc);
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "删除角色信息失败", gnRoleResponseVO == null?"删除角色信息失败":gnRoleResponseVO.respondeDesc);
			}
		} catch (CallerException e) {
			LOGGER.error("删除角色信息失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "删除角色信息失败", null);
		}
		return responseData;
	}
}
