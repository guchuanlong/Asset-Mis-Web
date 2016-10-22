package com.myunihome.myxapp.framepage.controller;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myunihome.myxapp.base.exception.SystemException;
import com.myunihome.myxapp.framepage.util.OperInfoUtil;
import com.myunihome.myxapp.paas.model.pagination.ResponseData;
import com.myunihome.myxapp.paas.util.StringUtil;
import com.myunihome.myxapp.sys.api.framepage.interfaces.IProviderForFramePageSV;
import com.myunihome.myxapp.sys.api.framepage.param.Menu;
import com.myunihome.myxapp.sys.api.framepage.param.MenuQuery;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;

@RestController
@RequestMapping("/framepage")
public class FramePageController {
	private static final Log LOG = LogFactory.getLog(FramePageController.class);
    /**
     * 预览模板
     * 
     * @param path
     * @return
     * @author zhangchao
     */
    @RequestMapping("/view")
    public ModelAndView view(String path) {
        if (StringUtil.isBlank(path)) {
            throw new SystemException("模板加载出错，请输入模板路径,如:template/bis/default");
        }
        return new ModelAndView(path + "/template");
    }
    
    @RequestMapping("/viewclc")
    public ModelAndView viewclc() {
        return new ModelAndView("/template/clc/default/template");
    }
    
    @RequestMapping("/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		UserLoginVo user = (UserLoginVo) session.getAttribute(OperInfoUtil.USER_SESSION_KEY);
		if(user!=null){
			try {
				session.removeAttribute(OperInfoUtil.USER_SESSION_KEY);
				ResourceBundle rb = ResourceBundle.getBundle("sso");
				String logOutServerUrl = rb.getString("logOutServerUrl");
				String logOutBackUrl = rb.getString("logOutBackUrl");
				//response.sendRedirect(logOutServerUrl + "?service=" + logOutBackUrl);
				response.sendRedirect(logOutServerUrl + "?service=" + logOutBackUrl+"?tenantId="+user.getTenantId());
				session.invalidate();
			} catch (IOException e) {
				LOG.error("用户登出失败",e);
			}
		}
	}
	
    /**
     * 获取当前登录用户归属的租户信息
     * 
     * @param request
     * @return
     * @author zhangchao
     */
    @RequestMapping("/getCurrentLoginUser")
    public ResponseData<UserLoginVo> getCurrentLoginUser(HttpServletRequest request) {
        ResponseData<UserLoginVo> responseData = null;
        try {
            UserLoginVo user = OperInfoUtil.getCurrentLoginUser(request.getSession());
            responseData = new ResponseData<UserLoginVo>(ResponseData.AJAX_STATUS_SUCCESS,
                    "获取当前登录用户信息成功", user);
        } catch (Exception e) {
        	LOG.error("获取当前登录用户信息失败:"+e.getMessage(),e);
            responseData = new ResponseData<UserLoginVo>(ResponseData.AJAX_STATUS_FAILURE,
                    "获取当前登录用户信息失败,请联系管理员");
        }
        return responseData;
    }

