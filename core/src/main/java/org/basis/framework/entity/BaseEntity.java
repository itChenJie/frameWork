package org.basis.framework.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 创建者id
     */
    @ApiModelProperty(value = "创建者ID")
    @TableField("create_user_id")
    private Long createUserId;
    /**
     * 更新者id
     */
    @ApiModelProperty(value = "更新者ID")
    @TableField("update_user_id")
    private Long updateUserId;
}
