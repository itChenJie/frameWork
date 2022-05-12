package org.basis.framework.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.DateFormat;

/**
 * Log附加程序，对控制台日志进行处理
 * 使用Spring 注入的方式添加，也可以使用 extends Filter<ILoggingEvent> 进行配置，
 * 但存在问题 logback Filter 加载顺序太靠前，导致无法使用 bean
 * @author ChenWenJie
 * @since 2021/11/24 10:11
 */
public class ProcessLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    public void init(String level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(level);
        filter.setContext(context);
        filter.start();
        this.addFilter(filter);
        this.setContext(context);
        context.getLogger("ROOT").addAppender(ProcessLogAppender.this);

        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        IThrowableProxy tp = event.getThrowableProxy();
        LoggerMessage loggerMessage = new LoggerMessage(
                event.getMessage()
                , DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp())),
                event.getThreadName(),
                event.getLoggerName(),
                event.getLevel().levelStr
        );
        LoggerDisruptorQueue.publishEvent(loggerMessage);
    }
}