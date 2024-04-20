package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MicrometerConfiguration {
    private final MeterRegistry meterRegistry;
    private final ApplicationConfig applicationConfig;

    @Bean
    public Counter micrometerCounter() {
        return Counter.builder(applicationConfig.micrometer()).register(meterRegistry);
    }
}
