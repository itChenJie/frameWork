package org.basis.framework.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @Description 基础Entity
 * @Author ChenWenJie
 * @Data 2021/7/4 5:03 下午
 **/
@Data
public class BaseEntity {
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 创建者id
     */
    @TableField("create_user_id")
    private Long createUserId;
    /**
     * 更新者id
     */
    @TableField("update_user_id")
    private Long updateUserId;
}
