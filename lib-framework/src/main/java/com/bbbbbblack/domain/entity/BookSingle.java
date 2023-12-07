package com.bbbbbblack.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("book_single")
public class BookSingle implements Serializable {

    public static final long serialVersionUID = 23468746812L;
    @TableId
    private Long bookId;
    private Long groupId;
    private Integer status;
}
