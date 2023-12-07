package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.User;
import com.bbbbbblack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/loginByEmail")
    public Result loginByEmail(@RequestParam String email,
                               @RequestParam String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return userService.loginByEmail(user);
    }

    //    @RequestMapping("/loginByWechat")
//    public Result loginByWechat(HttpServletRequest request) throws ServletException, IOException {
//        return userService.loginByWechat(request);
//    }
//    @RequestMapping("/registerByWechat")
//    public Result registerByWechat(HttpServletRequest request){
//        return userService.registerByWechat(request);
//    }
    @PostMapping("/registerByEmail")
    public Result registerByEmail(@RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String code) {
        return userService.registerByEmail(email, password, code);
    }

    @PostMapping("/resetForgottenPassword")
    public Result resetForgetPwd(@RequestParam String email,
                                 @RequestParam String newPwd,
                                 @RequestParam String code) {
        return userService.resetForgetPwd(email, newPwd, code);
    }

    @PostMapping("/fillUserMessage")
    public Result fillUserMessage(@RequestParam(required = false) String password,
                                  @RequestParam(required = false) String nickName,
                                  @RequestParam(required = false) MultipartFile headImg) {
        User user = new User();
        user.setPassword(password);
        user.setNickName(nickName);
        return userService.fillUserMessage(user, headImg);
    }

    @GetMapping("/getUserMessage")
    public Result getUserMessage() {
        return userService.getUserMessage();
    }
}
