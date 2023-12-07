package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Post;
import com.bbbbbblack.domain.vo.PostDetailVo;
import com.bbbbbblack.domain.vo.PostVo;
import com.bbbbbblack.service.PostService;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping("/sharePost")
    public Result<PostVo> sharePost(@RequestParam String content,
                                  @RequestParam String subject,
                                  @RequestParam String bookIsbn) {
        Post post = new Post();
        post.setContent(content);
        post.setSubject(subject);
        post.setBookIsbn(bookIsbn);
        post.setCreateTime(new Date());
        post.setPosterId(SecurityUtil.getNowUserId());
        return postService.sharePost(post);
    }

    @PostMapping("/showPostList")
    public Result<List<PostVo>> showPostList(@RequestParam String isbn,
                                             @RequestParam Integer page) {
        return postService.showPostList(isbn, page);
    }

    @GetMapping("/showPostDetail/{id}")
    public Result<PostDetailVo> showPostDetail(@PathVariable String id) {
        return postService.showPostDetail(id);
    }
}
