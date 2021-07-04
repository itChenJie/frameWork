package org.basis.framework.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/7/4 5:07 下午
 **/
@Data
public class BaseUserEntity {
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
