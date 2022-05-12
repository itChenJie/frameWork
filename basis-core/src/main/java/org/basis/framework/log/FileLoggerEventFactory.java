package org.basis.framework.log;

import com.lmax.disruptor.EventFactory;

/**
 * @Description 文件日志事件工厂类
 * @Author ChenWenJie
 * @Data 2021/11/24 5:30 下午
 **/
public class FileLoggerEventFactory implements EventFactory<FileLoggerEvent> {
    @Override
    public FileLoggerEvent newInstance() { return new FileLoggerEvent(); }
}
