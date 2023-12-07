package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbbbbblack.cache.PostCache;
import com.bbbbbblack.dao.PostDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Post;
import com.bbbbbblack.domain.vo.PageVo;
import com.bbbbbblack.domain.vo.PostDetailVo;
import com.bbbbbblack.domain.vo.PostVo;
import com.bbbbbblack.service.PostService;
import com.bbbbbblack.utils.SecurityUtil;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * (Post)表服务实现类
 *
 * @author makejava
 * @since 2022-12-05 23:12:24
 */
@Service("postService")
public class PostServiceImpl extends ServiceImpl<PostDao, Post> implements PostService {

    @Autowired
    PostDao postDao;
    @Autowired
    PostCache postCache;

    @Override
    public Result<PostVo> sharePost(Post post) {
        postDao.insert(post);
        post.setPosterId(null);
        return new Result<>(200, "成功分享", new PostVo(post));
    }

    @Override
    public Result<List<PostVo>> showPostList(String isbn, Integer page) {
        List<Post> postList = postCache.getPostList(isbn, page);
        List<PostVo> voList = new ArrayList<>();
        for (Post post : postList) {
            voList.add(new PostVo(post));
        }
        return new Result<>(200, "查看分享列表", voList);
    }

    @Override
    public Result<PostDetailVo> showPostDetail(String id) {
        PostDetailVo detail = postCache.getPostDetail(id);
        return new Result<>(200, "分享详情", detail);
    }
}