    @RequestMapping("/getMenusByLimited")
    public ResponseData<List<Menu>> getMenusByLimited(HttpServletRequest request) {
        ResponseData<List<Menu>> responseData = null;
        try {
            String systemId ="";
            String systemModeId="";
            UserLoginVo user = OperInfoUtil.getCurrentLoginUser(request.getSession());
            MenuQuery query = new MenuQuery();
            query.setTenantId(user.getTenantId());
            query.setOperId(user.getOperId());
            query.setSystemId(systemId);
            query.setSystemModeId(systemModeId);
            List<Menu> menus = DubboConsumerFactory.getService(IProviderForFramePageSV.class)
             .queryFirstLevelMenusByLimited(query);
             
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "加载一级菜单成功", menus);
        } catch (Exception e) {
            //e.printStackTrace();
        	LOG.error("加载一级菜单失败:"+e.getMessage(),e);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_FAILURE,
                    "加载一级菜单失败:"+e.getMessage());
        }
        return responseData;
    }

    /**
     * 获取菜单信息
     * 
     * @param request
     * @return
     * @author zhangchao
     */
    @RequestMapping("/getFirstLevelMenusByLimited")
    public ResponseData<List<Menu>> getFirstLevelMenusByLimited(HttpServletRequest request) {
        ResponseData<List<Menu>> responseData = null;
        try {
        	String systemId ="";
            String systemModeId="";
            UserLoginVo user = OperInfoUtil.getCurrentLoginUser(request.getSession());
            MenuQuery query = new MenuQuery();
            query.setTenantId(user.getTenantId());
            query.setOperId(user.getOperId());
            query.setSystemId(systemId);
            query.setSystemModeId(systemModeId);
            List<Menu> menus = DubboConsumerFactory.getService(IProviderForFramePageSV.class)
                    .queryFirstLevelMenusByLimited(query);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "加载一级菜单成功", menus);
        } catch (Exception e) {
        	LOG.error("加载一级菜单失败:"+e.getMessage(),e);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_FAILURE,
                    "加载一级菜单失败,请联系管理员");
        }
        return responseData;
    }

    /**
     * 加载子菜单数据
     * 
     * @param request
     * @return
     * @author zhangchao
     */
    @RequestMapping("/getSubMenusByLimited")
    public ResponseData<List<Menu>> getSubMenusByLimited(HttpServletRequest request) {
        ResponseData<List<Menu>> responseData = null;
        try {
        	String menuPid = request.getParameter("menuPid");
        	if (StringUtil.isBlank(menuPid)) {
        		menuPid="0";
        		//throw new SystemException("加载菜单数据失败:请指定上级菜单标识");
        	}
        	String systemId ="";
            String systemModeId="";
            UserLoginVo user = OperInfoUtil.getCurrentLoginUser(request.getSession());
            MenuQuery query = new MenuQuery();
            query.setTenantId(user.getTenantId());
            query.setOperId(user.getOperId());
            query.setSystemId(systemId);
            query.setSystemModeId(systemModeId);
            query.setMenuPid(Long.parseLong(menuPid));
            List<Menu> menus = DubboConsumerFactory.getService(IProviderForFramePageSV.class)
                    .querySubMenusByLimited(query);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "加载子菜单成功", menus);
        } catch (Exception e) {
        	LOG.error("加载子菜单失败:"+e.getMessage(),e);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_FAILURE,
                    "加载子菜单失败,请联系管理员");
        }
        return responseData;
    }
    
    /**
     * 获取租户的所有一级菜单信息
     * 
     * @param request
     * @return
     * @author gucl
     */
    @RequestMapping("/getFirstLevelMenusByLimitedAll")
    public ResponseData<List<Menu>> getFirstLevelMenusByLimitedAll(HttpServletRequest request) {
        ResponseData<List<Menu>> responseData = null;
        try {
        	
            //String systemId =framePageConf.getSystemId();
            //String systemModeId=framePageConf.getSystemModeId();
            UserLoginVo user = OperInfoUtil.getCurrentLoginUser(request.getSession());
            MenuQuery query = new MenuQuery();
            query.setTenantId(user.getTenantId());
            query.setOperId(user.getOperId());
            //query.setSystemId(systemId);
            //query.setSystemModeId(systemModeId);
            List<Menu> menus = DubboConsumerFactory.getService(IProviderForFramePageSV.class)
                    .queryFirstLevelMenusByLimited(query);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "加载一级菜单成功", menus);
        } catch (Exception e) {
        	LOG.error("加载一级菜单失败:"+e.getMessage(),e);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_FAILURE,
                    "加载一级菜单失败,请联系管理员");
        }
        return responseData;
    }

    /**
     * 加载租户下指定父级菜单的子菜单数据
     * 
     * @param request
     * @return
     * @author gucl
     */
    @RequestMapping("/getSubMenusByLimitedAll")
    public ResponseData<List<Menu>> getSubMenusByLimitedAll(HttpServletRequest request) {
        ResponseData<List<Menu>> responseData = null;
        try {
        	String menuPid = request.getParameter("menuPid");
        	if (StringUtil.isBlank(menuPid)) {
        		menuPid="0";
        		//throw new SystemException("加载菜单数据失败:请指定上级菜单标识");
        	}
            UserLoginVo user = OperInfoUtil.getCurrentLoginUser(request.getSession());
            MenuQuery query = new MenuQuery();
            query.setTenantId(user.getTenantId());
            query.setOperId(user.getOperId());
            query.setMenuPid(Long.parseLong(menuPid));
            List<Menu> menus = DubboConsumerFactory.getService(IProviderForFramePageSV.class)
                    .querySubMenusByLimited(query);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_SUCCESS,
                    "加载子菜单成功", menus);
        } catch (Exception e) {
        	LOG.error("加载子菜单失败:"+e.getMessage(),e);
            responseData = new ResponseData<List<Menu>>(ResponseData.AJAX_STATUS_FAILURE,
                    "加载子菜单失败,请联系管理员");
        }
        return responseData;
    }

}
