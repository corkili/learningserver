package com.corkili.learningserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/submittedExam")
public class SubmittedExamController {

    @Autowired
    private TokenManager tokenManager;

}
