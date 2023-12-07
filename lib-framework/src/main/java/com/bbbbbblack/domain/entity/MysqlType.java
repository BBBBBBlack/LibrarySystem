package com.bbbbbblack.domain.entity;

import lombok.Data;

@Data
public class MysqlType {
    private String id;//日记id
    private String create_by;//记录者
    private String subject;//日记标题
    private String content;//日记内容
    private String create_time;//写入时间
}
