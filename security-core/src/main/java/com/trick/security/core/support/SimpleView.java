package com.trick.security.core.support;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SimpleView extends AbstractView {

    private final String context;

    public SimpleView(String context) {
        this.context = context;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<h4>" + context + "</h4>");
    }
}
