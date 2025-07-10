package org.basis.framework.log;

import com.lmax.disruptor.EventFactory;

/**
 * @Description 文件日志事件工厂类
 * @Author ChenJie
 **/
public class FileLoggerEventFactory implements EventFactory<FileLoggerEvent> {
    @Override
    public FileLoggerEvent newInstance() { return new FileLoggerEvent(); }
}
