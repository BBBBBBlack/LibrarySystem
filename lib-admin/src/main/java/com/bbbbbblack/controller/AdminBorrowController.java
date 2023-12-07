package com.bbbbbblack.controller;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.service.AdminBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AdminBorrowController {
    @Autowired
    AdminBorrowService adminBorrowService;

    @GetMapping("/getPersonalBorrow/{userId}")
    public Result getPersonalBorrow(@PathVariable String userId) {
        return adminBorrowService.getPersonalBorrow(userId);
    }

    @PostMapping("/confirmCert")
    public Result confirmCert(@RequestBody Map<String, String> map) {
        String bookId = map.get("bookId");
        return adminBorrowService.confirmCert(bookId);
    }

    @PostMapping("/returnBook")
    public Result returnBook(@RequestBody Map<String, String> map) {
        String bookId = map.get("bookId");
        return adminBorrowService.returnBook(bookId);
    }
}
