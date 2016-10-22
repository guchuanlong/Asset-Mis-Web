package com.myunihome.myxapp.web.business.common.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.common.api.area.interfaces.IGnAreaQuerySV;
import com.myunihome.myxapp.common.api.area.param.GnAreaVo;
import com.myunihome.myxapp.common.api.depart.interfaces.IGnDepartQuerySV;
import com.myunihome.myxapp.common.api.depart.param.GnDepartCondition;
import com.myunihome.myxapp.common.api.depart.param.GnDepartVo;
import com.myunihome.myxapp.common.api.staff.interfaces.IGnStaffQuerySV;
import com.myunihome.myxapp.common.api.staff.param.GnStaffCondition;
import com.myunihome.myxapp.common.api.staff.param.GnStaffVo;
import com.myunihome.myxapp.common.api.staff.param.StaffPaginationResult;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.sso.api.system.interfaces.ITabSystemSV;
import com.myunihome.myxapp.sso.api.system.mapper.TabSystemQueryVO;
import com.myunihome.myxapp.sso.api.system.mapper.TabSystemVO;
import com.myunihome.myxapp.sso.api.systemmode.interfaces.ISystemModeSV;
import com.myunihome.myxapp.sso.api.systemmode.mapper.SystemModeListQueryVO;
import com.myunihome.myxapp.sso.api.systemmode.mapper.SystemModeVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.system.constants.Constants;

@Controller
@RequestMapping(value = "/commonManage")
public class CommonManageController {

	private static final Logger LOGGER = Logger.getLogger(CommonManageController.class);
	/** 一页行数 **/
	private static final Integer PAGESIZE = 5;

	private static final String PROVICE_LEVEL = "1"; // 省
	private static final String CITY_LEVEL = "2"; // 市
	private static final String COUNTY_LEVEL = "3"; // 县区
	private static final String STREET_LEVEL = "4"; // 街道
	private static final String AREA_LEVEL = "5"; // 详细地址

	/**
	 * 部门树结构展示
	 * 
	 * @return
	 */
	@RequestMapping("/departtree")
	public ModelAndView tree_page() {
		return new ModelAndView("commons/depart_tree");
	}

