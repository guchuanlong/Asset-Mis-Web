package com.myunihome.myxapp.web.business.area.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.common.api.area.interfaces.IGnAreaMaintainSV;
import com.myunihome.myxapp.common.api.area.interfaces.IGnAreaQuerySV;
import com.myunihome.myxapp.common.api.area.param.AreaLevel;
import com.myunihome.myxapp.common.api.area.param.GnAreaCondition;
import com.myunihome.myxapp.common.api.area.param.GnAreaPageCondition;
import com.myunihome.myxapp.common.api.area.param.GnAreaVo;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.framepage.util.OperInfoUtil;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.util.CollectionUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.web.business.area.model.AreaDto;

@Controller
@RequestMapping("/maintain/area")
public class AreaController {

    private static final Logger logger = LogManager.getLogger(AreaController.class);

    private static final int pageSize = 10;

    @RequestMapping("/listcity")
    @ResponseBody
    public ResponseData<List<GnAreaVo>> listCity(
            @RequestParam("parentAreaCode") String parentAreaCode, HttpSession session) {
        ResponseData<List<GnAreaVo>> responseData = null;
        try {
            IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
            List<GnAreaVo> areaVos = iGnAreaQuerySV.getCityListByProviceCode(parentAreaCode);
            responseData = new ResponseData<List<GnAreaVo>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "查询成功", areaVos);
        } catch (Exception e) {
            logger.error("查询失败", e);
            responseData = new ResponseData<List<GnAreaVo>>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage());
        }
        return responseData;
    }

    @RequestMapping("/listcounty")
    @ResponseBody
    public ResponseData<List<GnAreaVo>> listCounty(
            @RequestParam("parentAreaCode") String parentAreaCode, HttpSession session) {
        ResponseData<List<GnAreaVo>> responseData = null;
        try {
            IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
            List<GnAreaVo> areaVos = iGnAreaQuerySV.getCountyListByCityCode(parentAreaCode);
            responseData = new ResponseData<List<GnAreaVo>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "查询成功", areaVos);
        } catch (Exception e) {
            logger.error("查询失败", e);
            responseData = new ResponseData<List<GnAreaVo>>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage());
        }
        return responseData;
    }

    @RequestMapping("/liststreet")
    @ResponseBody
    public ResponseData<List<GnAreaVo>> listStreet(
            @RequestParam("parentAreaCode") String parentAreaCode, HttpSession session) {
        ResponseData<List<GnAreaVo>> responseData = null;
        try {
            IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
            List<GnAreaVo> areaVos = iGnAreaQuerySV.getStreetListByCountyCode(parentAreaCode);
            responseData = new ResponseData<List<GnAreaVo>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "查询成功", areaVos);
        } catch (Exception e) {
            logger.error("查询失败", e);
            responseData = new ResponseData<List<GnAreaVo>>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage());
        }
        return responseData;
    }

    @RequestMapping("/area_tree")
    public ModelAndView areatree_page() {
        return new ModelAndView("area/area_tree");
    }

    @RequestMapping("/listarea")
    @ResponseBody
    public List<GnAreaVo> listAreaSubNode(
            @RequestParam(value = "areaCode", required = false) String areaCode,
            @RequestParam(value = "areaLevel", required = false) String areaLevel,
            HttpSession session) {
        IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
        List<GnAreaVo> areaList = null;
        String tenantId = OperInfoUtil.getTenantId(session);
        if (StringUtils.isEmpty(areaLevel)) {
            areaLevel = "0";
        }

        if (AreaLevel.NATION_LEVEL.getLevelValue().equals(areaLevel)) {
            areaList = iGnAreaQuerySV.getProvinceList();
        } else if (AreaLevel.PROVINCE_LEVEL.getLevelValue().equals(areaLevel)) {
            areaList = iGnAreaQuerySV.getCityListByProviceCode(areaCode);
        } else if (AreaLevel.CITY_LEVEL.getLevelValue().equals(areaLevel)) {
            areaList = iGnAreaQuerySV.getCountyListByCityCode(areaCode);
        } else if (AreaLevel.COUNTY_LEVEL.getLevelValue().equals(areaLevel)) {
            areaList = iGnAreaQuerySV.getStreetListByCountyCode(areaCode);
        } else if (AreaLevel.STREET_LEVEL.getLevelValue().equals(areaLevel)) {
            areaList = iGnAreaQuerySV.getAreaListByStreetCode(tenantId, areaCode);
        }

        return areaList;
    }

    @RequestMapping("/main")
    public ModelAndView mainPage() {
        return new ModelAndView("area/area_main");
    }

    @RequestMapping("/addPage")
    public ModelAndView addDepart() {
        return new ModelAndView("area/area_form");
    }

    @RequestMapping("/editPage")
    public ModelAndView editPage(@RequestParam("areaCode") String areaCode, HttpSession session) {
        String tenantId = OperInfoUtil.getTenantId(session);
        IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
        GnAreaVo gnAreaVo = iGnAreaQuerySV.queryGnArea(tenantId, areaCode);
        AreaDto areaDto = new AreaDto();
        BeanUtils.copyProperties(gnAreaVo, areaDto);
        GnAreaVo streetArea = iGnAreaQuerySV.queryGnArea(tenantId, gnAreaVo.getParentAreaCode());
        GnAreaVo countyArea = iGnAreaQuerySV.queryGnArea(tenantId, streetArea.getParentAreaCode());
        areaDto.setCountyCode(countyArea.getAreaCode());
        areaDto.setStreetCode(streetArea.getAreaCode());
        areaDto.setParentAreaCode(streetArea.getAreaCode());
        ModelAndView view = new ModelAndView("area/area_form");
        view.addObject("areaDto", areaDto);
        return view;
    }

    @RequestMapping("/query")
    @ResponseBody
    public ResponseData<PagerResult<AreaDto>> queryDepartByPage(GnAreaPageCondition cond,
            @RequestParam("currentPage") Integer currentPage, HttpSession session) {
        ResponseData<PagerResult<AreaDto>> responseData = null;
        try {
            String tenantId = OperInfoUtil.getTenantId(session);
            cond.setTenantId(tenantId);
            cond.setPageNo(currentPage);
            cond.setPageSize(pageSize);
            IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
            PageInfo<GnAreaVo> pageInfo = iGnAreaQuerySV.getAreaListByPage(cond);
            Pager pager = new Pager(currentPage, pageInfo.getCount(), pageSize);

            PagerResult<AreaDto> pagerResult = new PagerResult<AreaDto>();
            pagerResult.setPager(pager);
            List<GnAreaVo> pageInfoList = pageInfo.getResult();
            List<AreaDto> resList = new ArrayList<AreaDto>();
            ICacheSV iCacheSV = DubboConsumerFactory.getService(ICacheSV.class);
            if (!CollectionUtil.isEmpty(pageInfoList)) {
                for (GnAreaVo areaVo : pageInfoList) {
                    AreaDto areaDto = new AreaDto();
                    BeanUtils.copyProperties(areaVo, areaDto);
                    areaDto.setProvinceName(
                            iCacheSV.getAreaName(tenantId, areaVo.getProvinceCode()));
                    areaDto.setCityName(iCacheSV.getAreaName(tenantId, areaVo.getCityCode()));
                    areaDto.setParentAreaName(
                            iCacheSV.getAreaName(tenantId, areaVo.getParentAreaCode()));
                    resList.add(areaDto);
                }
            }
            pagerResult.setResult(resList);
            responseData = new ResponseData<PagerResult<AreaDto>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "查询小区成功", pagerResult);
        } catch (Exception e) {
            logger.error("查询小区失败", e);
            responseData = new ResponseData<PagerResult<AreaDto>>(ResponseData.AJAX_STATUS_FAILURE,
                    "查询小区失败", null);
        }
        return responseData;
    }

    @RequestMapping(value = "/addArea", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> addArea(AreaDto areaDto, HttpSession session) {
        ResponseData<String> reaponseData = null;
        Gson gson = new Gson();
        areaDto.setSortId(0);
        areaDto.setAreaLevel(AreaLevel.AREA_LEVEL.getLevelValue());
        areaDto.setParentAreaCode(areaDto.getStreetCode());
        GnAreaVo gnAreaVo = gson.fromJson(gson.toJson(areaDto), GnAreaVo.class);
        gnAreaVo.setTenantId(OperInfoUtil.getTenantId(session));
        try {
            IGnAreaMaintainSV iGnAreaMaintainSV = DubboConsumerFactory
                    .getService(IGnAreaMaintainSV.class);
            String areaCode = iGnAreaMaintainSV.addArea(gnAreaVo);
            areaDto.setAreaCode(areaCode);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "添加成功",
                    areaCode);
        } catch (Exception e) {
            logger.error("添加小区失败", e);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "添加失败", null);
        }

        return reaponseData;
    }

    @RequestMapping(value = "/editArea", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> editArea(AreaDto areaDto, HttpSession session) {
        ResponseData<String> reaponseData = null;
        String tenantId = OperInfoUtil.getTenantId(session);
        IGnAreaQuerySV iGnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
        GnAreaVo areaVo = iGnAreaQuerySV.queryGnArea(tenantId, areaDto.getAreaCode());
        areaVo.setAreaName(areaDto.getAreaName());
        areaVo.setProvinceCode(areaDto.getProvinceCode());
        areaVo.setCityCode(areaDto.getCityCode());
        areaVo.setParentAreaCode(areaDto.getStreetCode());
        try {
            IGnAreaMaintainSV iGnAreaMaintainSV = DubboConsumerFactory
                    .getService(IGnAreaMaintainSV.class);
            iGnAreaMaintainSV.modifyArea(areaVo);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "修改成功", null);
        } catch (Exception e) {
            logger.error("修改小区失败", e);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "修改失败", null);
        }

        return reaponseData;
    }

    @RequestMapping(value = "/delArea")
    @ResponseBody
    public ResponseData<String> deleteArea(@RequestParam("areaCodes[]") String[] areaCodes,
            HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            List<GnAreaCondition> gnAreaConditions = new ArrayList<GnAreaCondition>();
            GnAreaCondition condition = null;

            for (String areaCode : areaCodes) {
                condition = new GnAreaCondition();
                condition.setAreaCode(areaCode);
                condition.setTenantId(OperInfoUtil.getTenantId(session));
                condition.setAreaLevel(AreaLevel.AREA_LEVEL);
                gnAreaConditions.add(condition);
            }
            IGnAreaMaintainSV iGnAreaMaintainSV = DubboConsumerFactory
                    .getService(IGnAreaMaintainSV.class);
            iGnAreaMaintainSV.deleteAreas(gnAreaConditions);

            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "删除成功",
                    "删除成功");
        } catch (CallerException e) {
            logger.error("删除失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "删除失败");
        } catch (Exception e) {
            logger.error("删除失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "删除失败");
        }
        return responseData;
    }

}
