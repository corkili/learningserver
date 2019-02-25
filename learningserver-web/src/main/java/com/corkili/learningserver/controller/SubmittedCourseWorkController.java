package com.corkili.learningserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/submittedCourseWork")
public class SubmittedCourseWorkController {

    @Autowired
    private TokenManager tokenManager;

}
