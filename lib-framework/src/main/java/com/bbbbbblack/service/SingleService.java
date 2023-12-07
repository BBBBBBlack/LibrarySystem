package com.bbbbbblack.service;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.BookSingleVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface SingleService {
    Result<List<BookSingleVo>> getCopy(String isbn);

    void createSingleQRCode(String bookId, HttpServletResponse response);
}
