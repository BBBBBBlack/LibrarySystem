package com.bbbbbblack.domain.vo;

import com.bbbbbblack.domain.entity.BookOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BookOrderVo {
    //唯一主键
    private Long id;
    //书籍id
    private Long bookId;

    private BookVo bookVo;
    //预订时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date createTime;
    //预订取书时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date orderTime;
    //预订状态（0为预订有效；-1为预订失效）
    private Integer status;

    public BookOrderVo(BookOrder order) {
        this.id = order.getId();
        this.bookId = order.getBookId();
        this.createTime = order.getCreateTime();
        this.orderTime = order.getOrderTime();
        this.status = order.getStatus();
    }
}
