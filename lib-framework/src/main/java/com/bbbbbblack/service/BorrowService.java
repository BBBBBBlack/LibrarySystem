package com.bbbbbblack.service;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.BookListVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BorrowService {
    Result addToCert(String bookId);

    Result delCert(String bookId);

    Result<List<BookListVo>> showCert(String userId);

    Result<List<BookListVo>> showBorrowed(Integer type, String userId);

    void freshMyQR(HttpServletResponse response);
}
