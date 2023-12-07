package com.bbbbbblack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.Search;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * (Book)表服务接口
 *
 * @author makejava
 * @since 2022-10-15 00:43:16
 */
public interface BookService extends IService<Book> {
    Result getBookByISBN(String isbn);

    Result<String> getSimpleImgByISBN(String isbn, HttpServletResponse response);

    Result getBookDetailByISBN(String isbn);

    Result getBooksByType(Long page, Integer type);

    Result getBooksByKeywords(Search search);

    Result getMoreLikeThis(String keywords, Integer from);

    Result getQueryRecord();

    //获取大家在搜
    Result<Set<Object>> getPublicTags();

    //获取我的标签
    Result<Set<Object>> getPersonalTags();

    //自动补全
    Result getAuto(String keywords, Integer type);
}

