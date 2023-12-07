package com.bbbbbblack.filter;

import com.bbbbbblack.utils.HeaderMapRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PackWsRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("进来了，进来了");
        request=addTokenForWebSocket(request,response);
        filterChain.doFilter(request,response);
    }
    private HttpServletRequest addTokenForWebSocket(HttpServletRequest request, HttpServletResponse response) { ;
        String token = request.getHeader("token");
        if(StringUtils.hasText(token)) {
            return request;
        }
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        token = request.getHeader("Sec-WebSocket-Protocol");
        if(!StringUtils.hasText(token)) {
            return request;
        }
        requestWrapper.addHeader("token", token);
        response.addHeader("Sec-WebSocket-Protocol", token);
        return  requestWrapper;
    }

}
