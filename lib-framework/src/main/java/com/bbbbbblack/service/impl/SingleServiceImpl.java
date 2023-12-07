package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.cache.BookCache;
import com.bbbbbblack.dao.SingleDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.BookSingle;
import com.bbbbbblack.domain.vo.BookSingleVo;
import com.bbbbbblack.service.SingleService;
import com.bbbbbblack.utils.JwtUtil;
import com.bbbbbblack.utils.QrCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SingleServiceImpl implements SingleService {
    @Autowired
    BookCache bookCache;
    @Autowired
    SingleDao singleDao;

    @Override
    public Result<List<BookSingleVo>> getCopy(String isbn) {
        Book book = bookCache.getBookByISBN(isbn);
        QueryWrapper<BookSingle> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", book.getId()).eq("status", 0);
        List<BookSingle> bookSingles = singleDao.selectList(wrapper);
        List<BookSingleVo> bookSingleVos = new ArrayList<>();
        for (BookSingle single : bookSingles) {
            BookSingleVo bookSingleVo = new BookSingleVo(book);
            bookSingleVo.setBookId(single.getBookId());
            bookSingleVos.add(bookSingleVo);
        }
        return new Result<>(200, "查询一类书的所有在馆副本", bookSingleVos);
    }

    @Override
    public void createSingleQRCode(String bookId, HttpServletResponse response) {

        String jwt = JwtUtil.createJWT(bookId, -1L);
        BufferedImage image;
        OutputStream outputStream;
        try {
            image = QrCodeUtils.createImage("/borrow/addToCert/" + jwt, null, false);
            response.setContentType("image/png");
            outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
