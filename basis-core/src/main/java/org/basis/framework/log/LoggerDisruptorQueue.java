package org.basis.framework.log;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *@Description
 *   日志处理队列
 *@Author ChenWenJie
 *@Data 2021/11/24 5:08 下午
 **/
public class LoggerDisruptorQueue {

    private Executor executor = Executors.newCachedThreadPool();

    private LoggerEventFactory factory = new LoggerEventFactory();

    private FileLoggerEventFactory fileLoggerEventFactory = new FileLoggerEventFactory();

    private int bufferSize = 2 * 1024;

    private Disruptor<LoggerEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);;

    private Disruptor<FileLoggerEvent> fileLoggerEventDisruptor = new Disruptor<>(fileLoggerEventFactory, bufferSize, executor);;

    private static  RingBuffer<LoggerEvent> ringBuffer;

    private static  RingBuffer<FileLoggerEvent> fileLoggerEventRingBuffer;

    public LoggerDisruptorQueue(EventHandler eventHandler, EventHandler fileLoggerEventHandler) {
        disruptor.handleEventsWith(eventHandler);
        fileLoggerEventDisruptor.handleEventsWith(fileLoggerEventHandler);
        this.ringBuffer = disruptor.getRingBuffer();
        this.fileLoggerEventRingBuffer = fileLoggerEventDisruptor.getRingBuffer();
        disruptor.start();
        fileLoggerEventDisruptor.start();
    }

    /**
     * 控制台日志发布事件
     * @param log
     */
    public static void publishEvent(LoggerMessage log) {
        if(ringBuffer == null) return;
        long sequence = ringBuffer.next();
        try {
            LoggerEvent event = ringBuffer.get(sequence);
            event.setLog(log);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 日志文件 发布事件
     * @param log
     */
    public static void publishEvent(String log) {
        if(fileLoggerEventRingBuffer == null) return;
        long sequence = fileLoggerEventRingBuffer.next();
        try {
            FileLoggerEvent event = fileLoggerEventRingBuffer.get(sequence);
            event.setLog(log);
        } finally {
            fileLoggerEventRingBuffer.publish(sequence);
        }
    }

}
