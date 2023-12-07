package com.bbbbbblack.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Objects;

@TableName("book_commend")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCommend implements Serializable {

    public static final long serialVersionUID = 986934659865L;
    @TableId
    private Long userId;
    private String clientId;
    private Integer days;
    private Long version;
    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCommend commend = (BookCommend) o;
        return Objects.equals(userId, commend.userId) && Objects.equals(clientId, commend.clientId) && Objects.equals(days, commend.days) && Objects.equals(status, commend.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clientId, days);
    }
}
