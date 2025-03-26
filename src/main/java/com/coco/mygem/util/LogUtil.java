package com.coco.mygem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger systemLogger = LoggerFactory.getLogger("system");
    private static final Logger businessLogger = LoggerFactory.getLogger("business");
    private static final Logger errorLogger = LoggerFactory.getLogger("error");

    public static void info(String message) {
        systemLogger.info(message);
    }

    public static void info(String message, Object... args) {
        systemLogger.info(message, args);
    }

    public static void debug(String message) {
        systemLogger.debug(message);
    }

    public static void debug(String message, Object... args) {
        systemLogger.debug(message, args);
    }

    public static void warn(String message) {
        systemLogger.warn(message);
    }

    public static void warn(String message, Object... args) {
        systemLogger.warn(message, args);
    }

    public static void error(String message) {
        errorLogger.error(message);
    }

    public static void error(String message, Object... args) {
        errorLogger.error(message, args);
    }

    public static void error(String message, Throwable throwable) {
        errorLogger.error(message, throwable);
    }

    public static void error(String message, Throwable throwable, Object... args) {
        errorLogger.error(message, args, throwable);
    }

    public static void businessInfo(String message) {
        businessLogger.info(message);
    }

    public static void businessInfo(String message, Object... args) {
        businessLogger.info(message, args);
    }

    public static void businessError(String message) {
        businessLogger.error(message);
    }

    public static void businessError(String message, Object... args) {
        businessLogger.error(message, args);
    }

    public static void businessError(String message, Throwable throwable) {
        businessLogger.error(message, throwable);
    }

    public static void businessError(String message, Throwable throwable, Object... args) {
        businessLogger.error(message, args, throwable);
    }
} 