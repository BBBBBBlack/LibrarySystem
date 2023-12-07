package com.bbbbbblack.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.bbbbbblack.domain.vo.BookListVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * (BookList)表实体类
 *
 * @author makejava
 * @since 2022-11-03 00:25:36
 */
@SuppressWarnings("serial")
@Data
@TableName("book_list")
@AllArgsConstructor
@NoArgsConstructor
public class BookList implements Serializable {

    public static final long serialVersionUID = 3219287938479L;

    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //书籍id
    private Long bookId;
    //书籍isbn
    private String bookIsbn;
    //书名
    private String bookTitle;
    //加入书单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date addTime;
    //借书时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date borrowTime;
    //还书时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date returnTime;
    //借书状态（0为书籍在借书栏中，1为借出未还，2为借出已还）
    private Integer status;

    public BookList(BookListVo bookListVo) {
        this.bookId = Long.valueOf(bookListVo.getBookId());
        this.bookIsbn = bookListVo.getBookIsbn();
        this.bookTitle = bookListVo.getBookTitle();
        this.addTime = bookListVo.getAddTime();
    }

}

