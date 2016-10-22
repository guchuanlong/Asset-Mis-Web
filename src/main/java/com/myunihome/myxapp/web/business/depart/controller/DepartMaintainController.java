package com.myunihome.myxapp.web.business.depart.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.common.api.depart.interfaces.IGnDepartMaintainSV;
import com.myunihome.myxapp.common.api.depart.interfaces.IGnDepartQuerySV;
import com.myunihome.myxapp.common.api.depart.param.DepartPaginationResult;
import com.myunihome.myxapp.common.api.depart.param.GnDepartCondition;
import com.myunihome.myxapp.common.api.depart.param.GnDepartVo;
import com.myunihome.myxapp.framepage.util.OperInfoUtil;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerHelper;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.util.StringUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.web.business.depart.model.DepartCondition;
import com.myunihome.myxapp.web.business.depart.model.DepartDto;

@Controller
@RequestMapping("/maintain/depart")
public class DepartMaintainController {
    private static final Logger logger = LogManager.getLogger(DepartMaintainController.class);

    private static final int pageSize = 10;

    @RequestMapping("/tree")
    public ModelAndView tree_page() {
        return new ModelAndView("depart/depart_tree");
    }

    @RequestMapping("/listsubnode")
    @ResponseBody
    public List<GnDepartVo> listSubNode(
            @RequestParam(value = "departId", required = false) String departId,
            HttpSession session) {
        IGnDepartQuerySV gnDepartQuerySV = DubboConsumerFactory.getService(IGnDepartQuerySV.class);
        if (StringUtils.isEmpty(departId)) {
            departId = "0";
        }
        GnDepartCondition condition = new GnDepartCondition();
        condition.setTenantId(OperInfoUtil.getTenantId(session));
        condition.setDepartId(departId);
        return gnDepartQuerySV.select(condition);
    }

    @RequestMapping("/addPage")
    public ModelAndView addDepart() {
        return new ModelAndView("depart/add_depart");
    }

    @RequestMapping("/main")
    public ModelAndView mainPage() {
        return new ModelAndView("depart/depart_main");
    }

    @RequestMapping("/editPage")
    public ModelAndView editPage(@RequestParam("departId") String departId, HttpSession session) {
        GnDepartCondition condition = new GnDepartCondition();
        condition.setDepartId(departId);
        condition.setTenantId(OperInfoUtil.getTenantId(session));
        IGnDepartQuerySV gnDepartQuerySV = DubboConsumerFactory.getService(IGnDepartQuerySV.class);
        GnDepartVo gnDepartVo = gnDepartQuerySV.selectById(condition);
        if ("0".equals(gnDepartVo.getParentDepartName())) {

            gnDepartVo.setParentDepartName("");
        }
        ModelAndView view = new ModelAndView("depart/edit_depart");
        view.addObject("gnDepartVo", gnDepartVo);
        return view;
    }

    @RequestMapping("/query")
    @ResponseBody
    public ResponseData<PagerResult<GnDepartVo>> queryDepartByPage(DepartCondition condition,
            @RequestParam("currentPage") Integer currentPage, HttpSession session) {
        ResponseData<PagerResult<GnDepartVo>> responseData = null;
        try {
            GnDepartCondition searchCondition = new GnDepartCondition();
            searchCondition.setTenantId(OperInfoUtil.getTenantId(session));
            searchCondition.setDepartId(condition.getParentDepartId());
            searchCondition.setDepartName(condition.getDepartName());
            IGnDepartQuerySV gnDepartQuerySV = DubboConsumerFactory
                    .getService(IGnDepartQuerySV.class);
            DepartPaginationResult result = gnDepartQuerySV.selectByPage(searchCondition,
                    PagerHelper.getStartRow(currentPage, pageSize), pageSize);

            Pager pager = new Pager(currentPage, result.getTotal(), pageSize);
            // 翻译省份、地市
            ICacheSV cacheSV = DubboConsumerFactory.getService(ICacheSV.class);
            List<GnDepartVo> info = result.getResult();
            if (info != null && info.size() > 0) {
                for (int i = 0; i < info.size(); i++) {
                    if (!StringUtil.isBlank(info.get(i).getProvinceCode())) {
                        String provinceName = cacheSV.getAreaName(OperInfoUtil.getTenantId(session),
                                info.get(i).getProvinceCode());
                        info.get(i).setProvinceCode(provinceName);
                    }
                    if (!StringUtil.isBlank(info.get(i).getCityCode())) {
                        if ("000".equals(info.get(i).getCityCode())) {

                            info.get(i).setCityCode("00");
                        }
                        String cityName = cacheSV.getAreaName(OperInfoUtil.getTenantId(session),
                                info.get(i).getCityCode());
                        info.get(i).setCityCode(cityName);
                    }
                }
            }

            PagerResult<GnDepartVo> pagerResult = new PagerResult<GnDepartVo>();
            pagerResult.setPager(pager);
            pagerResult.setResult(result.getResult());
            responseData = new ResponseData<PagerResult<GnDepartVo>>(
                    ResponseData.AJAX_STATUS_SUCCESS, "查询部门成功", pagerResult);
        } catch (Exception e) {
            logger.error("查询部门失败", e);
            responseData = new ResponseData<PagerResult<GnDepartVo>>(
                    ResponseData.AJAX_STATUS_FAILURE, "查询部门失败", null);
        }
        return responseData;
    }

