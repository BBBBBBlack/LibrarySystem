package com.bbbbbblack.handler;

import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson.JSON;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("他妈的认证失败");
        Result result=new Result(HttpStatus.UNAUTHORIZED.value(), "认证失败");
        String json = JSONUtil.toJsonStr(result);
        WebUtils.renderString(response,json);
    }
}