	/**
	 * 获取部门信息
	 * 
	 * @param departId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDepartList", method = RequestMethod.POST)
	public List<GnDepartVo> listSubNode(@RequestParam(value = "departId", required = false) String departId, HttpSession session) {
		GnDepartCondition condition = new GnDepartCondition();
		UserLoginVo user = (UserLoginVo) session.getAttribute(Constants.SESSION_USER_KEY);
		condition.setTenantId(user.getTenantId());
		if (StringUtils.isEmpty(departId)) {
			condition.setDepartId("0");
		} else {
			condition.setDepartId(departId);
		}
		IGnDepartQuerySV gnDepartQuerySV = DubboConsumerFactory.getService("gnDepartQuerySV");
		return gnDepartQuerySV.select(condition);
	}

	@RequestMapping("/getArea")
	public void getArea(HttpServletRequest request, HttpServletResponse response) {
		try {
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			String areaLevel = request.getParameter("areaLevel");
			String parentAreaCode = request.getParameter("parentAreaCode");
			List<GnAreaVo> areaList = new ArrayList<GnAreaVo>();
			IGnAreaQuerySV gnAreaQuerySV = DubboConsumerFactory.getService("getGnAreaQuerySV");
			if (PROVICE_LEVEL.equals(areaLevel)) {
				areaList = gnAreaQuerySV.getProvinceList();
				areaList = addDefSelect(areaList, "00", "全国");
			} else if (CITY_LEVEL.equals(areaLevel) && StringUtils.isNoneEmpty(parentAreaCode)) {
				areaList = gnAreaQuerySV.getCityListByProviceCode(parentAreaCode);
				if (isAddDefSelect(parentAreaCode)) {
					areaList = addDefSelect(areaList, "000", "全国");
				}
			} else if (COUNTY_LEVEL.equals(areaLevel) && StringUtils.isNoneEmpty(parentAreaCode)) {
				areaList = gnAreaQuerySV.getCountyListByCityCode(parentAreaCode);
				if (isAddDefSelect(parentAreaCode)) {
					areaList = addDefSelect(areaList, "000", "全国");
				}
			} else if (STREET_LEVEL.equals(areaLevel) && StringUtils.isNoneEmpty(parentAreaCode)) {
				areaList = gnAreaQuerySV.getStreetListByCountyCode(parentAreaCode);
				if (isAddDefSelect(parentAreaCode)) {
					areaList = addDefSelect(areaList, "000", "全国");
				}
			} else if (AREA_LEVEL.equals(areaLevel) && StringUtils.isNoneEmpty(parentAreaCode)) {
				areaList = gnAreaQuerySV.getAreaListByStreetCode(user.getTenantId(), parentAreaCode);
				if (isAddDefSelect(parentAreaCode)) {
					areaList = addDefSelect(areaList, "000", "全国");
				}
			}
			net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(areaList);
			LOGGER.info(jsonArray.toString());
			response.getWriter().print(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			LOGGER.error("出现异常，信息：", e);
		}
	}

	/**
	 * 是否添加默认选项
	 * 
	 * @param parentAreaCode
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private boolean isAddDefSelect(String parentAreaCode) {
		return "000".equals(parentAreaCode) || "00".equals(parentAreaCode);
	}

	/**
	 * 添加默认选项
	 * 
	 * @param areaList
	 * @param areaCode
	 * @param areaName
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private List<GnAreaVo> addDefSelect(List<GnAreaVo> areaList, String areaCode, String areaName) {
		List<GnAreaVo> rasultAreaList = new LinkedList<GnAreaVo>();
		GnAreaVo arg0 = new GnAreaVo();
		arg0.setAreaCode(areaCode);
		arg0.setAreaName(areaName);
		rasultAreaList.add(arg0);
		if (areaList != null && areaList.size() > 0) {
			rasultAreaList.addAll(areaList);
		}
		return rasultAreaList;
	}

	/**
	 * 查询员工列表
	 * 
	 * @param request
	 * @param currentPage
	 * @param gnStaffCondition
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryStaffList")
	public ResponseData<PagerResult<GnStaffVo>> queryStaffList(HttpServletRequest request, @RequestParam("currentPage") Integer currentPage,
			GnStaffCondition gnStaffCondition) {

		ResponseData<PagerResult<GnStaffVo>> responseData = null;
		try {

			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			gnStaffCondition.setTenantId(user.getTenantId());// 租户ID

			int beginIndex = currentPage * PAGESIZE - PAGESIZE;

			IGnStaffQuerySV gnStaffQuerySV = DubboConsumerFactory.getService("gnStaffQuerySV");
			StaffPaginationResult results = gnStaffQuerySV.selectByPage(gnStaffCondition, beginIndex, PAGESIZE);

			PagerResult<GnStaffVo> pagerResult = new PagerResult<GnStaffVo>();
			Pager pager = new Pager(currentPage, results != null ? results.getTotal() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			pagerResult.setResult(results != null ? results.getResult() : null);

			responseData = new ResponseData<PagerResult<GnStaffVo>>(ResponseData.AJAX_STATUS_SUCCESS, "工号列表查询成功", pagerResult);
		} catch (Exception e) {
			LOGGER.error("员工工号列表查询失败，错误信息：", e);
			responseData = new ResponseData<PagerResult<GnStaffVo>>(ResponseData.AJAX_STATUS_FAILURE, "工号列表查询失败", null);
		}
		return responseData;
	}

	/**
	 * 获取系统标识列表
	 * 
	 * @param request
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/querySystemList")
	public List<TabSystemVO> getSystemList(HttpServletRequest request) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		List<TabSystemVO> queryTabSystemList = new ArrayList<TabSystemVO>();
		try {
			ITabSystemSV tabSystemSV = DubboConsumerFactory.getService("tabSystemSV");
			TabSystemQueryVO systemQueryVo = new TabSystemQueryVO();
			systemQueryVo.setTenantId(user.getTenantId());
			queryTabSystemList = tabSystemSV.queryTabSystemList(systemQueryVo);
		} catch (CallerException e) {
			LOGGER.error("获取系统模块信息失败，错误信息：", e);
		}
		return queryTabSystemList;
	}

	/**
	 * 获取频道列表
	 * 
	 * @param request
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/querySystemModeList")
	public List<SystemModeVO> querySystemModeList(HttpServletRequest request) {
		List<SystemModeVO> systemModeList = new ArrayList<SystemModeVO>();
		try {
			ISystemModeSV systemModeSV = DubboConsumerFactory.getService("systemModeSV");
			SystemModeListQueryVO systemModeListQueryVO = new SystemModeListQueryVO();
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			systemModeListQueryVO.setTenantId(user.getTenantId());
			systemModeList = systemModeSV.querySystemModeList(systemModeListQueryVO);
		} catch (CallerException e) {
			LOGGER.error("获取频道信息失败，错误信息：", e);
		}
		return systemModeList;
	}
}
