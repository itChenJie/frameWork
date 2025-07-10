package org.basis.framework.message.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description
 * 消息详情信息
 * @Author ChenJie
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDetailsInfo {

    /**
     * 收件人
     */
    private MessageAccount receiveAccount;

    /**
     * 邮件标题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String body;

    /**
     * 接收/发送时间
     */
    private Date time;

}
