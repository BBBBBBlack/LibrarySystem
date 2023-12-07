package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Search;
import com.bbbbbblack.domain.vo.PageVo;
import com.bbbbbblack.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * (Book)表控制层
 *
 * @author makejava
 * @since 2022-10-15 00:42:51
 */
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/getByISBN/{isbn}")
    public Result getByISBN(@PathVariable String isbn) {
        return bookService.getBookByISBN(isbn);
    }

    @GetMapping("/getSimpleImgByISBN/{isbn}")
    public Result<String> getSimpleImgByISBN(@PathVariable String isbn, HttpServletResponse response) {
        return bookService.getSimpleImgByISBN(isbn, response);
    }

    @GetMapping("/getBookDetailByISBN/{isbn}")
    public Result getBookDetailByISBN(@PathVariable String isbn) {
        return bookService.getBookDetailByISBN(isbn);
    }

    @PostMapping("/getBooksByType")
    public Result getBooksByType(@RequestParam String from,
                                 @RequestParam String type) {
        return bookService.getBooksByType(Long.parseLong(from), Integer.parseInt(type));
    }

    @PostMapping("/getBooksByKeywords")
    public Result getBooksByKeywords(@RequestParam(required = false) Integer type,
                                     @RequestParam(required = false) Integer bookType,
                                     @RequestParam(required = false) String keywords,
                                     @RequestParam(required = false) Integer highPrice,
                                     @RequestParam(required = false) Integer lowPrice,
                                     @RequestParam Integer from) {
        Search search=new Search(type,bookType,keywords,highPrice,lowPrice,from);
        search.setFrom(search.getFrom() * PageVo.pageSize.intValue());
        return bookService.getBooksByKeywords(search);
    }

    @PostMapping("/getMoreLikeThis")
    public Result getMoreLikeThis(@RequestParam String title,
                                  @RequestParam Integer from) {
        return bookService.getMoreLikeThis(title, from * PageVo.pageSize.intValue());
    }

    @GetMapping("/getQueryRecord")
    public Result getQueryRecord() {
        return bookService.getQueryRecord();
    }

    @GetMapping("/getPublicTags")
    public Result getPublicTags() {
        return bookService.getPublicTags();
    }

    @GetMapping("/getPersonalTags")
    public Result getPersonalTags() {
        return bookService.getPersonalTags();
    }

    @PostMapping("/getAuto")
    public Result getAuto(@RequestParam String keywords,
                          @RequestParam Integer type) {
        return bookService.getAuto(keywords, type);
    }
}

