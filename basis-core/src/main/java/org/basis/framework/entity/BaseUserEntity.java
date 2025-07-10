package org.basis.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author ChenJie
 **/
@Data
public class BaseUserEntity {
    @ApiModelProperty(value = "创建者ID")
    @TableField("create_user_id")
    private Long createUserId;

    @ApiModelProperty(value = "更新者ID")
    @TableField("update_user_id")
    private Long updateUserId;
}
