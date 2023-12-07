package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.BookSingleVo;
import com.bbbbbblack.service.SingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/single")
public class SingleController {
    @Autowired
    SingleService singleService;

    @GetMapping("/getCopy/{isbn}")
    public Result<List<BookSingleVo>> getCopy(@PathVariable String isbn) {
        return singleService.getCopy(isbn);
    }

    @GetMapping("/createSingleQRCode/{bookId}")
    public void createSingleQRCode(@PathVariable String bookId, HttpServletResponse response) {
        singleService.createSingleQRCode(bookId, response);
    }
}
