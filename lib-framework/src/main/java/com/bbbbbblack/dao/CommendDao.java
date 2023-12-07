package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.BookCommend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommendDao extends BaseMapper<BookCommend> {
}
