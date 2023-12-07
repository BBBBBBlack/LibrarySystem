package com.bbbbbblack.controller;


import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Comment;
import com.bbbbbblack.service.CommentService;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2022-12-08 09:32:46
 */
@RestController
@RequestMapping("comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    @PostMapping("/addComment")
    public Result addComment(@RequestParam String postId,
                             @RequestParam String rootId,
                             @RequestParam String content,
                             @RequestParam String toCommentId) {
        Comment comment = new Comment
                (null, Long.parseLong(postId), Long.parseLong(rootId), content, SecurityUtil.getNowUserId(), new Date(), Long.parseLong(toCommentId));
        return commentService.addComment(comment);
    }

    @PostMapping("/findComment")
    public Result findComment(@RequestParam String postId,
                              @RequestParam Integer page) {
        return commentService.findComment(postId, page);
    }
}

