package com.trick.security.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping({"","/index"})
    public String index(){
        return "/pub/Index.html";
    }
}
