package com.bbbbbblack.domain.vo;

import com.bbbbbblack.domain.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {

    private String id;

    private String subject;

    private Integer likes;

    private Integer collCnt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date createTime;

    public PostVo(Post post) {
        this.id = post.getId().toString();
        this.subject = post.getSubject();
        this.likes = post.getLikes();
        this.collCnt = post.getCollCnt();
        this.createTime = post.getCreateTime();
    }
}
