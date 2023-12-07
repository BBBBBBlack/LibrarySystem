package com.bbbbbblack.service;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.BookListVo;

import java.util.List;

public interface AdminBorrowService {
    Result<List<BookListVo>> getPersonalBorrow(String userId);

    Result confirmCert(String bookId);

    Result returnBook(String bookId);
}
