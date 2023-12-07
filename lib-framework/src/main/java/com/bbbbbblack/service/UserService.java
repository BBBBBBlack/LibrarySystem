package com.bbbbbblack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-10-11 17:12:31
 */
public interface UserService extends IService<User> {
    Result loginByEmail(User user);

//    Result loginByWechat(HttpServletRequest req) throws ServletException, IOException;

    Result registerByEmail(String email, String password, String code);

//    Result registerByWechat(HttpServletRequest req);

    Result resetForgetPwd(String email, String password, String code);

    Result fillUserMessage(User user, MultipartFile headImg);

    Result getUserMessage();
}

