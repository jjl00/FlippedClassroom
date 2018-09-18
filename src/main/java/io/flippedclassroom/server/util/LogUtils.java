package io.flippedclassroom.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局日志
 */
public class LogUtils {
    private LogUtils() {
    }

    public static Logger getInstance() {
        return Inner.logger;
    }

    private static final class Inner {
        private static final Logger logger = LoggerFactory.getLogger("FlippedClassroom");
    }
}
