package com.bbbbbblack.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2022-10-11 17:12:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{
    public static final long serialVersionUID = 14536587698L;
    @TableId(type = IdType.AUTO)
    private Long id;
    //绑定微信后的open_id
    private String openId;
    //绑定邮箱
    private String email;
    //密码
    private String password;
    //昵称（若绑定微信，则为微信昵称，否则为随机字符串）
    private String nickName;
    //头像（若绑定微信，则为微信昵称，否则为默认）
    private String headImgUrl;
    //用户所缴纳的押金
    private Double account;
    //用户可支配的押金
    private Double accountRest;
    //用户状态
    private Integer status;
}

