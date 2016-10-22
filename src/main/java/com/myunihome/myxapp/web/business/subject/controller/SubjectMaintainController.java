package com.myunihome.myxapp.web.business.subject.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.myunihome.myxapp.base.exception.CallerException;
import com.myunihome.myxapp.base.exception.SystemException;
import com.myunihome.myxapp.base.vo.PageInfo;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.common.api.cache.param.SysParam;
import com.myunihome.myxapp.common.api.subjectmaintain.interfaces.IGnSubjectMaintainSV;
import com.myunihome.myxapp.common.api.subjectmaintain.param.GnSubjectCondition;
import com.myunihome.myxapp.common.api.subjectmaintain.param.GnSubjectFundVo;
import com.myunihome.myxapp.common.api.subjectmaintain.param.GnSubjectKeyParam;
import com.myunihome.myxapp.common.api.subjectmaintain.param.GnSubjectVo;
import com.myunihome.myxapp.common.api.tenant.interfaces.IGnTenantQuerySV;
import com.myunihome.myxapp.common.api.tenant.param.GnTenantConditon;
import com.myunihome.myxapp.common.api.tenant.param.GnTenantVo;
import com.myunihome.myxapp.framepage.util.OperInfoUtil;
import com.myunihome.myxapp.paas.constants.MyXAppPaaSConstant;
import com.myunihome.myxapp.paas.model.pagination.Pager;
import com.myunihome.myxapp.paas.model.pagination.PagerResult;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.util.BeanUtils;
import com.myunihome.myxapp.paas.util.CollectionUtil;
import com.myunihome.myxapp.paas.util.StringUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.web.business.subject.model.SubjectDto;
import com.myunihome.myxapp.web.system.constants.Constants;

@Controller
@RequestMapping("/maintain/subject")
public class SubjectMaintainController {
    private static final Logger logger = LogManager.getLogger(SubjectMaintainController.class);

    private static final int pageSize = 10;

    @RequestMapping("/main")
    public ModelAndView mainPage(HttpSession session) {
        ModelAndView view = new ModelAndView("subject/subject_main");
        view.addObject("tenantId", OperInfoUtil.getTenantId(session));
        return view;
    }

    /**
     * 异步初始化租户下拉列表
     */
    @RequestMapping("/listtenant")
    @ResponseBody
    public ResponseData<List<GnTenantVo>> listTenant(
            @RequestParam("industryCode") String industryCode, HttpSession session) {
        try {
            List<GnTenantVo> tenantList = DubboConsumerFactory.getService(IGnTenantQuerySV.class)
                    .getTenants();
            if (CollectionUtil.isEmpty(tenantList)) {
                return new ResponseData<List<GnTenantVo>>(ResponseData.AJAX_STATUS_FAILURE,
                        "获取租户失败：无租户");
            }
            if (StringUtil.isBlank(industryCode) || "0".equals(industryCode)) {
                return new ResponseData<List<GnTenantVo>>(ResponseData.AJAX_STATUS_SUCCESS, "查询成功",
                        tenantList);
            }
            for (int i = tenantList.size(); i > 0; i--) {
                GnTenantVo tenant = tenantList.get(i - 1);
                if (!industryCode.equals(tenant.getIndustryCode())
                        && !"0".equals(tenant.getIndustryCode())) {
                    // 移除其他行业的租户
                    tenantList.remove(i - 1);
                }
            }
            return new ResponseData<List<GnTenantVo>>(ResponseData.AJAX_STATUS_SUCCESS, "查询成功",
                    tenantList);
        } catch (CallerException e) {
            logger.error("查询租户异常", e);
            return new ResponseData<List<GnTenantVo>>(ResponseData.AJAX_STATUS_FAILURE, "查询租户异常",
                    null);
        } catch (Exception e) {
            logger.error("查询租户异常", e);
            return new ResponseData<List<GnTenantVo>>(ResponseData.AJAX_STATUS_FAILURE, "查询租户异常",
                    null);
        }
    }

