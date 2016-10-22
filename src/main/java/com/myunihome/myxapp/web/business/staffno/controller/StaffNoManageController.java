package com.myunihome.myxapp.web.business.staffno.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.util.Md5Encoder;
import com.myunihome.myxapp.sys.api.sysstaffno.interfaces.IGnStaffNoManageSV;
import com.myunihome.myxapp.sys.api.sysstaffno.param.GnStaffNoParamsVO;
import com.myunihome.myxapp.sys.api.sysstaffno.param.GnStaffNoPasswdVO;
import com.myunihome.myxapp.sys.api.sysstaffno.param.GnStaffNoResponseVO;
import com.myunihome.myxapp.sys.api.sysstaffno.param.GnStaffNoVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.system.constants.Constants;
import com.myunihome.myxapp.web.system.util.DateTimeUtil;

import net.sf.json.JSONObject;

/**
 * 员工工号管理 Date: 2015年9月23日 <br>
 * Copyright (c) 2015 asiainfo.com <br>
 * 
 * @author liangbs
 */
@Controller
@RequestMapping(value = "/staffNoManage")
public class StaffNoManageController {

	private static final Logger LOGGER = Logger.getLogger(StaffNoManageController.class);

	/**
	 * 分页大小
	 */
	private static final int PAGESIZE = 5;

	/**
	 * 跳转页面
	 */
	@RequestMapping(value = "/toStaffNoList")
	public ModelAndView toStaffNoList(HttpServletRequest request, HttpServletResponse response) {

		UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		request.setAttribute("tenantId", user.getTenantId());// 租户ID

		return new ModelAndView("staffno/staffNoList");
	}

	/**
	 * 查询列表
	 * 
	 * @param condition
	 *            员工工号查询条件
	 * @param currentPage
	 *            请求页码
	 * @return
	 * @author liangbs
	 */
	@ResponseBody
	@RequestMapping("/queryStaffNoList")
	public ResponseData<PagerResult<GnStaffNoVO>> queryOrderAuditList(HttpServletRequest request, @RequestParam("currentPage") Integer currentPage,
			GnStaffNoParamsVO gnStaffNoParamsVO) {
		/*
		 * ,
		 * 
		 * @RequestParam("activeTimeStr") String activeTimeStr,
		 * 
		 * @RequestParam("inactiveTimeStr") String inactiveTimeStr
		 */

		ResponseData<PagerResult<GnStaffNoVO>> responseData = null;
		try {

			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			gnStaffNoParamsVO.setTenantId(user.getTenantId());// 租户ID
			gnStaffNoParamsVO.setInactiveTime(DateTimeUtil.getCurrTimestamp());
			/*
			 * if(StringUtils.isEmpty(activeTimeStr)){
			 * gnStaffNoParamsVO.setActiveTime(DateTimeUtil.getCurrTimestamp());
			 * }else{
			 * gnStaffNoParamsVO.setActiveTime(DateTimeUtil.stringToTimstamp
			 * (activeTimeStr)); } if(StringUtils.isEmpty(inactiveTimeStr)){
			 * gnStaffNoParamsVO
			 * .setInactiveTime(DateTimeUtil.getCurrTimestamp()); }else{
			 * gnStaffNoParamsVO
			 * .setInactiveTime(DateTimeUtil.stringToTimstamp(inactiveTimeStr));
			 * }
			 */

			PageInfo<GnStaffNoVO> pageInfo = new PageInfo<GnStaffNoVO>();
			pageInfo.setPageNo(currentPage);
			pageInfo.setPageSize(PAGESIZE);
			gnStaffNoParamsVO.setPageInfo(pageInfo);// 分页信息

			IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
			PageInfo<GnStaffNoVO> results = gnStaffNoManageSV.queryStaffNo(gnStaffNoParamsVO);

			PagerResult<GnStaffNoVO> pagerResult = new PagerResult<GnStaffNoVO>();
			Pager pager = new Pager(currentPage, results != null ? results.getCount() : 0, PAGESIZE);
			pagerResult.setPager(pager);
			pagerResult.setResult(results != null ? results.getResult() : null);

			responseData = new ResponseData<PagerResult<GnStaffNoVO>>(ResponseData.AJAX_STATUS_SUCCESS, "员工工号列表查询成功", pagerResult);
		} catch (Exception e) {
			LOGGER.error("员工工号列表查询失败，错误信息：", e);
			responseData = new ResponseData<PagerResult<GnStaffNoVO>>(ResponseData.AJAX_STATUS_FAILURE, "员工工号列表查询失败", null);
		}
		return responseData;
	}

