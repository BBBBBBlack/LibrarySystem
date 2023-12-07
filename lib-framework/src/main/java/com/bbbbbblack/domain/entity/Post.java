package com.bbbbbblack.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (Post)表实体类
 *
 * @author makejava
 * @since 2022-12-05 23:12:12
 */
@SuppressWarnings("serial")
@Data
public class Post implements Serializable {

    public static final long serialVersionUID = 983759357123L;
    //帖子id
    private Long id;
    //帖子内容
    private String content;
    //发帖人id
    private Long posterId;
    //主题
    private String subject;
    //点赞数
    private Integer likes;
    //收藏数
    private Integer collCnt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date createTime;

    private String bookIsbn;
}

