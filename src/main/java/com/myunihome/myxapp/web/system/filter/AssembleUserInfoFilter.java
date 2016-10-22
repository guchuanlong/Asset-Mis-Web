package com.myunihome.myxapp.web.system.filter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;

import com.myunihome.myxapp.common.api.tenant.interfaces.IGnTenantQuerySV;
import com.myunihome.myxapp.common.api.tenant.param.GnTenantConditon;
import com.myunihome.myxapp.common.api.tenant.param.GnTenantVo;
import com.myunihome.myxapp.paas.util.StringUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;

public class AssembleUserInfoFilter implements Filter{
	private String[] ignor_suffix={};
	private static final String SESSION_USER_KEY = "user_session_key";
	private static final Logger LOG = Logger.getLogger(AssembleUserInfoFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String ignore_res = filterConfig.getInitParameter("ignore_suffix");
        if (!"".equals(ignore_res)){
        	this.ignor_suffix = filterConfig.getInitParameter("ignore_suffix").split(",");
        }
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if(!shouldFilter(req)){
			chain.doFilter(req, response);
			return;
		}
		HttpSession session = req.getSession();
		UserLoginVo user = (UserLoginVo) session.getAttribute(SESSION_USER_KEY);
		if(user == null){
			user = assembleUser(req);
			
			/*非营业频道登录 或者 相关属性 为空进行控制*/
			if(user== null ){
				req.getRequestDispatcher("guide.jsp").forward(request, response);
			}else {
				session.setAttribute(SESSION_USER_KEY,user);
				chain.doFilter(req, response);
				LOG.info("已封装的用户信息为："+user.toString());
			}
		} else {
			chain.doFilter(req, response);
		}
	}

	@Override
	public void destroy() {
		
	}
	
	/**
	 * 封装用户信息
	 * @param request
	 * @return
	 */
	private UserLoginVo assembleUser(HttpServletRequest request){
		UserLoginVo user = null;
		try {
			Principal principal = request.getUserPrincipal();
			if(principal!=null){
				user = new UserLoginVo();
				AttributePrincipal attributePrincipal = (AttributePrincipal) principal;
				Map<String, Object> attributes = attributePrincipal.getAttributes();
				Field[] fields = UserLoginVo.class.getDeclaredFields();
				for (Field field : fields) {
					String value = (String) attributes.get(field.getName());
					if(value != null){
						field.setAccessible(true);
						if("long".equals(field.getType().toString())){
							field.set(user, Long.parseLong(value));
						}else{
							field.set(user, value);
						}
					}
				}//end for
				
				if(!StringUtil.isBlank(user.getTenantId())){
					user.setTenantName(getTenantNameByTenantId(user.getTenantId()));
				}
				
			}//end if
		}catch (Exception e) {
			LOG.error("封装用户信息失败",e);
		}
		return user;
	}
	
	private String getTenantNameByTenantId(String tenantId){
		String tenantName="";
		GnTenantConditon cond=new GnTenantConditon();
		cond.setTenantId(tenantId);
		GnTenantVo tenant=DubboConsumerFactory.getService(IGnTenantQuerySV.class).getTenant(cond);
		if(tenant!=null){
			tenantName=tenant.getTenantName();
		}
		return tenantName;
	}
	
	private boolean shouldFilter(HttpServletRequest req) {
		if(ignor_suffix!=null&&ignor_suffix.length>0){
			String uri = req.getRequestURI().toLowerCase();
			for (String suffix : ignor_suffix) {
				if(uri.endsWith(suffix)){
					return false;
				}
			}
		}
		return true;
	}
}
