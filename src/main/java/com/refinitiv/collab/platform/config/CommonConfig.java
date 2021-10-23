package com.refinitiv.collab.platform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    /**
     * Standard object mapper for conversion from String to JSON-based Java object.
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
