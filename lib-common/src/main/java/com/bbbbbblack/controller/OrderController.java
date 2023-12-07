package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.BookOrderVo;
import com.bbbbbblack.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/addOrder")
    public Result addOrder(@RequestParam String bookId,
                           @RequestParam String orderTime) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = format.parse(orderTime);
        } catch (ParseException e) {
            return new Result(400, "日期格式不正确");
        }
        return orderService.orderBook(Long.valueOf(bookId), date);
    }

    @GetMapping("/showOrder")
    public Result<List<BookOrderVo>> showOrder() {
        return orderService.showOrder();
    }
}
