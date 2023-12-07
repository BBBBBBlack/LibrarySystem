package com.bbbbbblack.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbbbbblack.configuration.WechatConfig;
import com.bbbbbblack.dao.MenuDao;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.LoginUser;
import com.bbbbbblack.domain.entity.User;
import com.bbbbbblack.service.UserService;
import com.bbbbbblack.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2022-10-11 17:12:32
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

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
    public Result loginByEmail(User user) {
        //封装Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
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


    //备注：在前端页面直接加载url 就可以出现二维码界面了。直接用的微信的页面，也可以根据自己的爱好进行设计页面

    /**
     * 公众号微信登录授权回调函数
     */
//    @Override
//    public Result loginByWechat(HttpServletRequest req) throws ServletException, IOException {
//        /*start 获取微信用户基本信息*/
//        //第二步：通过code换取网页授权access_token
//        String url = WechatConfig.getUrl(req.getParameter("code"));
//        String result = HttpClientUtil.doGet(url);
////        JSONObject jsonObject = JSON.parseObject(result);
////        String openid = jsonObject.getString("openid");
//        JSONObject jsonObject = JSONUtil.parseObj(result);
//        String openid = (String) jsonObject.get("openid");
//        //封装Authentication对象
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(openid, "123");
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//        if (Objects.isNull(authentication)) {
//            throw new RuntimeException("用户名或密码错误");
//        }
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        if (loginUser.getUser().getStatus() == 0) {
//            return new Result(HttpStatus.UNAUTHORIZED.value(), "账号已封禁，请联系管理员");
//        }
//        String userId = loginUser.getUser().getId().toString();
//        String jwt = JwtUtil.createJWT(userId);
//        redisCache.setCacheObject("login:" + userId, loginUser, 30, TimeUnit.MINUTES);
//        Map<String, String> map = new HashMap<>();
//        map.put("token", jwt);
//        return new Result(200, "登录成功", map);
//    }
    @Override
    public Result registerByEmail(String email, String password, String code) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userDao.selectOne(queryWrapper);
        if (user != null) {
            return new Result(200, "用户已存在");
        }
        String cc = redisCache.getCacheObject(email);
        if (code.equals(cc)) {
            User user2 = new User
                    (null, null, email, passwordEncoder.encode(password), UUID.randomUUID().toString(), null, null, null, null);
            userDao.insert(user2);
            menuDao.addRole(user2.getId());
            redisCache.deleteObject(email);
            return new Result(200, "注册成功，请重新登录");
        }
        return new Result(200, "验证码错误");
    }

    /**
     * 公众号微信登录授权回调函数
     */
//    @Override
//    public Result registerByWechat(HttpServletRequest req){
//        String url = WechatConfig.getUrl(req.getParameter("code"));
//        String result = HttpClientUtil.doGet(url);
////        JSONObject jsonObject = JSON.parseObject(result);
////        String openid = jsonObject.getString("openid");
//        JSONObject jsonObject = JSONUtil.parseObj(result);
//        String openId = (String) jsonObject.get("openid");
////        查找用户是否存在
//        Map<String,Object> map=new HashMap<>();
//        map.put("open_id",openId);
//        List<User> users = userDao.selectByMap(map);
//        if (!users.isEmpty()){
//            return new Result(200,"用户已存在");
//        }
//
//        String accessToken = (String) jsonObject.get("access_token");
//        String infoUrl = WechatConfig.getUserInfoUrl(openId,accessToken);
//        JSONObject userInfo = JSONUtil.parseObj(HttpClientUtil.doGet(infoUrl));
//        System.out.println(userInfo);
//        // 注册
//        User user =
//                new User(null, openId, null, passwordEncoder.encode("123"),(String) userInfo.get("nickname"), (String) userInfo.get("headimgurl"), null, null, null);
//        userDao.insert(user);
//        menuDao.addRole(user.getId());
//        return new Result(200, "注册成功，重新登录");
//    }
    @Override
    public Result resetForgetPwd(String email, String password, String code) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userDao.selectOne(queryWrapper);
        if (user == null) {
            return new Result(200, "用户不存在");
        }
        String cc = redisCache.getCacheObject(email);
        if (code.equals(cc)) {
            user.setPassword(passwordEncoder.encode(password));
            userDao.updateById(user);
            redisCache.deleteObject(email);
            return new Result(200, "密码重置成功");
        }
        return new Result(200, "验证码错误");
    }

    @Override
    public Result fillUserMessage(User user, MultipartFile headImg) {
        //修改密码
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        //添加头像
        if(!headImg.isEmpty()){
            String headUrl=null;
            try {
                headUrl = FileUtil.upLoadProImag(headImg);
            } catch (Exception e) {
                return new Result(500, e.getMessage());
            }
            //删去旧头像
            String oldUrl = SecurityUtil.getLoginUser().getUser().getHeadImgUrl();
            if(StringUtils.hasText(oldUrl)){
                FileUtil.delete(oldUrl);
            }
            user.setHeadImgUrl(headUrl);
        }
        Long userId = SecurityUtil.getNowUserId();
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId);
        userDao.update(user, updateWrapper);
//      更新SecurityContextHolder
        User user1 = userDao.selectById(userId);
        LoginUser loginUser = SecurityUtil.getLoginUser();
        loginUser.setUser(user1);
        redisCache.setCacheObject("login:" + userId, loginUser);
        return new Result(200, "修改信息成功");
    }

    @Override
    public Result getUserMessage() {
        User user = SecurityUtil.getLoginUser().getUser();
        user.setPassword("");
        user.setId(0L);
        user.setOpenId("");
        if (!StringUtils.hasText(user.getHeadImgUrl())) {
            user.setHeadImgUrl("");
        }
        return new Result(200, "查询用户信息", user);
    }
}
