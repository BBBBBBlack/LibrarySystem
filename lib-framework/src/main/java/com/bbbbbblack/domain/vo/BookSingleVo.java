package com.bbbbbblack.domain.vo;

import com.bbbbbblack.domain.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSingleVo implements Serializable {
    public static final long serialVersionUID = 34683642487L;
    private String isbn;
    private String title;
    private String author;
    private Long bookId;
    private Integer status;//状态，0为在库，1为不可外借

    public BookSingleVo(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();
    }
}
