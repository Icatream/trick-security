package com.trick.sso.server;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@RestController
//@SessionAttributes("authorizationRequest")
public class SsoApprovalEndpoint {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @RequestMapping("/oauth/confirm_access")
    public void getAccessConfirmation(HttpServletRequest request, HttpServletResponse response) throws IOException {

        redirectStrategy.sendRedirect(request, response, "/oauth/authorize");
    }

}
