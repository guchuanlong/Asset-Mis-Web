package com.myunihome.myxapp.web.business.operarea.controller;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.common.api.area.interfaces.IGnAreaQuerySV;
import com.myunihome.myxapp.common.api.area.param.GnAreaCodeCondition;
import com.myunihome.myxapp.common.api.area.param.GnAreaPageFilterCondition;
import com.myunihome.myxapp.common.api.area.param.GnAreaVo;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.util.BeanUtils;
import com.myunihome.myxapp.paas.util.DateUtil;
import com.myunihome.myxapp.sys.api.sysoperarea.interfaces.IGnOperAreaManageSV;
import com.myunihome.myxapp.sys.api.sysoperarea.param.GnOperAreaParamsVO;
import com.myunihome.myxapp.sys.api.sysoperarea.param.GnOperAreaResponseVO;
import com.myunihome.myxapp.sys.api.sysoperarea.param.GnOperAreaVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.business.operarea.model.GnAreaInfo;
import com.myunihome.myxapp.web.business.operarea.model.GnOperAreaInfo;
import com.myunihome.myxapp.web.system.constants.Constants;
import com.myunihome.myxapp.web.system.util.DateTimeUtil;

/**
 * 操作员区域管理 Date: 2015年11月27日 <br>
 * Copyright (c) 2015 asiainfo.com <br>
 * 
 * @author jiaxs
 */
@Controller
@RequestMapping(value = "/operAreaManage")
public class OperAreaManageController {

	private static final Logger LOGGER = Logger.getLogger(OperAreaManageController.class);

	/**
	 * 分页大小
	 */
	private static final int PAGESIZE = 5;

