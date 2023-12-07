package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbbbbblack.amqp.publisher.KeywordsPublisher;
import com.bbbbbblack.cache.BookCache;
import com.bbbbbblack.dao.BookDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.Search;
import com.bbbbbblack.domain.vo.BookVo;
import com.bbbbbblack.domain.vo.PageVo;
import com.bbbbbblack.service.BookService;
import com.bbbbbblack.utils.ESUtil;
import com.bbbbbblack.utils.SecurityUtil;
import com.bbbbbblack.utils.ThumbnailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * (Book)表服务实现类
 *
 * @author makejava
 * @since 2022-10-15 00:43:17
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookDao, Book> implements BookService {
    @Autowired
    ThumbnailsUtil thumbnailsUtil;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    BookDao bookDao;
    @Autowired
    BookCache bookCache;
    @Autowired
    KeywordsPublisher publisher;

    @Override
    public Result getBookByISBN(String isbn) {
        Book book = bookCache.getBookByISBN(isbn);
        return new Result(200, "查询书号", new BookVo(book));
    }

    @Override
    public Result<String> getSimpleImgByISBN(String isbn, HttpServletResponse response) {
        Book book = bookCache.getBookByISBN(isbn);
        String coverUrl = book.getCoverUrl();
//        int index = coverUrl.lastIndexOf('/');
//        try {
//            thumbnailsUtil.thumbnails(response.getOutputStream(), coverUrl.substring(index));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new Result<>(200,"获取图片",coverUrl);
    }

    @Override
    public Result getBookDetailByISBN(String isbn) {
        return new Result(200, "书籍详情", bookCache.getBookByISBN(isbn));
    }

    @Override
    public Result getBooksByType(Long page, Integer type) {
        IPage<Book> iPage = new Page<>();
        iPage.setSize(PageVo.pageSize);
        iPage.setCurrent(page);
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        bookDao.selectPage(iPage, wrapper);
        List<Book> records = iPage.getRecords();
        List<BookVo> bookVoList = new ArrayList<>();
        for (Book book : records) {
            bookVoList.add(new BookVo(book));
        }
        PageVo<BookVo> pageVo = new PageVo<>();
        pageVo.setRes(bookVoList);
        pageVo.setTotal(iPage.getTotal());
        return new Result(200, "查询分类", pageVo);
    }

    @Override
    public Result getBooksByKeywords(Search search) {
        List<BookVo> bookVoList = null;
        try {
            bookVoList = ESUtil.queryBoolKeywords("library_book", search);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bookVoList != null && !bookVoList.isEmpty()) {
            //将搜索记录记入redis，并提取个人和群体标签
            publisher.sendMessage(search);
        }
        return new Result(200, "关键词查询", bookVoList);
    }

    @Override
    public Result getMoreLikeThis(String keywords, Integer from) {
        List<BookVo> bookVoList = null;
        try {
            List<String> list = new ArrayList<>();
            list.add(keywords);
            bookVoList = ESUtil.getMoreLikeThis("library_book", list, from);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(200, "推荐相关书籍", bookVoList);
    }

    @Override
    public Result getQueryRecord() {

        List<Object> range = redisTemplate.opsForList().
                range("query_record:" + SecurityUtil.getNowUserId().toString(), 0, -1);
        return new Result(200, "查询搜索记录", range);
    }

    @Override
    public Result<Set<Object>> getPublicTags() {
        Set<Object> commonTags = redisTemplate.opsForZSet().reverseRange("common_tags", 0, PageVo.pageSize);
        return new Result<>(200, "获取大家在搜", commonTags);
    }

    @Override
    public Result<Set<Object>> getPersonalTags() {
        String userId = SecurityUtil.getNowUserId().toString();
        Set<Object> personalTags = redisTemplate.opsForZSet().reverseRange("personal_tags:" + userId, 0, PageVo.pageSize);
        return new Result<>(200, "获取个人标签", personalTags);
    }

    @Override
    public Result getAuto(String keywords, Integer type) {
        List<String> list = null;
        try {
            //标题补全
            if (type == 1) {
                list = ESUtil.autoTitle("library_book", keywords);
            } else if (type == 2) {
                list = ESUtil.autoAuthor("library_book", keywords);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(200, "自动补全", list);
    }
}

