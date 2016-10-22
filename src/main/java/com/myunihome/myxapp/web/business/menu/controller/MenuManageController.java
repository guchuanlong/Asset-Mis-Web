package com.myunihome.myxapp.web.business.menu.controller;

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
import com.myunihome.myxapp.sys.api.sysmenu.interfaces.IGnMenuManageSV;
import com.myunihome.myxapp.sys.api.sysmenu.param.GnMenuParamsVO;
import com.myunihome.myxapp.sys.api.sysmenu.param.GnMenuResponseVO;
import com.myunihome.myxapp.sys.api.sysmenu.param.GnMenuVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.system.constants.Constants;
import com.myunihome.myxapp.web.system.util.DateTimeUtil;

/**
 * 菜单管理
 * Date: 2015年9月23日 <br>
 * Copyright (c) 2015 asiainfo.com <br>
 * 
 * @author liangbs
 */
@Controller
@RequestMapping(value="/menuManage")
public class MenuManageController {
    
    private static final Logger LOGGER = Logger.getLogger(MenuManageController.class);
    
    /**
	 * 分页大小
	 */
    private static final int PAGESIZE = 5;

    /**
     * 跳转页面
     */
	@RequestMapping(value="/toMenuList")
	public ModelAndView toMenuList(HttpServletRequest request, HttpServletResponse response) {
	    
	    UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
	    request.setAttribute("tenantId", user.getTenantId());//租户ID
	    
		return new ModelAndView("menu/menuList");
	}
	
