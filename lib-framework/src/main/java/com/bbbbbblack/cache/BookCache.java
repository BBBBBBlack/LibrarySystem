package com.bbbbbblack.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.dao.BookDao;
import com.bbbbbblack.domain.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class BookCache {
    @Autowired
    BookDao bookDao;

    @Cacheable(cacheNames = "bookISBNCache", key = "#isbn")
    public Book getBookByISBN(String isbn) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("isbn", isbn);
        return bookDao.selectOne(wrapper);
    }
}
