package org.basis.framework.message;

import org.basis.framework.message.bean.MessageAccount;
import org.basis.framework.message.bean.MessageDetailsInfo;

/**
 * @Description 消息接口
 * @Author ChenWenJie
 * @Data 2021/10/22 5:47 下午
 */
public interface IMessage {

    /**
     * 发送消息
     * @param account 发送账号
     * @param detailsInfo 详细详情
     * @return
     */
    boolean sendMessage(MessageAccount account, MessageDetailsInfo detailsInfo);

    /**
     * 接收消息
     * @param account 接收账号
     * @return
     */
    boolean receiveMessage(MessageAccount account);
}
