package org.basis.framework.message.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * 消息详情信息
 * @Author ChenWenJie
 * @Data 2021/10/22 5:57 下午
 **/
@Data
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
