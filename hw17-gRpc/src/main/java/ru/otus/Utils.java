package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static void sleep(long second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            logger.error("Error", e);
        }
    }
}