	/**
	 * 查询列表
	 * @param condition 菜单查询条件
	 * @param currentPage 请求页码
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/queryMeunList")
	public ResponseData<PagerResult<GnMenuVO>> queryOrderAuditList(HttpServletRequest request, GnMenuParamsVO gnMenuParamsVO, 
	                @RequestParam("currentPage") Integer currentPage){
	    
	    ResponseData<PagerResult<GnMenuVO>> responseData = null;
	    try {
	        
	        UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
	        
	        gnMenuParamsVO.setTenantId(user.getTenantId());                     //租户ID
	        Timestamp currTimestamp = DateTimeUtil.getCurrTimestamp();
	        //gnMenuParamsVO.setActiveTime(currTimestamp);
	        gnMenuParamsVO.setInactiveTime(currTimestamp);
	        
	        PageInfo<GnMenuVO> pageInfo = new PageInfo<GnMenuVO>();
	        pageInfo.setPageNo(currentPage);
	        pageInfo.setPageSize(PAGESIZE);
	        gnMenuParamsVO.setPageInfo(pageInfo);                               //分页信息
	        
	        IGnMenuManageSV gnMenuManageSV = DubboConsumerFactory.getService("gnMenuManageSV");
	        PageInfo<GnMenuVO> results = gnMenuManageSV.queryMenu(gnMenuParamsVO);
	        
	        PagerResult<GnMenuVO> pagerResult = new PagerResult<GnMenuVO>();
	        Pager pager = new Pager(currentPage, results!=null?results.getCount():0, PAGESIZE);
	        pagerResult.setPager(pager);
	        pagerResult.setResult(results!=null?results.getResult():null);
	        
	        responseData = new ResponseData<PagerResult<GnMenuVO>>(ResponseData.AJAX_STATUS_SUCCESS, "菜单列表查询成功", pagerResult);
	    }catch (CallerException e) {
	        LOGGER.error("菜单列表查询失败，错误信息：", e);
            responseData = new ResponseData<PagerResult<GnMenuVO>>(ResponseData.AJAX_STATUS_FAILURE, "菜单列表查询失败", null);
        }
        return responseData;
	}
	
	/**
	 * 获取单条数据
	 * @param request
	 * @param gnMenuParamsVO
	 * @param currentPage
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping("/getMenuData")
	public ResponseData<GnMenuVO> getMenuData(HttpServletRequest request, @RequestParam("menuId") Long menuId){
	    
	    ResponseData<GnMenuVO> responseData = null;
	    try {
	        
	        GnMenuVO result = new GnMenuVO();
	        if(menuId != null){
	            UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
	            GnMenuParamsVO gnMenuParamsVO = new GnMenuParamsVO();
	            gnMenuParamsVO.setTenantId(user.getTenantId());
	            gnMenuParamsVO.setMenuId(menuId);
	            
	            IGnMenuManageSV gnMenuManageSV = DubboConsumerFactory.getService("gnMenuManageSV");
	            result = gnMenuManageSV.queryMenuById(gnMenuParamsVO);
	        }
	        
	        responseData = new ResponseData<GnMenuVO>(ResponseData.AJAX_STATUS_SUCCESS, "菜单查询成功", result);
	    }catch (CallerException e) {
	        LOGGER.error("菜单查询失败，错误信息：", e);
	        responseData = new ResponseData<GnMenuVO>(ResponseData.AJAX_STATUS_FAILURE, "菜单查询失败", null);
	    }
	    return responseData;
	}
	
	/**
	 * 菜单新增或更新
	 * @param request
	 * @param gnMenuParamsVO
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value="/addOrUpdateMenu", method=RequestMethod.POST)
	public ResponseData<String> addOrUpdateMenu(HttpServletRequest request, GnMenuParamsVO gnMenuParamsVO
	        , @RequestParam("activeTimeStr") String activeTimeStr, @RequestParam("inactiveTimeStr") String inactiveTimeStr){
	    
	    ResponseData<String> responseData = null;
        try {
            UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
            
            Long menuId = null;
            if(gnMenuParamsVO != null){
                
                menuId = gnMenuParamsVO.getMenuId();
                gnMenuParamsVO.setSystemId(Constants.SYSTEM_ID);
                gnMenuParamsVO.setSystemModeId(Constants.SYSTEM_MODEL_ID);
                if(menuId != null){
                    //更新操作
                    responseData = modifyMenuData(gnMenuParamsVO, activeTimeStr, inactiveTimeStr, user);
                }else{
                    //新增操作
                    responseData = addMenuData(gnMenuParamsVO, activeTimeStr, inactiveTimeStr, user);
                }
            }else{
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "菜单操作失败", "参数为空");
            }
        }catch (Exception e) {
            LOGGER.error("菜单创建失败，错误信息：", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "菜单操作失败", "操作异常");
        }
        return responseData;
	}

	/**
	 * 新增操作
	 * @param gnMenuParamsVO
	 * @param activeTimeStr
	 * @param inactiveTimeStr
	 * @param user
	 * @return
	 * @throws Exception
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private ResponseData<String> addMenuData(GnMenuParamsVO gnMenuParamsVO, String activeTimeStr, String inactiveTimeStr, UserLoginVo user) throws Exception {
		ResponseData<String> responseData;
		Long menuId;
		gnMenuParamsVO.setTenantId(user.getTenantId());
		gnMenuParamsVO.setCreateOperId(user.getOperId());
		gnMenuParamsVO.setActiveTime(DateTimeUtil.stringToTimstamp(activeTimeStr));
		gnMenuParamsVO.setInactiveTime(DateTimeUtil.stringToTimstamp(inactiveTimeStr));
		
		IGnMenuManageSV gnMenuManageSV = DubboConsumerFactory.getService("gnMenuManageSV");
		menuId = gnMenuManageSV.addMenu(gnMenuParamsVO);
		
		if(menuId != null){
		    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "菜单创建成功", menuId.toString());
		}else{
		    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "菜单创建失败", null);
		}
		return responseData;
	}

	/**
	 * 修改操作
	 * @param gnMenuParamsVO
	 * @param activeTimeStr
	 * @param inactiveTimeStr
	 * @param user
	 * @return
	 * @throws Exception
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	private ResponseData<String> modifyMenuData(GnMenuParamsVO gnMenuParamsVO, String activeTimeStr, String inactiveTimeStr, UserLoginVo user) throws Exception {
		ResponseData<String> responseData;
		GnMenuResponseVO result;
		gnMenuParamsVO.setTenantId(user.getTenantId());
		gnMenuParamsVO.setUpdateOperId(user.getOperId());
		gnMenuParamsVO.setActiveTime(DateTimeUtil.stringToTimstamp(activeTimeStr));
		gnMenuParamsVO.setInactiveTime(DateTimeUtil.stringToTimstamp(inactiveTimeStr));
		IGnMenuManageSV gnMenuManageSV = DubboConsumerFactory.getService("gnMenuManageSV");
		result = gnMenuManageSV.updateMenu(gnMenuParamsVO);
		
		if("1".equals(result.responseCode)){
		    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "菜单更新成功", result.respondeDesc);
		}else{
		    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "菜单更新失败", result.respondeDesc);
		}
		return responseData;
	}
	
	/**
	 * 菜单删除操作
	 * @param request
	 * @param gnMenuParamsVO
	 * @param activeTimeStr
	 * @param inactiveTimeStr
	 * @return
	 * @author liangbs
	 * @ApiDocMethod
	 */
	@ResponseBody
	@RequestMapping(value="/delMenu", method=RequestMethod.POST)
	public ResponseData<String> delMenu(HttpServletRequest request, GnMenuParamsVO gnMenuParamsVO){
	    
	    ResponseData<String> responseData = null;
	    try {
	        UserLoginVo user = (UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
	        
	        GnMenuResponseVO result = null;
	            
	        Long menuId = gnMenuParamsVO.getMenuId();
            if(menuId != null){
                
                gnMenuParamsVO.setTenantId(user.getTenantId());
                IGnMenuManageSV gnMenuManageSV = DubboConsumerFactory.getService("gnMenuManageSV");
                result = gnMenuManageSV.delMenuByID(gnMenuParamsVO);
                
                if("1".equals(result.responseCode)){
                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "菜单删除成功", result.respondeDesc);
                }else{
                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "菜单删除失败", result.respondeDesc);
                }
                
	        }else{
	            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "菜单删除失败", "参数为空");
	        }
	    }catch (Exception e) {
	        LOGGER.error("菜单创建失败，错误信息：", e);
	        responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "菜单删除失败", "操作异常");
	    }
	    return responseData;
	}
	
}