package com.bbbbbblack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Comment;

/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2022-12-08 09:22:43
 */
public interface CommentService {
    Result addComment(Comment comment);

    Result findComment(String postId, Integer page);
}

