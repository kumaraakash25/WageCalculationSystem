package com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan
@EnableWebMvc
public class SpringBootApplication extends SpringBootServletInitializer {

    private final static Log LOG = LogFactory.getLog(SpringBootApplication.class);

    public static void main(String[] args) {
        LOG.info("Initialising the Wage Calculation System...");
        SpringApplication.run(SpringBootApplication.class, args);
    }
}
