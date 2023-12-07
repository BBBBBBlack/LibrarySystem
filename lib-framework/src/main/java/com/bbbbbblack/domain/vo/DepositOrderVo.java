package com.bbbbbblack.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.bbbbbblack.domain.entity.DepositOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class DepositOrderVo implements Serializable {
    public static final long serialVersionUID = 8912435609L;

    //商户订单号
    @TableId
    private String outTradeNo;
    //支付宝交易凭证号（交易未完成时为空）
    private String tradeNo;
    //交易名称
    private String subject;
    //交易金额
    private Double totalAmount;
    //付款时间
    private String gmtPayment;
    //订单生成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //将返回给前端的时间用此种形式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //将前端返回的数据用此种形式转换
    private Date createTime;
    //交易状态（0为未支付，1为已支付，2为退款）
    private Integer tradeStatus;

    public DepositOrderVo(DepositOrder order) {
        this.outTradeNo = order.getOutTradeNo().toString();
        this.tradeNo = order.getTradeNo();
        this.subject = order.getSubject();
        this.totalAmount = order.getTotalAmount();
        this.gmtPayment = order.getGmtPayment();
        this.createTime = order.getCreateTime();
        this.tradeStatus=order.getTradeStatus();
    }
}
