package com.qf.springboot.config;

import com.qf.springboot.service.TestService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * TestService自动装配
 *
 * @author ly
 * @since 2026-03-05 15:32
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "test.service", name = "enabled", havingValue = "true")
public class TestServiceAutoConfiguration {
    @Bean
    public TestService testService() {
        return new TestService();
    }
}