package com.github.afanas10101111.dfl;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockMockConfig {

    @Bean
    public Clock clock() {
        return Mockito.mock(Clock.class);
    }
}
