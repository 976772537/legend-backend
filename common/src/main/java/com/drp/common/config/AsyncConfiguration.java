package com.drp.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month05day  21:27:44
 */
@EnableAsync
@Configuration
public class AsyncConfiguration implements WebMvcConfigurer {
    @Bean
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        final int processors = Runtime.getRuntime().availableProcessors();

        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors * 5);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(processors * 2);
        executor.setThreadNamePrefix("sso-async-thread-");
        //how to dispose the following threads when the pool is full
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.initialize();
        return executor;
    }
}
