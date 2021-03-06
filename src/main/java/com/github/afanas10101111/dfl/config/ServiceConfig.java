package com.github.afanas10101111.dfl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@ComponentScan("com.github.afanas10101111.dfl.service")
public class ServiceConfig {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
