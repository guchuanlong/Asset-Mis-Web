package com.myunihome.myxapp.web.business.staff.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.myunihome.myxapp.base.exception.BusinessException;
import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.common.api.depart.interfaces.IGnDepartQuerySV;
import com.myunihome.myxapp.common.api.depart.param.GnDepartCondition;
import com.myunihome.myxapp.common.api.depart.param.GnDepartVo;
import com.myunihome.myxapp.common.api.staff.interfaces.IGnStaffMaintainSV;
import com.myunihome.myxapp.common.api.staff.interfaces.IGnStaffQuerySV;
import com.myunihome.myxapp.common.api.staff.param.GnStaffCondition;
import com.myunihome.myxapp.common.api.staff.param.GnStaffVo;
import com.myunihome.myxapp.common.api.staff.param.StaffPaginationResult;
import com.myunihome.myxapp.framepage.util.OperInfoUtil;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerHelper;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.qrcode.factory.QRCodeFactory;
import com.myunihome.myxapp.paas.util.StringUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.web.business.staff.model.StaffCondition;
import com.myunihome.myxapp.web.business.staff.model.StaffDto;

@Controller
@RequestMapping("/maintain/staff")
public class StaffMaintainController {

    private static final Logger logger = LogManager.getLogger(StaffMaintainController.class);

    private static final int pageSize = 10;

    @RequestMapping("/main")
    public ModelAndView queryMain() {
        return new ModelAndView("staff/staff_main");
    }

    @RequestMapping("/query")
    @ResponseBody
    public ResponseData<PagerResult<GnStaffVo>> queryStaffByPage(StaffCondition condition,
            @RequestParam("currentPage") Integer currentPage, HttpSession session) {
        ResponseData<PagerResult<GnStaffVo>> responseData = null;
        try {
            GnStaffCondition searchCondition = new GnStaffCondition();
            searchCondition.setTenantId(OperInfoUtil.getTenantId(session));
            searchCondition.setDepartId(condition.getDepartId());
            searchCondition.setStaffNo(condition.getStaffNo());
            searchCondition.setStaffName(condition.getStaffName());
            IGnStaffQuerySV gnStaffQuerySV = DubboConsumerFactory.getService(IGnStaffQuerySV.class);
            StaffPaginationResult result = gnStaffQuerySV.selectByPage(searchCondition,
                    PagerHelper.getStartRow(currentPage, pageSize), pageSize);

            Pager pager = new Pager(currentPage, result.getTotal(), pageSize);
            // 翻译省份、地市

            ICacheSV cacheSV = DubboConsumerFactory.getService(ICacheSV.class);
            List<GnStaffVo> info = result.getResult();

            if (info != null && info.size() > 0) {
                for (int i = 0; i < info.size(); i++) {
                    if (!StringUtil.isBlank(info.get(i).getDepartId())) {
                        GnDepartCondition serchCondition = new GnDepartCondition();
                        serchCondition.setTenantId(OperInfoUtil.getTenantId(session));
                        serchCondition.setDepartId(info.get(i).getDepartId());
                        IGnDepartQuerySV gnDepartQuerySV = DubboConsumerFactory
                                .getService(IGnDepartQuerySV.class);
                        GnDepartVo depart = gnDepartQuerySV.selectById(serchCondition);
                        if (depart != null) {
                            if (!StringUtil.isBlank(depart.getProvinceCode())) {
                                // 翻译省份
                                String provinceName = cacheSV.getAreaName(
                                        OperInfoUtil.getTenantId(session),
                                        depart.getProvinceCode());
                                info.get(i).setPostcode(provinceName);
                            }
                            if (!StringUtil.isBlank(depart.getCityCode())) {
                                if ("000".equals(depart.getCityCode())) {

                                    depart.setCityCode("00");
                                }
                                // 翻译地市
                                String cityName = cacheSV.getAreaName(
                                        OperInfoUtil.getTenantId(session), depart.getCityCode());
                                info.get(i).setPositionCode(cityName);
                            }

                        }

                    }

                }
            }
            PagerResult<GnStaffVo> pagerResult = new PagerResult<GnStaffVo>();
            pagerResult.setPager(pager);
            pagerResult.setResult(result.getResult());

            responseData = new ResponseData<PagerResult<GnStaffVo>>(
                    ResponseData.AJAX_STATUS_SUCCESS, "查询员工成功", pagerResult);
        } catch (Exception e) {
            logger.error("查询员工失败", e);
            responseData = new ResponseData<PagerResult<GnStaffVo>>(
                    ResponseData.AJAX_STATUS_FAILURE, e.getMessage(), null);
        }
        return responseData;
    }

