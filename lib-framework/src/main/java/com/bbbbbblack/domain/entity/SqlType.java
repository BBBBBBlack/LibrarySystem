package com.bbbbbblack.domain.entity;

import lombok.Data;

@Data
public class SqlType {
    private Integer id;//日记id
    private Integer create_by;//记录者
    private Integer subject;//日记标题
    private Integer content;//日记内容
    private Integer create_time;//写入时间
}
