package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.dao.MenuDao;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.entity.LoginUser;
import com.bbbbbblack.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private MenuDao menuDao;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(userName.endsWith("@qq.com")){
            queryWrapper.eq("email",userName);
        } else {
            queryWrapper.eq("open_id",userName);
        }
        User user = userDao.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        List<String> permsList = menuDao.findPermsByUserId(user.getId());
        return new LoginUser(user,permsList);
    }
}