    @RequestMapping("/addPage")
    public ModelAndView addStaffPage() {
        return new ModelAndView("staff/add_staff");
    }

    @RequestMapping("/addStaff")
    @ResponseBody
    public ResponseData<String> addStaff(StaffDto staffDto, HttpSession session) {
        ResponseData<String> reaponseData = null;
        GnStaffVo gnStaffVo = new GnStaffVo();
        gnStaffVo.setDepartId(staffDto.getDepartId());
        gnStaffVo.setStaffClass(staffDto.getStaffClass());
        gnStaffVo.setStaffName(staffDto.getStaffName());
        gnStaffVo.setAddress(staffDto.getAddress());
        gnStaffVo.setContactTel(staffDto.getContactTel());
        gnStaffVo.setEmail(staffDto.getEmail());
        gnStaffVo.setPostcode(staffDto.getPostcode());
        gnStaffVo.setStaffNo(staffDto.getStaffNo());
        gnStaffVo.setTenantId(OperInfoUtil.getTenantId(session));
        gnStaffVo.setPositionCode(staffDto.getPositionCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            gnStaffVo.setActiveTime(
                    new Timestamp(simpleDateFormat.parse(staffDto.getActivityDate()).getTime()));
            gnStaffVo.setInactiveTime(
                    new Timestamp(simpleDateFormat.parse(staffDto.getInactivityDate()).getTime()));
        } catch (ParseException e) {
            logger.error("转换时间失败", e);
        }
        IGnStaffMaintainSV gnStaffMaintainSV = DubboConsumerFactory
                .getService(IGnStaffMaintainSV.class);

        try {
            gnStaffMaintainSV.addStaff(gnStaffVo);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "添加成功", null);
        } catch (Exception e) {
            logger.error("添加员工失败", e);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "添加失败", null);
        }

        return reaponseData;
    }

    @RequestMapping("/detailPage")
    public ModelAndView toDetailPage(@RequestParam("staffId") String staffId, HttpSession session) {
    	ModelAndView view = new ModelAndView("staff/detail_staff");
    	GnStaffCondition condition = new GnStaffCondition();
    	condition.setStaffId(staffId);
    	condition.setTenantId(OperInfoUtil.getTenantId(session));
    	IGnStaffQuerySV gnStaffQuerySV = DubboConsumerFactory.getService(IGnStaffQuerySV.class);
    	GnStaffVo staffVo = gnStaffQuerySV.selectById(condition);
    	Gson gson = new Gson();
    	StaffDto dto = gson.fromJson(gson.toJson(staffVo), StaffDto.class);
    	dto.setStaffId(staffId);
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	dto.setActivityDate(simpleDateFormat.format(new Date(staffVo.getActiveTime().getTime())));
    	dto.setInactivityDate(
    			simpleDateFormat.format(new Date(staffVo.getInactiveTime().getTime())));
    	view.addObject("staffVo", dto);
    	return view;
    }
    @RequestMapping("/getStaffQRCode")
	public void getStaffQRCode(HttpServletRequest request, HttpServletResponse response,@RequestParam("staffId") String staffId) {
		try {
			HttpSession session=request.getSession();
			GnStaffCondition condition = new GnStaffCondition();
	    	condition.setStaffId(staffId);
	    	condition.setTenantId(OperInfoUtil.getTenantId(session));
	    	IGnStaffQuerySV gnStaffQuerySV = DubboConsumerFactory.getService(IGnStaffQuerySV.class);
	    	GnStaffVo staffVo = gnStaffQuerySV.selectById(condition);
	    	String text="2"+staffVo.getDepartId()+staffVo.getStaffId()+staffVo.getStaffName();
			QRCodeFactory.getZxingQRCodeClient().writeQRCode(response.getOutputStream(), text, 100, 100);
		} catch (Exception e) {
			logger.error("生成图片验证码错误：" + e);
			e.printStackTrace();
		}
	}
    
    @RequestMapping("/editPage")
    public ModelAndView toEditPage(@RequestParam("staffId") String staffId, HttpSession session) {
        ModelAndView view = new ModelAndView("staff/edit_staff");
        GnStaffCondition condition = new GnStaffCondition();
        condition.setStaffId(staffId);
        condition.setTenantId(OperInfoUtil.getTenantId(session));
        IGnStaffQuerySV gnStaffQuerySV = DubboConsumerFactory.getService(IGnStaffQuerySV.class);
        GnStaffVo staffVo = gnStaffQuerySV.selectById(condition);
        Gson gson = new Gson();
        StaffDto dto = gson.fromJson(gson.toJson(staffVo), StaffDto.class);
        dto.setStaffId(staffId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dto.setActivityDate(simpleDateFormat.format(new Date(staffVo.getActiveTime().getTime())));
        dto.setInactivityDate(
                simpleDateFormat.format(new Date(staffVo.getInactiveTime().getTime())));
        view.addObject("staffVo", dto);
        return view;
    }

    @RequestMapping("/editStaff")
    @ResponseBody
    public ResponseData<String> editStaff(StaffDto staffDto, HttpSession session) {
        ResponseData<String> reaponseData = null;
        if (StringUtils.isEmpty(staffDto.getStaffId())) {
            throw new BusinessException("10000", "请传入员工ID");
        }
        GnStaffCondition gnStaffCondition = new GnStaffCondition();
        gnStaffCondition.setStaffId(staffDto.getStaffId());
        gnStaffCondition.setTenantId(OperInfoUtil.getTenantId(session));
        IGnStaffQuerySV gnStaffQuerySV = DubboConsumerFactory.getService(IGnStaffQuerySV.class);
        GnStaffVo gnStaffVo = gnStaffQuerySV.selectById(gnStaffCondition);
        gnStaffVo.setDepartId(staffDto.getDepartId());
        gnStaffVo.setStaffClass(staffDto.getStaffClass());
        gnStaffVo.setAddress(staffDto.getAddress());
        gnStaffVo.setContactTel(staffDto.getContactTel());
        gnStaffVo.setEmail(staffDto.getEmail());
        gnStaffVo.setPostcode(staffDto.getPostcode());
        gnStaffVo.setPositionCode(staffDto.getPositionCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            gnStaffVo.setActiveTime(
                    new Timestamp(simpleDateFormat.parse(staffDto.getActivityDate()).getTime()));
            gnStaffVo.setInactiveTime(
                    new Timestamp(simpleDateFormat.parse(staffDto.getInactivityDate()).getTime()));
        } catch (ParseException e) {
            logger.error("转换时间失败", e);
        }
        IGnStaffMaintainSV gnStaffMaintainSV = DubboConsumerFactory
                .getService(IGnStaffMaintainSV.class);
        try {
            gnStaffMaintainSV.modifyStaff(gnStaffVo);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "修改成功", null);
        } catch (Exception e) {
            logger.error("修改员工失败", e);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "修改失败", null);
        }

        return reaponseData;
    }

    @RequestMapping(value = "/delStaff")
    @ResponseBody
    public ResponseData<String> deleteStaff(@RequestParam("staffIds[]") String[] staffIds,
            HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            List<GnStaffCondition> gnStaffConditions = new ArrayList<GnStaffCondition>();
            GnStaffCondition condition = null;
            for (String staffId : staffIds) {
                condition = new GnStaffCondition();
                condition.setStaffId(staffId);
                condition.setTenantId(OperInfoUtil.getTenantId(session));
                gnStaffConditions.add(condition);
            }
            IGnStaffMaintainSV gnStaffMaintainSV = DubboConsumerFactory
                    .getService(IGnStaffMaintainSV.class);
            gnStaffMaintainSV.deleteStaffsByIds(gnStaffConditions);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "删除成功",
                    "删除成功");
        } catch (CallerException e) {
            logger.error("删除员工失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "删除失败");
        } catch (Exception e) {
            logger.error("删除员工失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "删除失败");
        }
        return responseData;
    }

}
