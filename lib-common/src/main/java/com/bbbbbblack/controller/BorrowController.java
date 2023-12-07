package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.BookListVo;
import com.bbbbbblack.service.BorrowService;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    BorrowService borrowService;

    @PostMapping("/addToCert/{bookId}")
    public Result addToCert(@PathVariable String bookId) {
        return borrowService.addToCert(bookId);
    }

    @PostMapping("/delCert/{bookId}")
    public Result delCert(@PathVariable String bookId) {
        return borrowService.delCert(bookId);
    }

    @GetMapping("/showCert")
    public Result<List<BookListVo>> showCert() {
        return borrowService.showCert(SecurityUtil.getNowUserId().toString());
    }

    @GetMapping("/showBorrowed/{type}")
    public Result showBorrowed(@PathVariable String type) {
        return borrowService.showBorrowed(Integer.parseInt(type), SecurityUtil.getNowUserId().toString());
    }

    @GetMapping("/freshMyQR")
    public void freshMyQR(HttpServletResponse response) {
        borrowService.freshMyQR(response);
    }
}
