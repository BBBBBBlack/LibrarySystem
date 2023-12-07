package com.bbbbbblack.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (BookOrder)表实体类
 *
 * @author makejava
 * @since 2022-11-19 00:36:58
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookOrder implements Serializable {

    public static final long serialVersionUID = 68273458193913L;

    //唯一主键
    private Long id;
    //书籍id
    private Long bookId;
    //用户id
    private Long userId;
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
}

