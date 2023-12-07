package com.bbbbbblack.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Mapper
public interface MenuDao {
    List<String> findPermsByUserId(Long userId);
    void addRole(Long userId);
    void changeRole(Long userId);
}
