package com.bbbbbblack.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {
    public static final Long pageSize = 5L;
    private Long total;
    private List<T> res;
}
