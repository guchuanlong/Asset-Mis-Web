package com.myunihome.myxapp.web.business.rolemenu.controller;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.sys.api.sysmenu.interfaces.IGnMenuManageSV;
import com.myunihome.myxapp.sys.api.sysmenu.param.GnMenuParamsVO;
import com.myunihome.myxapp.sys.api.sysmenu.param.GnMenuVO;
import com.myunihome.myxapp.sys.api.sysrolemenu.interfaces.IGnRoleMenuManageSV;
import com.myunihome.myxapp.sys.api.sysrolemenu.param.GnRoleMenuParamsVO;
import com.myunihome.myxapp.sys.api.sysrolemenu.param.GnRoleMenuResponseVO;
import com.myunihome.myxapp.sys.api.sysrolemenu.param.GnRoleMenuVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.system.constants.Constants;
import com.myunihome.myxapp.web.system.util.DateTimeUtil;

@Controller
@RequestMapping(value = "/roleMenuManage")
public class RoleMenuManageController {
	private static final Logger LOGGER = Logger.getLogger(RoleMenuManageController.class);

	private static final int PAGESIZE = 5;// 单页大小

	/**
	 * 根据roleId获取菜单列表
	 * 
	 * @param request
	 * @param gnRoleParamsVO
	 * @param currentPage
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/queryRoleMenuList")
	public ResponseData<PagerResult<GnRoleMenuVO>> queryRoleMenuList(HttpServletRequest request, GnRoleMenuParamsVO gnRoleMenuParamsVO,
			@RequestParam("currentPage") Integer currentPage) {

		ResponseData<PagerResult<GnRoleMenuVO>> responseData = null;
		try {

			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			gnRoleMenuParamsVO.setTenantId(user.getTenantId()); // 租户ID

			PageInfo<GnRoleMenuVO> pageInfo = new PageInfo<GnRoleMenuVO>();
			pageInfo.setPageNo(currentPage);
			pageInfo.setPageSize(PAGESIZE);
			gnRoleMenuParamsVO.setPageInfo(pageInfo); // 分页信息
			// 只查生效数据
			Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
			//gnRoleMenuParamsVO.setActiveTime(currTimestamp);
			gnRoleMenuParamsVO.setInactiveTime(currTimestamp);

			IGnRoleMenuManageSV gnRoleMenuManageSV = DubboConsumerFactory.getService("gnRoleMenuManageSV");
			PageInfo<GnRoleMenuVO> results = gnRoleMenuManageSV.queryMenuByRole(gnRoleMenuParamsVO);

			PagerResult<GnRoleMenuVO> pagerResult = new PagerResult<GnRoleMenuVO>();
			Pager pager = new Pager(currentPage, results != null ? results.getCount() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			pagerResult.setResult(results != null ? results.getResult() : null);

			responseData = new ResponseData<PagerResult<GnRoleMenuVO>>(ResponseData.AJAX_STATUS_SUCCESS, "角色菜单列表查询成功", pagerResult);
		} catch (CallerException e) {
			responseData = new ResponseData<PagerResult<GnRoleMenuVO>>(ResponseData.AJAX_STATUS_FAILURE, "角色菜单列表查询失败", null);
			LOGGER.error("角色列表查询失败，错误信息：", e);
		}
		return responseData;
	}

	/**
	 * 查询角色可赋予的菜单列表
	 * 
	 * @param request
	 * @param gnRoleMenuParamsVO
	 * @param currentPage
	 *            请求页码
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/queryCanAddMeunList")
	public ResponseData<PagerResult<GnMenuVO>> queryCanAddMeunList(HttpServletRequest request, GnRoleMenuParamsVO gnRoleMenuParamsVO,
			GnMenuParamsVO menuParamsVO, @RequestParam("currentPage") Integer currentPage, @RequestParam("systemModeId") String systemModeId) {

		ResponseData<PagerResult<GnMenuVO>> responseData = null;
		try {
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
			gnRoleMenuParamsVO.setTenantId(user.getTenantId()); // 租户ID
			// 只查有效数据
			//gnRoleMenuParamsVO.setActiveTime(currTimestamp);
			gnRoleMenuParamsVO.setInactiveTime(currTimestamp);
			// 获得角色已有的角色菜单关系
			IGnRoleMenuManageSV gnRoleMenuManageSV = DubboConsumerFactory.getService("gnRoleMenuManageSV");
			List<GnRoleMenuVO> roleMenuList = gnRoleMenuManageSV.queryRoleMenuListByRoleId(gnRoleMenuParamsVO);
			// 设置过滤已存在的菜单过滤条件
			if (roleMenuList != null && roleMenuList.size() > 0) {
				List<Long> menuIdList = new LinkedList<Long>();
				for (GnRoleMenuVO roleMenuVO : roleMenuList) {
					Long menuId = roleMenuVO.getMenuId();
					if (menuId != null) {
						menuIdList.add(menuId);
					}
				}
				menuParamsVO.setFilterMenuIdList(menuIdList);
			}
			// 菜单过滤条件
			PageInfo<GnMenuVO> pageInfo = new PageInfo<GnMenuVO>();
			pageInfo.setPageNo(currentPage);
			pageInfo.setPageSize(PAGESIZE);
			menuParamsVO.setPageInfo(pageInfo);
			menuParamsVO.setTenantId(user.getTenantId());
			if (systemModeId != null && !"".equals(systemModeId) && !"undefined".equals(systemModeId)) {
				menuParamsVO.setSystemModeId(systemModeId);
			}
			// 只查有效数据
			menuParamsVO.setInactiveTime(currTimestamp);
			//menuParamsVO.setActiveTime(currTimestamp);
			// 查询可赋予角色的菜单列表
			IGnMenuManageSV gnMenuManageSV = DubboConsumerFactory.getService("gnMenuManageSV");
			PageInfo<GnMenuVO> results = gnMenuManageSV.queryMenuFilterIds(menuParamsVO);

			PagerResult<GnMenuVO> pagerResult = new PagerResult<GnMenuVO>();
			Pager pager = new Pager(currentPage, results != null ? results.getCount() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			pagerResult.setResult(results != null ? results.getResult() : null);

			responseData = new ResponseData<PagerResult<GnMenuVO>>(ResponseData.AJAX_STATUS_SUCCESS, "菜单列表查询成功", pagerResult);
		} catch (CallerException e) {
			LOGGER.error("菜单列表查询失败，错误信息：", e);
			responseData = new ResponseData<PagerResult<GnMenuVO>>(ResponseData.AJAX_STATUS_FAILURE, "菜单列表查询失败", null);
		}
		return responseData;
	}

	/**
	 * 删除角色菜单关系
	 * 
	 * @param request
	 * @param gnRoleMenuParamsVO
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/delRoleMenu")
	public ResponseData<String> delRoleMenu(HttpServletRequest request, GnRoleMenuParamsVO gnRoleMenuParamsVO) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

		ResponseData<String> responseData = null;
		try {
			String tenantId = user.getTenantId();
			gnRoleMenuParamsVO.setTenantId(tenantId);
			gnRoleMenuParamsVO.setUpdateTime(DateTimeUtil.getCurrTimestamp());
			gnRoleMenuParamsVO.setUpdateOperId(user.getOperId());

			IGnRoleMenuManageSV roleMenuSV = DubboConsumerFactory.getService("gnRoleMenuManageSV");
			GnRoleMenuResponseVO gnRoleMenuResponseVO = roleMenuSV.deleteRoleMenu(gnRoleMenuParamsVO);
			if (gnRoleMenuResponseVO != null && ResponseData.AJAX_STATUS_SUCCESS.equals(gnRoleMenuResponseVO.responseCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "角色菜单关系删除成功", gnRoleMenuResponseVO.respondeDesc);
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "角色菜单关系删除失败", gnRoleMenuResponseVO == null?"角色菜单关系删除失败":gnRoleMenuResponseVO.respondeDesc);
			}
		} catch (CallerException e) {
			LOGGER.error("角色菜单关系删除失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "角色菜单关系删除失败", null);
		}
		return responseData;
	}

	/**
	 * 角色赋菜单权限
	 * 
	 * @param request
	 * @param roleMenuParamsList
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/addMenuToRole")
	public ResponseData<PagerResult<String>> addMenuToRole(HttpServletRequest request, @RequestParam("roleMenuParamsList") String roleMenuParamsList) {

		ResponseData<PagerResult<String>> responseData = null;
		try {

			Type listType = new TypeToken<LinkedList<GnRoleMenuParamsVO>>() {
			}.getType();
			roleMenuParamsList = StringEscapeUtils.unescapeHtml4(roleMenuParamsList);
			LinkedList<GnRoleMenuParamsVO> roleMenuParamsListByGson = new Gson().fromJson(roleMenuParamsList, listType);
			if (roleMenuParamsListByGson != null && roleMenuParamsListByGson.size() > 0) {

				int failureCount = 0;
				String statusCode = ResponseData.AJAX_STATUS_SUCCESS;

				UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
				IGnRoleMenuManageSV gnRoleMenuManageSV = DubboConsumerFactory.getService("gnRoleMenuManageSV");

				for (GnRoleMenuParamsVO gnRoleMenuParamsVO : roleMenuParamsListByGson) {

					gnRoleMenuParamsVO.setTenantId(user.getTenantId());// 租户ID
					gnRoleMenuParamsVO.setCreateOperId(user.getOperId());// 创建操作员
					// 生效时间
					Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
					String timstampStr = DateTimeUtil.timstamp3String(currTimestamp);
					gnRoleMenuParamsVO.setActiveTime(DateTimeUtil.stringToTimstamp(timstampStr));

					gnRoleMenuParamsVO.setInactiveTime(DateTimeUtil.stringToTimstamp("2099-12-31"));// 失效时间

					Long roleMenuId = gnRoleMenuManageSV.addMenuToRole(gnRoleMenuParamsVO);
					if (roleMenuId == null) {
						failureCount++;
						statusCode = ResponseData.AJAX_STATUS_FAILURE;
					}
				}

				if (statusCode.equals(ResponseData.AJAX_STATUS_FAILURE)) {
					responseData = new ResponseData<PagerResult<String>>(ResponseData.AJAX_STATUS_FAILURE, "角色赋菜单权限完成，其中有" + failureCount + "条失败！", null);
				}
			}
			responseData = new ResponseData<PagerResult<String>>(ResponseData.AJAX_STATUS_SUCCESS, "角色赋菜单权限成功", null);
		} catch (Exception e) {
			responseData = new ResponseData<PagerResult<String>>(ResponseData.AJAX_STATUS_FAILURE, "角色赋菜单权限失败", null);
			LOGGER.error("角色赋菜单权限失败，错误信息：", e);
		}
		return responseData;
	}
}
