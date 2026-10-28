package com.fiserv.payment.connector.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class PortLoggerFactory {

    private static final ConcurrentHashMap<String, LoggersContainer> portLoggersMap = new ConcurrentHashMap<>();
    private static final String APP_LOG_PATTERN = "%d{dd-MM-yyyy HH:mm:ss.SSS} [%thread] %-5level %logger{0}.%M - %msg%n";
    private static final String REQ_RES_LOG_PATTERN = "%d{dd-MM-yyyy HH:mm:ss.SSS} %-5level %logger{0}.%M - %msg%n";

    public static class LoggersContainer {
        public final org.slf4j.Logger appLogger;
        public final org.slf4j.Logger reqResLogger;
        public final Appender<ILoggingEvent> rawAppAppender;

        public LoggersContainer(org.slf4j.Logger appLogger, org.slf4j.Logger reqResLogger, Appender<ILoggingEvent> rawAppAppender) {
            this.appLogger = appLogger;
            this.reqResLogger = reqResLogger;
            this.rawAppAppender = rawAppAppender;
        }
    }

    public static LoggersContainer getLoggersForPort(String port) {
        return portLoggersMap.computeIfAbsent(port, PortLoggerFactory::createLoggersForPort);
    }

    private static LoggersContainer createLoggersForPort(String port) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger appLogger = context.getLogger("PortAppLogger-" + port);
        appLogger.setAdditive(false);
        appLogger.setLevel(ch.qos.logback.classic.Level.INFO);
        FileAppender<ILoggingEvent> appAppender = new FileAppender<>();
        appAppender.setContext(context);
        appAppender.setName("FILE-APP-" + port);
        appAppender.setFile("./logs/" + port + "/paymentconnector.log");
        PatternLayoutEncoder appEncoder = new PatternLayoutEncoder();
        appEncoder.setContext(context);
        appEncoder.setPattern(APP_LOG_PATTERN);
        appEncoder.start();
        appAppender.setEncoder(appEncoder);
        appAppender.start();
        appLogger.addAppender(appAppender);
        // 2. Setup Isolated Port-Specific payment-req-res.log
        Logger reqResLogger = context.getLogger("PortReqResLogger-" + port);
        reqResLogger.setAdditive(false);
        reqResLogger.setLevel(ch.qos.logback.classic.Level.DEBUG);
        FileAppender<ILoggingEvent> rrAppender = new FileAppender<>();
        rrAppender.setContext(context);
        rrAppender.setName("FILE-REQRES-" + port);
        rrAppender.setFile("./logs/" + port + "/payment-req-res.log");
        PatternLayoutEncoder rrEncoder = new PatternLayoutEncoder();
        rrEncoder.setContext(context);
        rrEncoder.setPattern(REQ_RES_LOG_PATTERN);
        rrEncoder.start();
        rrAppender.setEncoder(rrEncoder);
        rrAppender.start();
        reqResLogger.addAppender(rrAppender);
        Logger rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        ch.qos.logback.core.filter.Filter<ILoggingEvent> threadFilter = new ch.qos.logback.core.filter.Filter<>() {
            @Override
            public ch.qos.logback.core.spi.FilterReply decide(ILoggingEvent event) {
                String activePort = DynamicPortFilter.getCurrentRequestPort();
                // If the log happens on a thread processing a request for THIS port, intercept it!
                if (port.equals(activePort)) {
                    return ch.qos.logback.core.spi.FilterReply.ACCEPT;
                }
                return ch.qos.logback.core.spi.FilterReply.DENY;
            }
        };
        threadFilter.start();
        appAppender.addFilter(threadFilter);
        rootLogger.addAppender(appAppender);

        return new LoggersContainer(appLogger, reqResLogger, appAppender);
    }
}
