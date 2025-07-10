package org.basis.framework.log;

import com.lmax.disruptor.EventFactory;

/**
 * @Description
 * @Author ChenJie
 **/
public class LoggerEventFactory implements EventFactory<LoggerEvent> {
    @Override
    public LoggerEvent newInstance() { return new LoggerEvent(); }
}
