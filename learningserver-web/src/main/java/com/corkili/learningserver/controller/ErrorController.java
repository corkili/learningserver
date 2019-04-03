package com.corkili.learningserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("**/error")
    public String error() {
        return "error";
    }

    @RequestMapping("/nologin")
    public String nologin() {
        return "nologin";
    }

}
