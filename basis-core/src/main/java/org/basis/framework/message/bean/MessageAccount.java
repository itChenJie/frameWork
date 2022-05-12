package org.basis.framework.message.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * 消息账号对象
 * @Author ChenWenJie
 * @Data 2021/10/22 5:53 下午
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAccount {

    /**
     * 账号
     */
    private String account;

    /**
     * 第三方登陆授权码
     */
    private String password;
}
