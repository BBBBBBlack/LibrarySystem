package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.BookSingle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SingleDao extends BaseMapper<BookSingle> {
}
