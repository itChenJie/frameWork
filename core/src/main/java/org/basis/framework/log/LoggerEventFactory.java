package org.basis.framework.log;

import com.lmax.disruptor.EventFactory;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/11/24 5:32 下午
 **/
public class LoggerEventFactory implements EventFactory<LoggerEvent> {
    @Override
    public LoggerEvent newInstance() { return new LoggerEvent(); }
}
