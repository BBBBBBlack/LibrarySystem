package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Book)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-15 00:43:11
 */
@Mapper
public interface BookDao extends BaseMapper<Book> {

}

