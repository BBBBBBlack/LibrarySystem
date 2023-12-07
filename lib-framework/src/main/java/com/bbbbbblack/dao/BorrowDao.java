package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.BookList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowDao extends BaseMapper<BookList> {
}
