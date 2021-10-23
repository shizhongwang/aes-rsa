package com.refinitiv.collab.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableCaching
@EnableSwagger2
@SpringBootApplication
@ComponentScans({
        @ComponentScan(value = "com.refinitiv.collab.platform.config"),
})
public class WebapiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebapiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebapiApplication.class);
    }
}

