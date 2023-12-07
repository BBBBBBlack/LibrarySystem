package com.bbbbbblack.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Search implements Serializable {
    private Integer type;//查询类型——1为书名，2为作者，三为内容
    private Integer bookType;
    private String keywords;//关键词
    private Integer highPrice;//最高价
    private Integer lowPrice;//最低价
    private Integer from;//起始记录序号
}
