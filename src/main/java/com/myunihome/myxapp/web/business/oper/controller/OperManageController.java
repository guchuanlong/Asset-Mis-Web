package com.myunihome.myxapp.web.business.oper.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.common.api.cache.param.Depart;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.util.BeanUtils;
import com.myunihome.myxapp.sso.api.oper.interfaces.IOperatorSV;
import com.myunihome.myxapp.sso.api.oper.param.OperatorVo;
import com.myunihome.myxapp.sys.api.sysoper.interfaces.IGnOperManageSV;
import com.myunihome.myxapp.sys.api.sysoper.param.GnOperParamsVO;
import com.myunihome.myxapp.sys.api.sysoper.param.GnOperResponseVO;
import com.myunihome.myxapp.sys.api.sysoper.param.GnOperVO;
import com.myunihome.myxapp.sys.api.sysstaffno.interfaces.IGnStaffNoManageSV;
import com.myunihome.myxapp.sys.api.sysstaffno.param.GnStaffNoParamsVO;
import com.myunihome.myxapp.sys.api.sysstaffno.param.GnStaffNoVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.business.oper.model.GnOperInfo;
import com.myunihome.myxapp.web.business.oper.model.GnStaffOperParams;
import com.myunihome.myxapp.web.system.constants.Constants;
import com.myunihome.myxapp.web.system.util.AreaUtil;

@Controller
@RequestMapping(value = "/operManage")
public class OperManageController {
	private static final Logger LOGGER = Logger.getLogger(OperManageController.class);

	/**
	 * 分页大小
	 */
	private static final int PAGESIZE = 5;

	/**
	 * 根据员工号获得操作员信息
	 * 
	 * @param request
	 * @param gnOperParamsVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOperListByStaffno", method = RequestMethod.POST)
	public ResponseData<List<GnOperInfo>> getOperListByStaffno(HttpServletRequest request, @RequestParam("staffnoId") Long staffnoId) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<List<GnOperInfo>> responseData = null;
		try {
			// 获得操作员列表信息
			IOperatorSV operQuerySV = DubboConsumerFactory.getService("getOperatorSV");
			List<OperatorVo> operVOList = operQuerySV.checkStaffInfoBystaffno(staffnoId, user.getTenantId());
			List<GnOperInfo> operInfoList = new LinkedList<GnOperInfo>();
			if (operVOList != null && operVOList.size() > 0) {
				for (OperatorVo operatorVo : operVOList) {
					GnOperInfo operInfo = new GnOperInfo();
					BeanUtils.copyProperties(operInfo, operatorVo);
					String orgName = getOrgName(operInfo);
					operInfo.setOrgName(orgName);
					operInfoList.add(operInfo);
				}
			}
			responseData = new ResponseData<List<GnOperInfo>>(ResponseData.AJAX_STATUS_SUCCESS, "获取操作员信息成功！", operInfoList);
		} catch (CallerException e) {
			LOGGER.error("获取操作员信息失败！错误信息：", e);
			responseData = new ResponseData<List<GnOperInfo>>(ResponseData.AJAX_STATUS_FAILURE, "获取操作员信息失败！", null);
		}
		return responseData;
	}

	/**
	 * 获取操作员信息
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/getOperInfo", method = RequestMethod.POST)
	public ResponseData<GnOperInfo> getOperInfo(HttpServletRequest request, GnOperParamsVO gnOperParamsVO) {

		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<GnOperInfo> responseData = null;
		try {
			String tenantId = user.getTenantId();
			Long operId = gnOperParamsVO.getOperId();
			GnOperInfo queryOperInfo = new GnOperInfo();
			if (operId != null) {
				GnOperParamsVO queryBaseInfo = new GnOperParamsVO();
				queryBaseInfo.setOperId(operId);
				queryBaseInfo.setTenantId(tenantId);
				queryBaseInfo.setState(Constants.STATE_NORMAL);
				// 操作员查询服务
				IGnOperManageSV operQuerySV = DubboConsumerFactory.getService("gnOperManageSV");
				GnOperVO operInfo = operQuerySV.queryOperById(queryBaseInfo);
				BeanUtils.copyProperties(queryOperInfo, operInfo);
				String orgName = getOrgName(operInfo);
				queryOperInfo.setOrgName(orgName);
			}
			responseData = new ResponseData<GnOperInfo>(ResponseData.AJAX_STATUS_SUCCESS, "获取操作员信息成功", queryOperInfo);
		} catch (CallerException e) {
			LOGGER.error("获取操作员信息失败！错误信息：", e);
			responseData = new ResponseData<GnOperInfo>(ResponseData.AJAX_STATUS_FAILURE, "获取操作员信息失败", null);
		}
		return responseData;
	}

	/**
	 * 获得组织名称
	 * 
	 * @param operInfo
	 * @return
	 * @author jiaxs
	 */
	private String getOrgName(GnOperVO operInfo) {
		String orgName = null;
		String tenantId = operInfo.getTenantId();
		String orgType = operInfo.getOrgType();
		String orgId = operInfo.getOrgId();
		if ("1".equals(orgType) && StringUtils.isNotEmpty(orgId)) {
			// 部门
			ICacheSV cacheSV = DubboConsumerFactory.getService("getCacheSV");
			orgName = cacheSV.getDepartName(tenantId, orgId);
		} 
		return orgName;
	}

