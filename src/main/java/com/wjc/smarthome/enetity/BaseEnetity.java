package com.wjc.smarthome.enetity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 王建成
 * @date 2022/3/31--10:19
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BaseEnetity {

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    @TableField(fill = FieldFill.INSERT,condition = SqlCondition.LIKE)
    private String createUserName;

    @TableField(fill = FieldFill.UPDATE,condition = SqlCondition.LIKE)
    private String updateUserName;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public BaseEnetity(Long createUserId, String createUserName,  LocalDateTime createTime) {
        this.createUserId = createUserId;
        this.createUserName = createUserName;
        this.createTime = createTime;
    }
}
