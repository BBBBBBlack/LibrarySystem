package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.BookOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BookOrder)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-19 00:36:58
 */
@Mapper
public interface OrderDao extends BaseMapper<BookOrder> {

}