	/**
	 * 操作员数据管理（新增或修改）
	 * 
	 * @param request
	 * @param gnOperParamsVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrUpDateOperInfo", method = RequestMethod.POST)
	public ResponseData<String> addOrUpDateOperInfo(HttpServletRequest request, GnOperParamsVO gnOperParamsVO) {
		Long operId = gnOperParamsVO.getOperId();
		if (operId == null) {
			return addRoleInfo(request, gnOperParamsVO);
		} else {
			return updateRoleInfo(request, gnOperParamsVO);
		}
	}

	/**
	 * 新增操作员信息
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/addOperInfo", method = RequestMethod.POST)
	public ResponseData<String> addRoleInfo(HttpServletRequest request, GnOperParamsVO gnOperParamsVO) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<String> responseData = null;
		try {
			// 添加默认值
			setOperDefValues(user, gnOperParamsVO, 1);
			// 角色管理服务
			IGnOperManageSV gnOperManageSV = DubboConsumerFactory.getService("gnOperManageSV");
			Long operId = gnOperManageSV.addOper(gnOperParamsVO);
			if (operId != null) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "操作员创建成功", operId.toString());
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员创建失败", null);
			}
		} catch (Exception e) {
			LOGGER.error("添加操作员信息失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员创建失败", null);
		}
		return responseData;
	}

	/**
	 * 更新操作员信息
	 * 
	 * @param
	 * @return
	 * @author
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/updateOpreInfo", method = RequestMethod.POST)
	public ResponseData<String> updateRoleInfo(HttpServletRequest request, GnOperParamsVO gnOperParamsVO) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<String> responseData = null;
		try {
			// 添加默认值
			setOperDefValues(user, gnOperParamsVO, 2);

			// 操作员管理服务
			IGnOperManageSV gnOperManageSV = DubboConsumerFactory.getService("gnOperManageSV");
			GnOperResponseVO gnOperResponseVO = gnOperManageSV.updateOper(gnOperParamsVO);
			if (ResponseData.AJAX_STATUS_SUCCESS.equals(gnOperResponseVO.responseCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "操作员更新成功", gnOperResponseVO.respondeDesc);
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员更新失败", gnOperResponseVO.respondeDesc);
			}
		} catch (Exception e) {
			LOGGER.error("添加操作员信息失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "操作员创建失败", null);
		}
		return responseData;
	}

	/**
	 * 删除操作员信息
	 * 
	 * @param request
	 * @param gnOperParamsVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delOperInfo")
	public ResponseData<String> delRoleInfo(HttpServletRequest request, GnOperParamsVO gnOperParamsVO) {

		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

		ResponseData<String> responseData = null;
		try {
			// 设置默认值
			setOperDefValues(user, gnOperParamsVO, 4);
			// 操作员管理服务
			IGnOperManageSV operManageSV = DubboConsumerFactory.getService("gnOperManageSV");
			GnOperResponseVO gnOperResponseVO = operManageSV.delOperByID(gnOperParamsVO);
			String responseCode = gnOperResponseVO.responseCode;
			if (ResponseData.AJAX_STATUS_SUCCESS.equals(responseCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "删除操作员信息成功", gnOperResponseVO.respondeDesc);
			} else{
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "删除操作员信息失败", gnOperResponseVO.respondeDesc);
			}
		} catch (CallerException e) {
			LOGGER.error("添加操作员信息失败！错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "删除操作员信息失败", null);
		}
		return responseData;
	}

	/**
	 * 设置默认值
	 * 
	 * @param user
	 * @param gnOperParamsVO
	 * @param i
	 *            1:新增 2：修改 4:删除
	 */
	private void setOperDefValues(UserLoginVo user, GnOperParamsVO gnOperParamsVO, int i) {
		String tenantId = user.getTenantId();// 租户
		gnOperParamsVO.setTenantId(tenantId);
		long operId = user.getOperId();// 操作人id
		if ((i & 1) == 1) {
			gnOperParamsVO.setCreateOperId(operId);
			gnOperParamsVO.setState(Constants.STATE_NORMAL);
		} else if ((i & 2) == 2) {
			gnOperParamsVO.setUpdateOperId(operId);
		} else if ((i & 4) == 4) {
			gnOperParamsVO.setUpdateOperId(operId);
			gnOperParamsVO.setState(Constants.STATE_DELETED);
		}
	}

