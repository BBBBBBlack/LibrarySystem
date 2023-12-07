package com.bbbbbblack.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.bbbbbblack.handler.BlobAndStringHandler;
import com.bbbbbblack.handler.MyBlobTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.annotation.HandlesTypes;
import java.io.Serializable;
import java.sql.Blob;

/**
 * (Book)表实体类
 *
 * @author makejava
 * @since 2022-10-15 00:43:13
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book implements Serializable {
    public static final long serialVersionUID = 646497889878L;
    //书籍id
    private Long id;
    //书籍isbn号
    private String isbn;
    //书籍标题
    private String title;
    //书作者
    private String author;
    //出版社
    private String publisher;
    //版本号
    private String versionNum;
    //封面图片url
    @JsonProperty("cover_url")
    private String coverUrl;
    //书序
    private String preface;
    //目录
    private String catalogue;
    //内容简介
    private String introduction;
    //导读
    @TableField("`load`")
    private String load;
    //类别
    private Integer type;
    //价格
    private Double price;
    //总数
    private Integer total;
    //剩余数
    private Integer availableNum;
}

