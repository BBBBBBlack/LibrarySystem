package com.bbbbbblack.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbbbbblack.dao.PostDao;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.Post;
import com.bbbbbblack.domain.entity.User;
import com.bbbbbblack.domain.vo.PageVo;
import com.bbbbbblack.domain.vo.PostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostCache {

    @Autowired
    PostDao postDao;
    @Autowired
    UserDao userDao;

    //@Cacheable(cacheNames = "postListCache", key = "#isbn + #page")
    public List<Post> getPostList(String isbn, Integer page) {
        IPage<Post> iPage = new Page<>();
        iPage.setSize(PageVo.pageSize)
                .setCurrent(page);
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.eq("book_isbn", isbn);
        return postDao.selectPage(iPage, wrapper).getRecords();
    }

    @Cacheable(cacheNames = "postDetailCache", key = "#postId")
    public PostDetailVo getPostDetail(String postId) {
        Post post = postDao.selectById(postId);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("nick_name", "head_img_url")
                .eq("id", post.getPosterId());
        User user = userDao.selectOne(wrapper);
        PostDetailVo detailVo = new PostDetailVo(post);
        detailVo.setPoster(user);
        return detailVo;
    }
}
