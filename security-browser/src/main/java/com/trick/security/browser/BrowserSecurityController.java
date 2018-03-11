package com.trick.security.browser;

import com.trick.security.core.properties.SecurityConstants;
import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.browser.BrowserProperties;
import com.trick.security.core.support.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {

    private static Logger logger = LoggerFactory.getLogger(BrowserSecurityController.class);
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final BrowserProperties browserProperties;

    @Autowired
    public BrowserSecurityController(SecurityProperties securityProperties) {
        this.browserProperties = securityProperties.getBrowser();
    }

    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATED_URI)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("\n引发跳转的请求是: " + targetUrl);
            redirectStrategy.sendRedirect(request, response, browserProperties.getLoginPage());
        }
        return new SimpleResponse("访问的请求需要身份认证");
    }

    @GetMapping(SecurityConstants.DEFAULT_SESSION_INVALID_URI)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse sessionInvalid() {
        return new SimpleResponse("session失效");
    }
}
