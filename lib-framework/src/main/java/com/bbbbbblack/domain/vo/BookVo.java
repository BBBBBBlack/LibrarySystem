package com.bbbbbblack.domain.vo;

import com.bbbbbblack.domain.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookVo {
    private String isbn;
    private String author;
    private String title;
    private String coverUrl;

    public BookVo(Book book) {
        this.isbn = book.getIsbn();
        this.author = book.getAuthor();
        this.title = book.getTitle();
        this.coverUrl = book.getCoverUrl();
    }
}
