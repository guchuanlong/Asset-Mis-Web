package com.myunihome.myxapp.framepage.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.myunihome.myxapp.base.exception.SystemException;
import com.myunihome.myxapp.utils.web.model.UserLoginVo;

public final class OperInfoUtil {

    public static final String USER_SESSION_KEY = "user_session_key";

    private OperInfoUtil() {

    }

    public static UserLoginVo getCurrentLoginUser(HttpSession session) {
        UserLoginVo user = (UserLoginVo) session.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            throw new SystemException("无法获取当前登录人员信息：登录失效，请重新登录");
        }
        /* UserLoginVo user = new UserLoginVo();
        user.setOperCode("ST-001");
        user.setOperId(1000L);
        user.setStaffId("100010");
        user.setStaffName("张超");
        user.setTenantId("BIS-ST");
        user.setTenantName("世通在线");
        */
        return user;
    }

    public static String getTenantId(HttpServletRequest request) {
        UserLoginVo user = getCurrentLoginUser(request.getSession());
        return user.getTenantId();
    }

    public static String getTenantId(HttpSession session) {
        UserLoginVo user = getCurrentLoginUser(session);
        return user.getTenantId();
    }

    public static String getTenantId(PageContext pageContext) {
        return getTenantId((HttpServletRequest) pageContext.getRequest());
    }

    public static long getOperId(HttpSession session) {
        UserLoginVo user = getCurrentLoginUser(session);
        return user.getOperId();
    }

}