	/**
	 * 获取单条数据
	 * 
	 * @param request
	 * @param gnStaffNoParamsVO
	 * @param currentPage
	 * @return
	 * @author liangbs
	 */
	@ResponseBody
	@RequestMapping("/getStaffNoData")
	public ResponseData<GnStaffNoVO> getStaffNoData(HttpServletRequest request, @RequestParam("staffnoId") Long staffnoId) {

		ResponseData<GnStaffNoVO> responseData = null;
		try {

			GnStaffNoVO result = null;
			if (staffnoId != null) {
				UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
				GnStaffNoParamsVO gnStaffNoParamsVO = new GnStaffNoParamsVO();
				gnStaffNoParamsVO.setTenantId(user.getTenantId());
				gnStaffNoParamsVO.setStaffnoId(staffnoId);

				IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
				result = gnStaffNoManageSV.queryStaffNoById(gnStaffNoParamsVO);
			}

			if (result != null) {
				responseData = new ResponseData<GnStaffNoVO>(ResponseData.AJAX_STATUS_SUCCESS, "员工工号查询成功", result);
			} else {
				responseData = new ResponseData<GnStaffNoVO>(ResponseData.AJAX_STATUS_FAILURE, "员工工号查询失败", null);
			}
		} catch (Exception e) {
			LOGGER.error("员工工号查询失败，错误信息：", e);
			responseData = new ResponseData<GnStaffNoVO>(ResponseData.AJAX_STATUS_FAILURE, "员工工号查询失败", null);
		}
		return responseData;
	}

