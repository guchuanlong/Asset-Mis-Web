package com.myunihome.myxapp.web.system.base;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myunihome.myxapp.utils.web.model.UserLoginVo;
import com.myunihome.myxapp.web.system.constants.Constants;

@Controller
public class LogoutController {
	private static final Logger LOG = Logger.getLogger(LogoutController.class);
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		UserLoginVo user = (UserLoginVo) session.getAttribute(Constants.SESSION_USER_KEY);
		if(user!=null){
			try {
				session.removeAttribute(Constants.SESSION_USER_KEY);
				ResourceBundle rb = ResourceBundle.getBundle("sso");
				String logOutServerUrl = rb
						.getString("logOutServerUrl");
				String logOutBackUrl = rb
						.getString("logOutBackUrl");
				response.sendRedirect(logOutServerUrl + "?service=" + logOutBackUrl+"?tenantId="+user.getTenantId());
				session.invalidate();
			} catch (IOException e) {
				LOG.error("用户登出失败",e);
			}
		}
	}

}
