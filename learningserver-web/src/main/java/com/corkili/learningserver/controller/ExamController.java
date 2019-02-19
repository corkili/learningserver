package com.corkili.learningserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corkili.learningserver.service.ExamService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ExamService examService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public void createExam() {

    }

}