	/**
	 * 员工工号新增或更新
	 * 
	 * @param request
	 * @param gnStaffNoParamsVO
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrUpdateStaffNo", method = RequestMethod.POST)
	public ResponseData<String> addOrUpdateStaffNo(HttpServletRequest request, GnStaffNoParamsVO gnStaffNoParamsVO,
			@RequestParam("activeTimeStr") String activeTimeStr, @RequestParam("inactiveTimeStr") String inactiveTimeStr) {

		ResponseData<String> responseData = null;
		try {
			if (gnStaffNoParamsVO != null) {
				Long staffnoId = gnStaffNoParamsVO.getStaffnoId();
				UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
				if (staffnoId != null) {
					// 更新操作
					responseData = updateStaffInfo(gnStaffNoParamsVO, activeTimeStr, inactiveTimeStr, user);
				} else {
					// 检查工号是否已存在
					IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
					gnStaffNoParamsVO.setTenantId(user.getTenantId());
					Boolean isStaffNoUnique = gnStaffNoManageSV.isStaffNoUnique(gnStaffNoParamsVO);
					if (isStaffNoUnique) {
						// 新增操作
						responseData = addStaffInfo(gnStaffNoParamsVO, activeTimeStr, inactiveTimeStr, user);
					} else {
						responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号已存在，不可重复！", "员工工号已存在！");
					}
				}
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号操作失败", "参数为空");
			}
		} catch (Exception e) {
			LOGGER.error("员工工号创建失败，错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号操作失败", "操作异常");
		}
		return responseData;
	}

	private ResponseData<String> addStaffInfo(GnStaffNoParamsVO gnStaffNoParamsVO, String activeTimeStr, String inactiveTimeStr, UserLoginVo user)
			throws Exception {
		ResponseData<String> responseData = null;
		gnStaffNoParamsVO.setTenantId(user.getTenantId());
		gnStaffNoParamsVO.setCreateOperId(user.getOperId());
		gnStaffNoParamsVO.setStaffPasswd(Md5Encoder.encodePassword(gnStaffNoParamsVO.getStaffPasswd()));// 加密密码
		gnStaffNoParamsVO.setActiveTime(DateTimeUtil.stringToTimstamp(activeTimeStr));
		gnStaffNoParamsVO.setInactiveTime(DateTimeUtil.stringToTimstamp(inactiveTimeStr));

		IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
		Long staffnoId = gnStaffNoManageSV.addStaffNo(gnStaffNoParamsVO);

		if (staffnoId != null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "员工工号创建成功", staffnoId.toString());
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号创建失败", null);
		}
		return responseData;
	}

	private ResponseData<String> updateStaffInfo(GnStaffNoParamsVO gnStaffNoParamsVO, String activeTimeStr, String inactiveTimeStr, UserLoginVo user)
			throws Exception {
		ResponseData<String> responseData = null;
		// 更新操作
		gnStaffNoParamsVO.setTenantId(user.getTenantId());
		gnStaffNoParamsVO.setUpdateOperId(user.getOperId());
		gnStaffNoParamsVO.setState(Constants.STATE_NORMAL);
		gnStaffNoParamsVO.setActiveTime(DateTimeUtil.stringToTimstamp(activeTimeStr));
		gnStaffNoParamsVO.setInactiveTime(DateTimeUtil.stringToTimstamp(inactiveTimeStr));

		gnStaffNoParamsVO.setStaffPasswd(null);// 不能在此修改密码

		IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
		GnStaffNoResponseVO result = gnStaffNoManageSV.updateStaffNo(gnStaffNoParamsVO);

		if ("1".equals(result.responseCode)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "员工工号更新成功", result.respondeDesc);
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号更新失败", result.respondeDesc);
		}
		return responseData;
	}

	/**
	 * 员工工号删除操作
	 * 
	 * @param request
	 * @param gnStaffNoParamsVO
	 * @param activeTimeStr
	 * @param inactiveTimeStr
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/delStaffNo", method = RequestMethod.POST)
	public ResponseData<String> delStaffNo(HttpServletRequest request, GnStaffNoParamsVO gnStaffNoParamsVO) {

		ResponseData<String> responseData = null;
		try {
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			GnStaffNoResponseVO result = null;

			Long staffnoId = gnStaffNoParamsVO.getStaffnoId();
			if (staffnoId != null) {

				gnStaffNoParamsVO.setTenantId(user.getTenantId());
				IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
				result = gnStaffNoManageSV.delStaffNoByID(gnStaffNoParamsVO);

				if ("1".equals(result.responseCode)) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "员工工号删除成功", result.respondeDesc);
				} else {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号删除失败", result.respondeDesc);
				}

			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号删除失败", "参数为空");
			}
		} catch (Exception e) {
			LOGGER.error("员工工号创建失败，错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "员工工号删除失败", "操作异常");
		}
		return responseData;
	}

	/**
	 * 重置员工密码
	 * 
	 * @param request
	 * @param gnStaffNoParamsVO
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value = "/resetStaffPasswd", method = RequestMethod.POST)
	public ResponseData<String> resetStaffPasswd(HttpServletRequest request, GnStaffNoPasswdVO gnStaffNoPasswdVO) {

		ResponseData<String> responseData = null;
		try {
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			GnStaffNoResponseVO result = null;

			String staffNo = gnStaffNoPasswdVO.getStaffNo();
			if (StringUtils.isNotEmpty(staffNo)) {
				gnStaffNoPasswdVO.setTenantId(user.getTenantId());
				IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
				result = gnStaffNoManageSV.resetStaffPassword(gnStaffNoPasswdVO);

				if ("1".equals(result.responseCode)) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "工号密码重置成功", result.respondeDesc);
				} else {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "工号密码重置失败", result.respondeDesc);
				}

			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "工号密码重置失败", "参数为空");
			}
		} catch (Exception e) {
			LOGGER.error("员工工号创建失败，错误信息：", e);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "工号密码重置失败", "操作异常");
		}
		return responseData;
	}

	/**
	 * 员工工号唯一性检查操作
	 * 
	 * @param request
	 * @param staffnoId
	 * @param staffNo
	 * @return
	 * @author liangbs
	 */
	@ResponseBody
	@RequestMapping(value = "/isStaffNoUnique", method = RequestMethod.POST)
	public JSONObject isStaffNoUnique(HttpServletRequest request, Long staffnoId, String staffNo) {

		JSONObject json = new JSONObject();
		try {
			UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);

			if (staffNo != null) {
				GnStaffNoParamsVO gnStaffNoParamsVO = new GnStaffNoParamsVO();
				gnStaffNoParamsVO.setTenantId(user.getTenantId());
				gnStaffNoParamsVO.setStaffnoId(staffnoId);
				gnStaffNoParamsVO.setStaffNo(staffNo);

				IGnStaffNoManageSV gnStaffNoManageSV = DubboConsumerFactory.getService("gnStaffNoManageSV");
				Boolean resultFlag = gnStaffNoManageSV.isStaffNoUnique(gnStaffNoParamsVO);
				json.put("valid", resultFlag);
			}
		} catch (Exception e) {
			LOGGER.error("员工工号唯一性检查失败，错误信息：", e);
		}
		return json;
	}
}