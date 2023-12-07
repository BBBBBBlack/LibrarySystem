package com.bbbbbblack.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2022-12-08 09:22:41
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    //评论ID
    @TableId(type = IdType.AUTO)
    private Long id;
    //所评论帖子ID
    private Long postId;
    //根评论ID，若该评论为根评论，root_id=-1
    private Long rootId;
    //评论内容
    private String content;
    //发评论人ID
    private Long createBy;
    //创建评论时间
    private Date createTime;
    //所评论的评论ID,若该评论为根评论，to_comment_id=-1
    private Long toCommentId;
}

