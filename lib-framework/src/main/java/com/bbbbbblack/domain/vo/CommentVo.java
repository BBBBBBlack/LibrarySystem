package com.bbbbbblack.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CommentVo {
    private String id;//评论id
    private String rootId;//根评论的id，若该评论为根评论，rootId=-1
    private String toCommentId;//评论的评论的id,若该评论为根评论，toCommentId=-1
    private String toCommentName;//被当前评论所评论者的昵称
    private String createByName;//发评论人的昵称
    private String createByPicture;//发评论人头像
    private String content;//评论内容
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//发评论时间
    private List<CommentVo> commentVoList;
}
