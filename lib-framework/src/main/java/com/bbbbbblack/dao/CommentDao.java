package com.bbbbbblack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbbbbblack.domain.entity.Comment;
import com.bbbbbblack.domain.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-08 09:22:41
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {
    List<CommentVo> findComments(@Param("rootId") String rootId, @Param("postId") String postId);

    void deleteComments(String commentId);

    List<CommentVo> findMyComment(String userId);

    List<CommentVo> findMyReply(String toCommentId);
}

