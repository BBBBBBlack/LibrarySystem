package com.bbbbbblack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Post;
import com.bbbbbblack.domain.vo.PostDetailVo;
import com.bbbbbblack.domain.vo.PostVo;

import java.util.List;

/**
 * (Post)表服务接口
 *
 * @author makejava
 * @since 2022-12-05 23:12:24
 */
public interface PostService extends IService<Post> {
    Result<PostVo> sharePost(Post post);

    Result<List<PostVo>> showPostList(String isbn, Integer page);

    Result<PostDetailVo> showPostDetail(String id);
}

