package com.myunihome.myxapp.framepage.sitemesh;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;

/**
 * 默认的装饰器，按照租户匹配框架页面模板<br>
 * Date: 2015年11月28日 <br>
 * Copyright (c) 2015 asiainfo.com <br>
 * 
 * @author gucl
 */
public class ParamDecoratorSelector implements DecoratorSelector<WebAppContext> {
    private DecoratorSelector<WebAppContext> defaultDecoratorSelector;

    private SiteMeshFilterBuilder builder;

    public ParamDecoratorSelector(SiteMeshFilterBuilder builder) {
        this.builder = builder;
        this.defaultDecoratorSelector = builder.getDecoratorSelector();
    }

    @Override
    public String[] selectDecoratorPaths(Content content, WebAppContext context) throws IOException {
        HttpServletRequest request = context.getRequest();
        String decoratorPath="/template/default/template.jsp";
        builder.addDecoratorPath("/*", decoratorPath);
        return defaultDecoratorSelector.selectDecoratorPaths(content, context);
    }

}
