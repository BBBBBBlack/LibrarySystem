package com.bbbbbblack.domain.vo;

import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.BookList;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookListVo implements Serializable {
    //书籍id
    private String bookId;
    //书籍isbn
    private String bookIsbn;
    //书名
    private String bookTitle;
    //加入书单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date addTime;
    //借书时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date borrowTime;
    //借书状态（0为书籍在借书栏中，1为借出未还，2为借出已还）
    private Integer status;

    public BookListVo(BookList bookList) {
        if (bookList != null) {
            this.bookId = bookList.getBookId().toString();
            this.bookIsbn = bookList.getBookIsbn();
            this.bookTitle = bookList.getBookTitle();
            this.addTime = bookList.getAddTime();
            this.borrowTime = bookList.getBorrowTime();
            this.status = bookList.getStatus();
        }
    }

    public BookListVo(Book book) {
        this.bookIsbn = book.getIsbn();
        this.bookTitle = book.getTitle();
    }
}