    /**
     * 界面点击查询科目
     */
    @RequestMapping("/query")
    @ResponseBody
    public ResponseData<PagerResult<SubjectDto>> querySubject(GnSubjectCondition cond,
            @RequestParam("currentPage") Integer currentPage, HttpSession session) {
        ResponseData<PagerResult<SubjectDto>> response = null;
        try {

            cond.setPageSize(pageSize);
            cond.setPageNo(currentPage);
            PageInfo<GnSubjectVo> pageInfo = DubboConsumerFactory.getService(
                    IGnSubjectMaintainSV.class).querySubejct(cond);
            Pager pager = new Pager(currentPage, pageInfo.getCount(), pageSize);
            PagerResult<SubjectDto> pagerResult = new PagerResult<SubjectDto>();
            pagerResult.setPager(pager);

            if (!CollectionUtil.isEmpty(pageInfo.getResult())) {
                List<SubjectDto> toList = new ArrayList<SubjectDto>();
                ICacheSV cacheSv = DubboConsumerFactory.getService(ICacheSV.class);
                for (GnSubjectVo vo : pageInfo.getResult()) {
                    SubjectDto to = new SubjectDto();
                    BeanUtils.copyProperties(to, vo);
                    if (!StringUtil.isBlank(to.getIndustryCode())) {
                        SysParam param = cacheSv.getSysParam(Constants.ALL_TENANT, "GN_TENANT",
                                "INDUSTRY_CODE", to.getIndustryCode());
                        if (param != null) {
                            to.setIndustryName(param.getColumnDesc());
                        } else {
                            to.setIndustryName(to.getIndustryCode());
                        }
                    }
                    if (!StringUtil.isBlank(to.getSubjectType())) {
                        SysParam param = cacheSv.getSysParam(Constants.ALL_TENANT, "GN_SUBJECT",
                                "SUBJECT_TYPE", to.getSubjectType());
                        if (param != null) {
                            to.setTypeName(param.getColumnDesc());
                        } else {
                            to.setTypeName(to.getSubjectType());
                        }
                    }
                    toList.add(to);
                }
                pagerResult.setResult(toList);
            }
            response = new ResponseData<PagerResult<SubjectDto>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "查询科目成功", pagerResult);
        } catch (Exception e) {
            logger.error("查询科目失败", e);
            response = new ResponseData<PagerResult<SubjectDto>>(ResponseData.AJAX_STATUS_FAILURE,
                    "查询科目失败");
        }
        return response;
    }

    /**
     * 初始化添加科目页面
     */
    @RequestMapping("/addpage")
    public ModelAndView addSubjectPage(HttpSession session) {
        ModelAndView view = new ModelAndView("subject/subject_form");
        SubjectDto dto = new SubjectDto();
        String tenantId = OperInfoUtil.getTenantId(session);
        if (!StringUtil.isBlank(tenantId)) {
            GnTenantConditon cond = new GnTenantConditon();
            cond.setTenantId(tenantId);
            GnTenantVo tenant = DubboConsumerFactory.getService(IGnTenantQuerySV.class).getTenant(
                    cond);
            if (tenant != null) {
                dto.setIndustryCode(tenant.getIndustryCode());
                dto.setTenantId(tenant.getTenantId());
            }
        }
        view.addObject("subjectDto", dto);
        return view;
    }

    /**
     * 初始化科目编辑页面
     */
    @RequestMapping("/editpage")
    public ModelAndView editSubjectPage(@RequestParam("industryCode") String industryCode,
            @RequestParam("tenantId") String tenantId, @RequestParam("subjectId") Long subjectId,
            HttpSession session) {
        // TODO 错误页面需要定义
        // 1.检查操作人
        this.validRole(industryCode, tenantId, session);
        // 2.开始加载页面
        ModelAndView view = new ModelAndView("subject/subject_form");
        GnSubjectKeyParam key = new GnSubjectKeyParam();
        key.setIndustryCode(industryCode);
        key.setTenantId(tenantId);
        key.setSubjectId(subjectId);
        GnSubjectVo subject = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                .querySubject(key);
        SubjectDto dto = new SubjectDto();
        if (subject == null) {
            throw new SystemException("科目不存在,无法修改");
        }
        BeanUtils.copyProperties(dto, subject);
        view.addObject("subjectDto", dto);
        return view;
    }

    /**
     * 初始化资金属性编辑页面（包含添加）
     */
    @RequestMapping("/editsubjectfund")
    public ModelAndView editSubjectFundPage(@RequestParam("industryCode") String industryCode,
            @RequestParam("tenantId") String tenantId, @RequestParam("subjectId") Long subjectId,
            HttpSession session) {
        // TODO 错误页面需要定义
        // 1.检查操作人
        this.validRole(industryCode, tenantId, session);
        // 2.开始加载页面
        ModelAndView view = new ModelAndView("subject/fund_form");
        GnSubjectKeyParam key = new GnSubjectKeyParam();
        key.setIndustryCode(industryCode);
        key.setTenantId(tenantId);
        key.setSubjectId(subjectId);
        GnSubjectVo subject = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                .querySubject(key);
        if (subject == null) {
            throw new SystemException("科目不存在,无法添加属性");
        }
        GnSubjectFundVo subjectFund = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                .querySubjectFund(key);
        SubjectDto dto = new SubjectDto();
        // 2.1 先拷贝属性字段
        if (subjectFund != null) {
            BeanUtils.copyProperties(dto, subjectFund);
            dto.setFundSubjectId(subjectFund.getSubjectId());
        }
        // 2.2 后拷贝科目字段
        dto.setIndustryCode(subject.getIndustryCode());
        dto.setTenantId(subject.getTenantId());
        dto.setSubjectId(subject.getSubjectId());
        dto.setSubjectName(subject.getSubjectName());

        view.addObject("subjectDto", dto);
        return view;
    }

    /**
     * 界面输入科目ID唯一性校验
     */
    @RequestMapping("/validsubjectid")
    @ResponseBody
    public JSONObject validSujectId(@RequestParam("industryCode") String industryCode,
            @RequestParam("tenantId") String tenantId, @RequestParam("subjectId") Long subjectId,
            HttpSession session) {
        JSONObject json = new JSONObject();
        GnSubjectKeyParam key = new GnSubjectKeyParam();
        key.setIndustryCode(industryCode);
        key.setTenantId(tenantId);
        key.setSubjectId(subjectId);
        GnSubjectVo subject = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                .querySubject(key);
        if (subject == null) {
            json.put("result", true);
        } else {
            json.put("result", false);
        }
        return json;
    }

    /**
     * 添加科目
     */
    @RequestMapping(value = "/addSubject", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> addSubject(SubjectDto subject, HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            // TODO 校验参数
            // 1.检查操作人
            this.validRole(subject.getIndustryCode(), subject.getTenantId(), session);
            // 2.开始添加
            GnSubjectVo vo = new GnSubjectVo();
            BeanUtils.copyProperties(vo, subject);
            vo.setOperId(String.valueOf(OperInfoUtil.getOperId(session)));
            DubboConsumerFactory.getService(IGnSubjectMaintainSV.class).addSubject(vo);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "添加成功", "");
        } catch (CallerException e) {
            logger.error("添加失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "添加失败");
        } catch (Exception e) {
            logger.error("添加失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "删除失败");
        }
        return responseData;
    }

    /**
     * 修改科目
     */
    @RequestMapping(value = "/editSubject", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> editSubject(SubjectDto subject, HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            // TODO 校验参数
            // 1.检查操作人
            this.validRole(subject.getIndustryCode(), subject.getTenantId(), session);
            // 2.开始修改
            GnSubjectVo vo = new GnSubjectVo();
            BeanUtils.copyProperties(vo, subject);
            vo.setOperId(String.valueOf(OperInfoUtil.getOperId(session)));
            DubboConsumerFactory.getService(IGnSubjectMaintainSV.class).modifySubject(vo);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "更新成功", "");
        } catch (CallerException e) {
            logger.error("更新失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "更新失败");
        } catch (Exception e) {
            logger.error("更新失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "更新失败");
        }
        return responseData;
    }

    /**
     * 批量删除科目
     */
    @RequestMapping(value = "/delSubject", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> delSujectId(@RequestParam("industryCode[]") String[] industryCode,
            @RequestParam("tenantId[]") String[] tenantId,
            @RequestParam("subjectId[]") Long[] subjectId, HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            if (industryCode.length != tenantId.length || industryCode.length != subjectId.length) {
                throw new SystemException(MyXAppPaaSConstant.ExceptionCode.PARAM_IS_NULL, "请求参数异常");
            }
            // 1.检查操作人
            for (int i = 0; i < subjectId.length; i++) {
                this.validRole(industryCode[i], tenantId[i], session);
            }
            // 2.删除
            for (int i = 0; i < subjectId.length; i++) {
                GnSubjectKeyParam key = new GnSubjectKeyParam();
                key.setIndustryCode(industryCode[i]);
                key.setTenantId(tenantId[i]);
                key.setSubjectId(subjectId[i]);
                DubboConsumerFactory.getService(IGnSubjectMaintainSV.class).deleteSubject(key);
            }
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "删除成功", "");
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

    /**
     * 添加资金属性
     */
    @RequestMapping(value = "/addSubjectFund", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> addSubjectFund(SubjectDto subject, HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            // TODO 校验参数
            // 1.检查操作人
            this.validRole(subject.getIndustryCode(), subject.getTenantId(), session);
            // 2.开始添加
            GnSubjectFundVo vo = new GnSubjectFundVo();
            BeanUtils.copyProperties(vo, subject);
            DubboConsumerFactory.getService(IGnSubjectMaintainSV.class).addSubjectFund(vo);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "添加成功", "");
        } catch (CallerException e) {
            logger.error("添加失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "添加失败");
        } catch (Exception e) {
            logger.error("添加失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "删除失败");
        }
        return responseData;
    }

    /**
     * 修改资金属性
     */
    @RequestMapping(value = "/editSubjectFund", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> editSubjectFund(SubjectDto subject, HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            // TODO 校验参数
            // 1.检查操作人
            this.validRole(subject.getIndustryCode(), subject.getTenantId(), session);
            // 2.开始修改
            GnSubjectFundVo vo = new GnSubjectFundVo();
            BeanUtils.copyProperties(vo, subject);
            DubboConsumerFactory.getService(IGnSubjectMaintainSV.class).modifySubjectFund(vo);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "更新成功", "");
        } catch (CallerException e) {
            logger.error("更新失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "更新失败");
        } catch (Exception e) {
            logger.error("更新失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "更新失败");
        }
        return responseData;
    }

    /**
     * 初始化复制科目页面
     */
    @RequestMapping(value = "/copypage", method = RequestMethod.POST)
    public ModelAndView copySubjectPage(@RequestParam("industryCode") String[] industryCode,
            @RequestParam("tenantId") String[] tenantId,
            @RequestParam("subjectId") Long[] subjectId, HttpSession session) {

        ModelAndView view = new ModelAndView("subject/copy_form");
        String optTenantId = OperInfoUtil.getTenantId(session);
        if (StringUtil.isBlank(optTenantId)) {
            throw new SystemException("获取用户Session失败");
        }
        GnTenantConditon cond = new GnTenantConditon();
        cond.setTenantId(optTenantId);
        GnTenantVo tenant = DubboConsumerFactory.getService(IGnTenantQuerySV.class).getTenant(cond);
        if (tenant == null) {
            throw new SystemException("获取租户失败");
        }
        try {
            if (industryCode.length != tenantId.length || industryCode.length != subjectId.length) {
                throw new CallerException(MyXAppPaaSConstant.ExceptionCode.PARAM_IS_NULL, "请求参数异常");
            }
            ICacheSV cacheSv = DubboConsumerFactory.getService(ICacheSV.class);
            JSONArray data = new JSONArray();
            GnSubjectKeyParam key = new GnSubjectKeyParam();
            for (int i = 0; i < subjectId.length; i++) {
                // 1.查询被复制的科目
                key.setIndustryCode(industryCode[i]);
                key.setTenantId(tenantId[i]);
                key.setSubjectId(subjectId[i]);
                GnSubjectVo oriSubject = DubboConsumerFactory
                        .getService(IGnSubjectMaintainSV.class).querySubject(key);
                SubjectDto dto = new SubjectDto();
                BeanUtils.copyProperties(dto, oriSubject);
                if (!StringUtil.isBlank(oriSubject.getIndustryCode())) {
                    SysParam param = cacheSv.getSysParam(Constants.ALL_TENANT, "GN_TENANT",
                            "INDUSTRY_CODE", oriSubject.getIndustryCode());
                    if (param != null) {
                        dto.setIndustryName(param.getColumnDesc());
                    } else {
                        dto.setIndustryName(dto.getIndustryCode());
                    }
                }
                JSONObject oriJson = JSON.parseObject(JSON.toJSONString(dto));
                // 2.检查被复制的科目是否冲突
                key.setIndustryCode(tenant.getIndustryCode());
                key.setTenantId(tenant.getTenantId());
                key.setSubjectId(subjectId[i]);
                GnSubjectVo subject = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                        .querySubject(key);
                if (subject != null) {
                    oriJson.put("copyValidStatus", false);
                    oriJson.put("copyValidMag", "ID已存在");
                } else {
                    oriJson.put("copyValidStatus", true);
                    oriJson.put("copyValidMag", "可复制");
                }
                data.add(oriJson);
            }
            view.addObject("subjectDto", data);
        } catch (Exception e) {
            logger.error("复制检测失败", e);
            // TODO
        }
        view.addObject("industryCode", tenant.getIndustryCode());
        view.addObject("tenantId", tenant.getTenantId());
        return view;
    }

    /**
     * 批量复制科目
     */
    @RequestMapping(value = "/copySubject", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData<String> copySujectId(@RequestParam("industryCode[]") String[] industryCode,
            @RequestParam("tenantId[]") String[] tenantId,
            @RequestParam("subjectId[]") Long[] subjectId,
            @RequestParam("toIndustryCode") String toIndustryCode,
            @RequestParam("toTenantId") String toTenantId, HttpSession session) {
        ResponseData<String> responseData = null;
        try {
            this.validRole(toIndustryCode, toTenantId, session);
            String operId = String.valueOf(OperInfoUtil.getOperId(session));
            if (industryCode.length != tenantId.length || industryCode.length != subjectId.length) {
                throw new CallerException(MyXAppPaaSConstant.ExceptionCode.PARAM_IS_NULL, "请求参数异常");
            }
            GnSubjectKeyParam key = new GnSubjectKeyParam();
            // 1.检查被复制的科目是否冲突
            for (int i = 0; i < subjectId.length; i++) {
                key.setIndustryCode(toIndustryCode);
                key.setTenantId(toTenantId);
                key.setSubjectId(subjectId[i]);
                GnSubjectVo subject = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                        .querySubject(key);
                if (subject != null) {
                    throw new SystemException("检测到科目ID冲突");
                }
            }
            //2.复制
            for (int i = 0; i < subjectId.length; i++) {
                key.setIndustryCode(industryCode[i]);
                key.setTenantId(tenantId[i]);
                key.setSubjectId(subjectId[i]);
                //2.1 复制subject
                GnSubjectVo subject = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                        .querySubject(key);
                if(subject==null){
                    throw new SystemException("复制科目原科目不存在");
                }
                subject.setIndustryCode(toIndustryCode);
                subject.setTenantId(toTenantId);
                subject.setOperId(operId);
                DubboConsumerFactory.getService(IGnSubjectMaintainSV.class).addSubject(subject);
                //2.2 复制subjectFund
                GnSubjectFundVo subjectFund = DubboConsumerFactory.getService(IGnSubjectMaintainSV.class)
                        .querySubjectFund(key);
                if(subjectFund!=null){
                    subjectFund.setIndustryCode(toIndustryCode);
                    subjectFund.setTenantId(toTenantId);
                    DubboConsumerFactory.getService(IGnSubjectMaintainSV.class).addSubjectFund(subjectFund);
                }
                
            }
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "复制成功", "");
        } catch (CallerException e) {
            logger.error("复制失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "复制失败");
        } catch (Exception e) {
            logger.error("复制失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    e.getMessage(), "复制失败");
        }
        return responseData;
    }

    private void validRole(String industryCode, String tenantId, HttpSession session)
            throws SystemException {

        String operTenantId = OperInfoUtil.getTenantId(session);
        if (StringUtil.isBlank(operTenantId)) {
            throw new SystemException("获取Session失败");
        }
        GnTenantConditon cond = new GnTenantConditon();
        cond.setTenantId(tenantId);
        GnTenantVo tenant = DubboConsumerFactory.getService(IGnTenantQuerySV.class).getTenant(cond);
        if (tenant == null || !tenant.getIndustryCode().equals(industryCode)
                || !operTenantId.equals(tenantId)) {
            throw new SystemException("您无权操作该科目");
        }
    }
}
