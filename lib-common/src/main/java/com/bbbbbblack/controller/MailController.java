package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    MailService mailService;
    @PostMapping("/sendMail")
    public Result sendMail(@RequestParam String email,
                           @RequestParam Integer type){
        return mailService.sendMail(email,type);
    }
}
