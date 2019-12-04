package com.drp.legendbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@SpringBootApplication
public class LegendBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegendBackendApplication.class, args);
	}

	@Bean
	public Executor asyncServiceExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		final int processors = Runtime.getRuntime().availableProcessors();

		executor.setCorePoolSize(processors);
		executor.setMaxPoolSize(processors * 5);
		executor.setKeepAliveSeconds(60);
		executor.setQueueCapacity(processors * 2);
		executor.setThreadNamePrefix("async-thread-");
		//how to dispose the following threads when the pool is full
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);

		executor.initialize();
		return executor;
	}

}
