package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbbbbblack.dao.CommentDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Comment;
import com.bbbbbblack.domain.vo.CommentVo;
import com.bbbbbblack.domain.vo.PageVo;
import com.bbbbbblack.service.CommentService;
import com.bbbbbblack.utils.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-12-08 09:22:43
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentDao commentDao;

    @Override
    public Result addComment(Comment comment) {
        Long userId = SecurityUtil.getNowUserId();
        comment.setCreateBy(userId);
        comment.setCreateTime(new Date());
        commentDao.insert(comment);
        return new Result(200, "已评论");
    }

    @Override
    public Result findComment(String postId, Integer page) {
        List<CommentVo> rootComments = commentDao.findComments("-1", postId);
        for (CommentVo rootComment : rootComments) {
            List<CommentVo> comments = commentDao.findComments(rootComment.getId().toString(), postId);
            rootComment.setCommentVoList(comments);
        }
        PageHelper.startPage(page, PageVo.pageSize.intValue());
        PageInfo<CommentVo> info = new PageInfo<>(rootComments);
        return new Result(200, "查询评论", info);
    }
}

