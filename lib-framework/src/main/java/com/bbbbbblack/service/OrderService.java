package com.bbbbbblack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.BookOrder;
import com.bbbbbblack.domain.vo.BookOrderVo;

import java.util.Date;
import java.util.List;

/**
 * (BookOrder)表服务接口
 *
 * @author makejava
 * @since 2022-11-19 00:37:11
 */
public interface OrderService {
    Result orderBook(Long bookId, Date orderTime);

    Result<List<BookOrderVo>> showOrder();
}

