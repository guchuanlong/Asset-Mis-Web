package com.myunihome.myxapp.web.business.common.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myunihome.myxapp.sso.api.menu.interfaces.IGetMenuByOperidSV;
import com.myunihome.myxapp.sso.api.menu.param.GetMenuByOperIDVO;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.business.common.model.Menu;
import com.myunihome.myxapp.web.business.common.model.MenuVo;
import com.myunihome.myxapp.web.system.constants.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author zhangbc
 *
 */
@Controller
@RequestMapping(value="/menu")
public class MenuController {
	 private static final Logger LOGGER = Logger.getLogger(MenuController.class);
	
       @RequestMapping(value="/getMenu")	
        public void  getMenu(HttpServletRequest request,HttpServletResponse response)
        {
    	   IGetMenuByOperidSV getMenuByOperidSV=DubboConsumerFactory.getService("getMenuByOperidSV");
    	   UserLoginVo userLoginVo=(UserLoginVo) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
    	   List<GetMenuByOperIDVO> list=getMenuByOperidSV.getMenuByOperID(userLoginVo.getStaffnoId(), userLoginVo.getTenantId());
    	    List<MenuVo> menuList=setMunuList(-1,list);
        try{
        	response.getWriter().print(JSONArray.fromObject(menuList).toString());
			response.getWriter().flush();
			response.getWriter().close();
        }
        catch(Exception e)
        {
        	LOGGER.info("获取菜单失败",e);
        }
        }
       
    
       
       public List<MenuVo> setMunuList(int pid,List<GetMenuByOperIDVO> list)
       {
    	   List<MenuVo> munuList=new ArrayList<MenuVo>();
    	   for(GetMenuByOperIDVO vo:list) {	
    		  /*add 非 营业厅功能 不展示*/
    		   if(vo.getSystemId()==null ||"".equals(vo.getSystemId()) || !Constants.SYSTEM_ID.equals(vo.getSystemId())){
    			   continue;
    		   }
    		   /*add 非 业务受理频道功能不展示*/
    		   if(vo.getSystemModeId()==null ||"".equals(vo.getSystemModeId()) || !Constants.SYSTEM_MODEL_ID.equals(vo.getSystemModeId())){
    			   continue;
    		   }
    		   
    		  if(vo.getMenuPid()==pid)
    		  {
    			  MenuVo menuVo=new MenuVo();
    			  menuVo.setMenuId(vo.getMenuId());
    			  menuVo.setMenuName(vo.getMenuName());
    			  menuVo.setMenuUrl(vo.getMenuUrl());
    			  menuVo.setMenuPid(vo.getMenuPid());
    			  menuVo.setMenuPic(vo.getMenuPic());
    			  menuVo.setMenuList(setMunuList((int)menuVo.getMenuId(),list));
    			  munuList.add(menuVo);
    		  }
    	   }
    	return munuList;
       }
       
       
       @RequestMapping(value="/getChannelMenu")
       public void getChannelMenu(HttpServletRequest request,HttpServletResponse response)
       {
    	   List<Menu> configMenu = readMenuConfigFile(); 
    	   try{
           	response.getWriter().print(JSONArray.fromObject(configMenu).toString());
   			response.getWriter().flush();
   			response.getWriter().close();
           }
           catch(Exception e)
           {
           	LOGGER.info("获取菜单失败",e);
           } 
    	   
       }
      
       
       private List<Menu> readMenuConfigFile() {
   		List<Menu> menuList = null ;
   		try {
   			ResourceBundle rb = ResourceBundle.getBundle("menuConfig");
   			String menuString = rb.getString("menu").toString();
   			if (menuString != null && !"".equals(menuString)) {
   				JSONArray jsonArray = JSONArray.fromObject(menuString);
   				menuList = new ArrayList<Menu>();
   				for (int i = 0; i < jsonArray.size(); i++) {
   					JSONObject jsonObject = jsonArray.getJSONObject(i);
   					Menu menu = new Menu();
   					menu.setMenuName(jsonObject.get("menuName").toString());
   					menu.setMenuUrl(jsonObject.get("menuUrl").toString());
   					menu.setUrlType(jsonObject.get("urlType").toString());
   					menu.setMenuFlog(jsonObject.get("menuFlog").toString());
   					menu.setMenuClass(jsonObject.get("menuClass").toString());
   					menuList.add(menu);
   				}
   			}
   		} catch (Exception e) {
   			LOGGER.error("解析json文件出错:" + e.getMessage(),e);
   		}
   		return menuList;
   	}

}