    @RequestMapping("/addDepart")
    @ResponseBody
    public ResponseData<String> addDepart(DepartDto departDto, HttpSession session) {
        Gson gson = new Gson();
        ResponseData<String> reaponseData = null;
        GnDepartVo gnDepartVo = gson.fromJson(gson.toJson(departDto), GnDepartVo.class);
        gnDepartVo.setTenantId(OperInfoUtil.getTenantId(session));
        if ("00".equals(departDto.getProvinceCode())) {
            gnDepartVo.setAreaCode("00");
        } else if ("000".equals(departDto.getCityCode())) {
            gnDepartVo.setAreaCode(departDto.getProvinceCode());
        } else if ("0000".equals(departDto.getAreaCode())) {
            gnDepartVo.setAreaCode(departDto.getCityCode());
        } else {
            gnDepartVo.setAreaCode(departDto.getAreaCode());
        }
        IGnDepartMaintainSV gnDepartMaintainSV = DubboConsumerFactory
                .getService(IGnDepartMaintainSV.class);
        try {
            gnDepartMaintainSV.addDepart(gnDepartVo);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "添加成功", null);
        } catch (Exception e) {
            logger.error("添加部门失败", e);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "添加失败", null);
        }

        return reaponseData;
    }

    @RequestMapping("/editDepart")
    @ResponseBody
    public ResponseData<String> editDepart(GnDepartVo departDto, HttpSession session) {
        ResponseData<String> reaponseData = null;
        GnDepartCondition gnDepartCondition = new GnDepartCondition();
        gnDepartCondition.setDepartId(departDto.getDepartId());
        gnDepartCondition.setTenantId(OperInfoUtil.getTenantId(session));
        IGnDepartQuerySV gnDepartQuerySV = DubboConsumerFactory.getService(IGnDepartQuerySV.class);
        GnDepartVo gnDepartVo = gnDepartQuerySV.selectById(gnDepartCondition);
        gnDepartVo.setDepartId(departDto.getDepartId());
        gnDepartVo.setAddress(departDto.getAddress());
        gnDepartVo.setCityCode(departDto.getCityCode());
        gnDepartVo.setProvinceCode(departDto.getProvinceCode());
        gnDepartVo.setPostcode(departDto.getPostcode());
        gnDepartVo.setContactName(departDto.getContactName());
        gnDepartVo.setContactTel(departDto.getContactTel());
        gnDepartVo.setDepartKindType(departDto.getDepartKindType());
        gnDepartVo.setParentDepartName(departDto.getDepartName());
        gnDepartVo.setParentDepartId(departDto.getParentDepartId());
        gnDepartVo.setDepartName(departDto.getDepartName());
        if ("00".equals(departDto.getProvinceCode())) {
            gnDepartVo.setAreaCode("00");
        } else if ("000".equals(departDto.getCityCode())) {
            gnDepartVo.setAreaCode(departDto.getProvinceCode());
        } else if ("0000".equals(departDto.getAreaCode())) {
            gnDepartVo.setAreaCode(departDto.getCityCode());
        } else {
            gnDepartVo.setAreaCode(departDto.getAreaCode());
        }
        IGnDepartMaintainSV gnDepartMaintainSV = DubboConsumerFactory
                .getService(IGnDepartMaintainSV.class);

        try {
            gnDepartMaintainSV.modifyDepart(gnDepartVo);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "修改成功", null);
        } catch (Exception e) {
            logger.error("修改部门失败", e);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "修改失败", null);
        }
        return reaponseData;
    }

    @RequestMapping(value = "/delDepart")
    @ResponseBody
    public ResponseData<String> deleteDepart(@RequestParam("departIds[]") String[] departIds,
            HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            List<GnDepartCondition> gnDepartConditions = new ArrayList<GnDepartCondition>();
            GnDepartCondition condition = null;

            for (String staffId : departIds) {
                condition = new GnDepartCondition();
                condition.setDepartId(staffId);
                condition.setTenantId(OperInfoUtil.getTenantId(session));
                gnDepartConditions.add(condition);
            }
            IGnDepartMaintainSV gnDepartMaintainSV = DubboConsumerFactory
                    .getService(IGnDepartMaintainSV.class);
            gnDepartMaintainSV.deleteDeparts(gnDepartConditions);

            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "删除成功",
                    "删除成功");
        } catch (CallerException e) {
            logger.error("删除失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "删除失败");
        } catch (Exception e) {
            logger.error("删除失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    "该部门底下存在子部门,不能被删除", "删除失败");
        }
        return responseData;
    }
}
