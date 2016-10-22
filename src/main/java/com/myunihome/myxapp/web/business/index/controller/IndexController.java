package com.myunihome.myxapp.web.business.index.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	private static final String WELCOME_JSP="welcome.jsp";

    @RequestMapping("/index")
    public String index(HttpSession session) {
        String welcomePath = "/template/default/"+WELCOME_JSP;
        return "forward:"+welcomePath;
    }
    
}
