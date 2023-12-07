package com.bbbbbblack.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (DepositOrder)表实体类
 *
 * @author makejava
 * @since 2022-11-08 14:40:49
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositOrder implements Serializable {
    public static final long serialVersionUID = 2351347623L;

    //商户订单号
    @TableId
    private Long outTradeNo;
    //支付宝交易凭证号（交易未完成时为空）
    private String tradeNo;
    //交易名称
    private String subject;
    //交易金额
    private Double totalAmount;
    //发布订单用户id
    private Long userId;
    //付款时间
    private String gmtPayment;
    //交易状态（0为未支付，1为已支付）
    private Integer tradeStatus;
    //订单生成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date createTime;
}

