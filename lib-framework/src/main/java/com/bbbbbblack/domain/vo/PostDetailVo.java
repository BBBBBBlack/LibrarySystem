package com.bbbbbblack.domain.vo;

import com.bbbbbblack.domain.entity.Post;
import com.bbbbbblack.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailVo implements Serializable {

    public static final long serialVersionUID = 19263192360L;
    //帖子id
    private String id;
    //帖子内容
    private String content;
    //发帖人id
    private User poster;
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

    public PostDetailVo(Post post) {
        this.id = post.getId().toString();
        this.content = post.getContent();
        this.subject = post.getSubject();
        this.likes = post.getLikes();
        this.collCnt = post.getCollCnt();
        this.createTime = post.getCreateTime();
        this.bookIsbn = post.getBookIsbn();
    }
}
