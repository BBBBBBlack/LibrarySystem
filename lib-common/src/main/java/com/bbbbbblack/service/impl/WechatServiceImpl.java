package com.bbbbbblack.service.impl;

import com.alibaba.fastjson.JSON;
import com.bbbbbblack.configuration.WechatConfig;
import com.bbbbbblack.dao.MenuDao;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.LoginUser;
import com.bbbbbblack.service.WechatService;
import com.bbbbbblack.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class WechatServiceImpl implements WechatService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisCache redisCache;
    @Autowired
    UserDao userDao;
    @Autowired
    MenuDao menuDao;
    @Override
    public Result loginByWechat(String openId) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(openId, "我是个大傻逼。I hope G.E.M will not be limited and create more popular songs.");
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (loginUser.getUser().getStatus() == 0) {
            return new Result(HttpStatus.UNAUTHORIZED.value(), "账号已封禁，请联系管理员");
        }
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        redisCache.setCacheObject("login:" + userId, loginUser, 30, TimeUnit.MINUTES);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new Result(200, "登录成功", map);
    }
//    @Override
//    public void checkToken(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
//        String signature = request.getParameter("signature");
//        String timestamp = request.getParameter("timestamp");
//        String nonce = request.getParameter("nonce");
//        String echostr = request.getParameter("echostr");
//        String[] arr={WechatConfig.WECHAT_TOKEN,timestamp,nonce};
//        Arrays.sort(arr);
//        String s = arr[0] + arr[1] + arr[2];
//        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
//        byte[] digest = messageDigest.digest(s.getBytes(StandardCharsets.UTF_8));
//        StringBuilder s2 = new StringBuilder();
//        for (byte b : digest) {
//            s2.append(ByteToStrUtil.byteToHexStr(b));
//        }
//        if(s2.toString().equals(signature.toUpperCase())){
//            PrintWriter writer = response.getWriter();
//            writer.print(echostr);
//        }
//    }
//    @Override
//    public void loadWechatLoginUrl(Integer type, Integer purpose, HttpServletResponse response) throws Exception {
//        // 第一步：用户同意授权，获取code
//        String encodeBaseUrl = null;
//        String url = null;
//        String imgPath = "D:\\DataBase\\pictures\\1186adb7193f4aacbb4f027ffd375f81.jpg";
////       登录
//        if (purpose == 1) {
//            encodeBaseUrl = URLEncoder.encode(WechatConfig.LOGIN_BASEURL, "utf-8");
//        } else if (purpose == 2) {
//            encodeBaseUrl = URLEncoder.encode(WechatConfig.REGISTER_BASEURL, "utf-8");
//        }
//        url = WechatConfig.getCodeUrl(encodeBaseUrl);
//        if (type == 1) {
//            //生成二维码的，扫描后跳转上面的地址
//            BufferedImage encode = QrCodeUtils.encode(url, imgPath, true);
//            response.setContentType("image/png");
//            OutputStream outputStream = response.getOutputStream();
//            ImageIO.write(encode, "png", outputStream);
//        } else if (type==2){
//            Result result = new Result(200, "请用微信访问如下链接登录", url);
//            String json = JSON.toJSONString(result);
//            WebUtils.renderString(response, json);
//        }
//    }

}
