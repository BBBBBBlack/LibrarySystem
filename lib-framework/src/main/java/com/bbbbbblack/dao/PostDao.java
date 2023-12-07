package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Post)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-05 23:12:12
 */
@Mapper
public interface PostDao extends BaseMapper<Post> {

}

