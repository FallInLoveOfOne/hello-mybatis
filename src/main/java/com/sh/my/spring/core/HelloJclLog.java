package com.sh.my.spring.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HelloJclLog {
    private static final Log logger = LogFactory.getLog(HelloJclLog.class);

    public static void main(String[] args) {
        logger.info(logger.getClass().getName());
        logger.info("Hello JCL Log!");
    }

}
