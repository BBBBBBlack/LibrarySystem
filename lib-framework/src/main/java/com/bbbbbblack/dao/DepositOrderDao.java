package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.DepositOrder;
import org.apache.ibatis.annotations.Mapper;


/**
 * (DepositOrder)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-08 14:40:44
 */
@Mapper
public interface DepositOrderDao extends BaseMapper<DepositOrder> {
    void InsertOrder(DepositOrder order);
}

