package com.bbbbbblack.service;

import com.bbbbbblack.domain.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface WechatService {
//    void checkToken(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, IOException;
//    void loadWechatLoginUrl(Integer type, Integer purpose, HttpServletResponse response) throws Exception;
    Result loginByWechat(String openId);
}