	/**
	 * 根据员工号获得操作员信息
	 * 
	 * @param request
	 * @param gnOperParamsVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryOperPageInfo", method = RequestMethod.POST)
	public ResponseData<PagerResult<GnOperInfo>> queryOperPageInfo(HttpServletRequest request, GnStaffOperParams gnStaffOperParamsVO,
			@RequestParam("currentPage") Integer currentPage) {
		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		ResponseData<PagerResult<GnOperInfo>> responseData = null;
		try {
			List<GnOperInfo> operInfoList = new LinkedList<GnOperInfo>();

			PageInfo<GnOperVO> operPageInfo = null;

			GnOperParamsVO gnOperParamsVO = new GnOperParamsVO();
			BeanUtils.copyProperties(gnOperParamsVO, gnStaffOperParamsVO);
			String staffName = gnStaffOperParamsVO.getStaffName();
			// 如果存在名称查询条件通过名称查询员工工号id集合
			if (StringUtils.isNotEmpty(staffName)) {
				List<Long> staffnoIdList = getStaffnoIdList(gnStaffOperParamsVO, user);
				// 若存在数据 加入查询条件 若没有 说明没有数据 直接返回空
				if (staffnoIdList != null && staffnoIdList.size() > 0) {
					gnOperParamsVO.setStaffnoIdList(staffnoIdList);
					operPageInfo = getOperPageData(currentPage, user, gnOperParamsVO);
				}
			} else {
				operPageInfo = getOperPageData(currentPage, user, gnOperParamsVO);
			}
			// 获得返回数据
			PagerResult<GnOperInfo> pagerResult = getPageResultData(currentPage, operInfoList, operPageInfo);
			responseData = new ResponseData<PagerResult<GnOperInfo>>(ResponseData.AJAX_STATUS_SUCCESS, "获取操作员信息成功！", pagerResult);
		} catch (CallerException e) {
			LOGGER.error("获取操作员信息失败！错误信息：", e);
			responseData = new ResponseData<PagerResult<GnOperInfo>>(ResponseData.AJAX_STATUS_FAILURE, "获取操作员信息失败！", null);
		}
		return responseData;
	}

	// 转换为返回数据
	private PagerResult<GnOperInfo> getPageResultData(Integer currentPage, List<GnOperInfo> operInfoList, PageInfo<GnOperVO> operPageInfo) {
		PagerResult<GnOperInfo> pagerResult = new PagerResult<GnOperInfo>();
		Pager pager = new Pager(currentPage, operPageInfo != null ? operPageInfo.getCount() : 0, PAGESIZE);
		pagerResult.setPager(pager);
		if (operPageInfo != null) {
			List<GnOperVO> queryresult = operPageInfo.getResult();
			if (queryresult != null && queryresult.size() > 0) {
				for (GnOperVO operVo : queryresult) {
					GnOperInfo gnOperInfo = new GnOperInfo();
					BeanUtils.copyProperties(gnOperInfo, operVo);
					// 设置组织名称
					String orgName = getOrgName(operVo);
					gnOperInfo.setOrgName(orgName);
					// 设置员工名称
					String staffName = getStaffName(operVo);
					gnOperInfo.setStaffName(staffName);
					// 设置省份,城市名称
					Map<String, String> areaData = getAreaData(operVo);
					gnOperInfo.setProvinceName(areaData.get("provinceName"));
					gnOperInfo.setCityName(areaData.get("cityName"));
					// 添加
					operInfoList.add(gnOperInfo);
				}
			}
		}
		pagerResult.setResult(operInfoList);
		return pagerResult;
	}

	// 获取地区信息
	private Map<String, String> getAreaData(GnOperVO operVo) {
		Map<String, String> areaMap = new HashMap<String, String>();
		String orgType = operVo.getOrgType();
		String orgId = operVo.getOrgId();
		String tenantId = operVo.getTenantId();
		if ("1".equals(orgType) && StringUtils.isNotEmpty(orgId)) {
			// 部门
			ICacheSV cacheSV = DubboConsumerFactory.getService("getCacheSV");
			Depart depart = cacheSV.getDepart(tenantId, orgId);
			if (depart != null) {
				// 省份
				String provinceCode = depart.getProvinceCode();
				String provinceName = AreaUtil.getProvinceName(tenantId, provinceCode);
				areaMap.put("provinceName", provinceName);
				// 城市
				String cityCode = depart.getCityCode();
				String cityName = AreaUtil.getCityName(tenantId, provinceCode, cityCode);
				areaMap.put("cityName", cityName);
			}
		} 
		return areaMap;
	}

	// 获得员工姓名
	private String getStaffName(GnOperVO operVo) {
		String staffName = null;
		IGnStaffNoManageSV staffnoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
		GnStaffNoParamsVO staffNoParams = new GnStaffNoParamsVO();
		staffNoParams.setTenantId(operVo.getTenantId());
		staffNoParams.setStaffnoId(operVo.getStaffnoId());
		GnStaffNoVO staffNoVO = staffnoManageSV.queryStaffNoById(staffNoParams);
		if (staffNoVO != null) {
			staffName = staffNoVO.getStaffName();
		}
		return staffName;
	}

	private PageInfo<GnOperVO> getOperPageData(Integer currentPage, UserLoginVo user, GnOperParamsVO gnOperParamsVO) {
		// 设置查询条件
		IGnOperManageSV operManageSV = DubboConsumerFactory.getService("gnOperManageSV");
		gnOperParamsVO.setTenantId(user.getTenantId());
		PageInfo<GnOperVO> pageInfo = new PageInfo<GnOperVO>();
		pageInfo.setPageNo(currentPage);
		pageInfo.setPageSize(PAGESIZE);
		gnOperParamsVO.setPageInfo(pageInfo);

		// 查询数据
		return operManageSV.queryOperPageInfo(gnOperParamsVO);
	}

	/**
	 * 获取员工工号id list
	 * 
	 * @param gnStaffOperParamsVO
	 * @param user
	 * @param gnOperParamsVO
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private List<Long> getStaffnoIdList(GnStaffOperParams gnStaffOperParamsVO, UserLoginVo user) {
		List<Long> staffnoIdList = null;
		IGnStaffNoManageSV staffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
		GnStaffNoParamsVO gnstaffnoParams = new GnStaffNoParamsVO();
		BeanUtils.copyProperties(gnstaffnoParams, gnStaffOperParamsVO);
		gnstaffnoParams.setTenantId(user.getTenantId());
		List<GnStaffNoVO> staffNoList = staffNoManageSV.queryStaffNoList(gnstaffnoParams);
		if (staffNoList != null && staffNoList.size() > 0) {
			staffnoIdList = new LinkedList<Long>();
			for (GnStaffNoVO gnStaffNoVO : staffNoList) {
				staffnoIdList.add(gnStaffNoVO.getStaffnoId());
			}
		}
		return staffnoIdList;
	}
}