	/**
	 * 跳转页面－操作员区域权限管理
	 */
	@RequestMapping(value = "/toOperAreaManagePage")
	public ModelAndView toOperAreaManagePage(HttpServletRequest request, HttpServletResponse response) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		request.setAttribute("tenantId", user.getTenantId());// 租户ID
		return new ModelAndView("operarea/operList");
	}

	@ResponseBody
	@RequestMapping(value = "/queryCanAddAreaList", method = RequestMethod.POST)
	public ResponseData<PagerResult<GnAreaInfo>> queryCanAddAreaList(HttpServletRequest request, GnOperAreaParamsVO gnOperAreaParamsVO,
			GnAreaPageFilterCondition gnAreaPageParamsVO, @RequestParam("currentPage") Integer currentPage) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<PagerResult<GnAreaInfo>> responseData = null;
		try {
			// 获得操作员列表信息
			String tenantId = user.getTenantId();
			gnAreaPageParamsVO.setTenantId(tenantId);
			gnAreaPageParamsVO.setPageNo(currentPage);
			gnAreaPageParamsVO.setPageSize(PAGESIZE);
			// 获得已存在员工区域关系中的过滤条件
			List<String> filterAreaCodeList = getFilterAreaCodeList(gnOperAreaParamsVO, tenantId);
			if (filterAreaCodeList != null && filterAreaCodeList.size() > 0) {
				gnAreaPageParamsVO.setFilterAreaCodeList(filterAreaCodeList);
			}
			// 获取可以赋予操作员 区域的列表
			IGnAreaQuerySV gnAreaQuerySV = DubboConsumerFactory.getService("getGnAreaQuerySV");
			PageInfo<GnAreaVo> operPageInfo = gnAreaQuerySV.getFilterAreaListByPage(gnAreaPageParamsVO);
			PagerResult<GnAreaInfo> pagerResult = new PagerResult<GnAreaInfo>();
			Pager pager = new Pager(currentPage, operPageInfo != null ? operPageInfo.getCount() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			if (operPageInfo != null) {
				List<GnAreaVo> result = operPageInfo.getResult();
				List<GnAreaInfo> areaInfoList = getAreaInfoList(result, tenantId);
				pagerResult.setResult(areaInfoList);
			}
			responseData = new ResponseData<PagerResult<GnAreaInfo>>(ResponseData.AJAX_STATUS_SUCCESS, "获取操作员信息成功！", pagerResult);
		} catch (CallerException e) {
			LOGGER.error("获取操作员区域信息失败！错误信息：", e);
			responseData = new ResponseData<PagerResult<GnAreaInfo>>(ResponseData.AJAX_STATUS_FAILURE, "获取操作员信息失败！", null);
		}
		return responseData;
	}

	/**
	 * 获取操作员已有的区域集
	 * 
	 * @param gnOperAreaParamsVO
	 * @param tenantId
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private List<String> getFilterAreaCodeList(GnOperAreaParamsVO gnOperAreaParamsVO, String tenantId) {
		List<String> areaCodeList = new LinkedList<String>();
		gnOperAreaParamsVO.setTenantId(tenantId);
		gnOperAreaParamsVO.setInactiveTime(DateUtil.getSysDate());
		IGnOperAreaManageSV operAreaService = DubboConsumerFactory.getService("gnOperAreaManageSV");
		List<GnOperAreaVO> operAreaList = operAreaService.queryAreaListByOperId(gnOperAreaParamsVO);
		if (operAreaList != null && operAreaList.size() > 0) {
			for (GnOperAreaVO operArea : operAreaList) {
				if (operArea != null) {
					areaCodeList.add(operArea.getAreaCode());
				}
			}
		}
		return areaCodeList;
	}

	/**
	 * 转换为页面展现对象
	 * 
	 * @param result
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private List<GnAreaInfo> getAreaInfoList(List<GnAreaVo> areaVoList, String tenantId) {
		List<GnAreaInfo> areaInfoList = null;
		if (areaVoList != null && areaVoList.size() > 0) {
			ICacheSV cacheSV = DubboConsumerFactory.getService("getCacheSV");
			areaInfoList = new LinkedList<GnAreaInfo>();
			for (GnAreaVo areaVo : areaVoList) {
				GnAreaInfo areaInfo = new GnAreaInfo();
				BeanUtils.copyProperties(areaInfo, areaVo);
				String provinceCode = areaVo.getProvinceCode();
				if (StringUtils.isNotEmpty(provinceCode)) {
					String provinceName = cacheSV.getAreaName(tenantId, provinceCode);
					areaInfo.setProvinceName(provinceName);
				}
				String cityCode = areaVo.getCityCode();
				if (StringUtils.isNotEmpty(cityCode)) {
					String cityName = cacheSV.getAreaName(tenantId, cityCode);
					areaInfo.setCityName(cityName);
				}
				areaInfoList.add(areaInfo);
			}
		}
		return areaInfoList;
	}

	/**
	 * 获得页面对象信息
	 * 
	 * @param operAreaPageInfo
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private List<GnOperAreaInfo> getOperAreaInfoList(PageInfo<GnOperAreaVO> operAreaPageInfo, String tenantId) {
		List<GnOperAreaInfo> operAreaInfoList = new LinkedList<GnOperAreaInfo>();
		if (operAreaPageInfo != null) {
			List<GnOperAreaVO> queryresult = operAreaPageInfo.getResult();
			if (queryresult != null && queryresult.size() > 0) {
				ICacheSV cacheSV = DubboConsumerFactory.getService("getCacheSV");
				for (GnOperAreaVO operVo : queryresult) {
					GnOperAreaInfo GnOperAreaInfo = new GnOperAreaInfo();
					BeanUtils.copyProperties(GnOperAreaInfo, operVo);
					// 获取区域名称
					String areaCode = operVo.getAreaCode();
					if (areaCode != null) {
						String areaName = cacheSV.getAreaName(tenantId, areaCode);
						GnOperAreaInfo.setAreaName(areaName);
						String addressDesc = changeAddressInfo(tenantId, cacheSV, areaCode);
						GnOperAreaInfo.setAddressDesc(addressDesc);
					}

					operAreaInfoList.add(GnOperAreaInfo);
				}
			}
		}
		return operAreaInfoList;
	}

	/**
	 * 地址转换
	 * 
	 * @param tenantId
	 * @param cacheSV
	 * @param operVo
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private String changeAddressInfo(String tenantId, ICacheSV cacheSV, String areaCode) {
		String addressValue = "";
		if (StringUtils.isNotEmpty(areaCode)) {
			// 转换区域名称
			String areaname = cacheSV.getAreaName(tenantId, areaCode);
			addressValue = splitString(areaname, addressValue, " - ");
			// 设置父级名称
			GnAreaCodeCondition gnAreaCodeCondition = new GnAreaCodeCondition();
			gnAreaCodeCondition.setTenantId(tenantId);
			gnAreaCodeCondition.setAreaCode(areaCode);
			IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService("getGnAreaQuerySV");
			List<GnAreaVo> parentAreaList = iGnAreaQuerySV.getParentAreaListByAreaCode(gnAreaCodeCondition);
			if (parentAreaList != null) {
				for (GnAreaVo gnAreaVo : parentAreaList) {
					String areaLevel = gnAreaVo.getAreaLevel();
					if (!"0".equals(areaLevel)) {
						String areaName = gnAreaVo.getAreaName();
						addressValue = splitString(areaName, addressValue, " - ");
					}
				}
			}
		}
		return addressValue;
	}

	/**
	 * 字符串拼接
	 * 
	 * @param str1
	 * @param str2
	 * @param character
	 *            拼接符
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private String splitString(String str1, String str2, String character) {
		if (StringUtils.isEmpty(str1)) {
			return str2;
		} else if (StringUtils.isEmpty(str2)) {
			return str1;
		} else {
			return str1 + character + str2;
		}
	}

	@ResponseBody
	@RequestMapping("/delOperArea")
	ResponseData<String> delOperArea(HttpServletRequest request, GnOperAreaParamsVO gnOperAreaParamsVO) {
		ResponseData<String> responseData;
		try {
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			String tenantId = user.getTenantId();
			gnOperAreaParamsVO.setTenantId(tenantId);
			gnOperAreaParamsVO.setUpdateOperId(user.getOperId());
			gnOperAreaParamsVO.setUpdateTime(DateTimeUtil.getCurrTimestamp());
			IGnOperAreaManageSV operAreaManageSV = DubboConsumerFactory.getService("gnOperAreaManageSV");
			GnOperAreaResponseVO result = operAreaManageSV.deleteOperArea(gnOperAreaParamsVO);
			if (result != null && ResponseData.AJAX_STATUS_SUCCESS.equals(result.responseCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "操作员区域关系删除成功", result.respondeDesc);
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员区域关系删除失败", result == null? "操作员区域关系删除失败":result.respondeDesc);
			}
		} catch (CallerException ex) {
			LOGGER.error("操作员区域关系删除失败！错误信息：", ex);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员区域关系删除失败", null);
		}
		return responseData;
	}

	@ResponseBody
	@RequestMapping(value = "/queryOperAreaList", method = RequestMethod.POST)
	public ResponseData<PagerResult<GnOperAreaInfo>> queryOperAreaList(HttpServletRequest request, GnOperAreaParamsVO gnOperAreaParamsVO,
			@RequestParam("currentPage") Integer currentPage) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<PagerResult<GnOperAreaInfo>> responseData = null;
		try {
			String tenantId = user.getTenantId();
			gnOperAreaParamsVO.setTenantId(tenantId);
			PageInfo<GnOperAreaVO> pageInfo = new PageInfo<GnOperAreaVO>();
			pageInfo.setPageNo(currentPage);
			pageInfo.setPageSize(PAGESIZE);
			gnOperAreaParamsVO.setPageInfo(pageInfo);
			gnOperAreaParamsVO.setInactiveTime(DateUtil.getSysDate());

			IGnOperAreaManageSV operAreaManageSV = DubboConsumerFactory.getService("gnOperAreaManageSV");
			PageInfo<GnOperAreaVO> operAreaPageInfo = operAreaManageSV.queryAreaPageByOperId(gnOperAreaParamsVO);
			PagerResult<GnOperAreaInfo> pagerResult = new PagerResult<GnOperAreaInfo>();
			Pager pager = new Pager(currentPage, operAreaPageInfo != null ? operAreaPageInfo.getCount() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			List<GnOperAreaInfo> operInfoList = getOperAreaInfoList(operAreaPageInfo, tenantId);
			pagerResult.setResult(operInfoList);
			responseData = new ResponseData<PagerResult<GnOperAreaInfo>>(ResponseData.AJAX_STATUS_SUCCESS, "获取操作员信息成功！", pagerResult);
		} catch (CallerException e) {
			LOGGER.error("获取操作员区域信息失败！错误信息：", e);
			responseData = new ResponseData<PagerResult<GnOperAreaInfo>>(ResponseData.AJAX_STATUS_FAILURE, "获取操作员信息失败！", null);
		}
		return responseData;
	}

	@ResponseBody
	@RequestMapping("/addAreaToOper")
	public ResponseData<String> addAreaToOper(HttpServletRequest request, @RequestParam("areaOperListStr") String areaOperListStr) {
		ResponseData<String> responseData = null;
		String statusCode = null;// responseData statusCode
		String statusInfo = null;// responseData statusInfo
		String dataInfo = null;// responseData data
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		try {
			Type listType = new TypeToken<LinkedList<GnOperAreaParamsVO>>() {
			}.getType();
			Gson gson = new Gson();
			areaOperListStr = StringEscapeUtils.unescapeHtml4(areaOperListStr);
			List<GnOperAreaParamsVO> operAreaVOParamsList = gson.fromJson(areaOperListStr, listType);
			if (operAreaVOParamsList != null && operAreaVOParamsList.size() > 0) {
				IGnOperAreaManageSV operAreaManageSV = DubboConsumerFactory.getService("gnOperAreaManageSV");
				for (GnOperAreaParamsVO operAreaParamsvo : operAreaVOParamsList) {
					operAreaParamsvo.setTenantId(user.getTenantId());
					operAreaParamsvo.setCreateOperId(user.getOperId());
					GnOperAreaResponseVO addAreaToOper = operAreaManageSV.addAreaToOper(operAreaParamsvo);
					if (ResponseData.AJAX_STATUS_SUCCESS.equals(addAreaToOper.responseCode)) {
						statusCode = ResponseData.AJAX_STATUS_SUCCESS;
						statusInfo = "操作员添加区域权限成功!";
						dataInfo = addAreaToOper.respondeDesc;
					} else {
						statusCode = ResponseData.AJAX_STATUS_FAILURE;
						statusInfo = "操作员添加区域权限失败!";
						dataInfo = addAreaToOper.respondeDesc;
						break;
					}
				}
			} else {
				statusCode = ResponseData.AJAX_STATUS_FAILURE;
				statusInfo = "添加失败，无操作员添加区域权限的相关数据！";
				dataInfo = "添加失败，无操作员添加区域权限的相关数据！";
			}
		} catch (CallerException ex) {
			LOGGER.error("添加区域信息失败！错误信息：", ex);
			statusCode = ResponseData.AJAX_STATUS_FAILURE;
			statusInfo = "操作员添加区域权限失败！";
			dataInfo = "添加失败，无操作员添加区域权限的相关数据！";
		} finally {
			responseData = new ResponseData<String>(statusCode, statusInfo, dataInfo);
		}
		return responseData;
	}
}
