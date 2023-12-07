package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.service.CommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commend")
public class CommendController {
    @Autowired
    CommendService commendService;

    @PostMapping("/openPush")
    public Result openPush(@RequestParam String clientId) {
        return commendService.openPush(clientId);
    }

    @PostMapping("/closePush")
    public Result closePush() {
        return commendService.closePush();
    }

    @PostMapping("/openCommend")
    public Result openCommend(@RequestParam Integer days) {
        //057892eafbb95da07617d7feaabc5716
        return commendService.openCommend(days);
    }

    @PostMapping("/closeCommend")
    public Result closeCommend() {
        return commendService.closeCommend();
    }
}
