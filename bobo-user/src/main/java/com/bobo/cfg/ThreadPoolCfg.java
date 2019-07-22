package com.bobo.cfg;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
@Configuration
public class ThreadPoolCfg{

	
	private static int corePoolSize=24;
	private static int maxPoolSize=50;
	private static int queueCapacity=10000;

	@Bean("myTaskExecutor")
	public ThreadPoolTaskExecutor  getMyExecutor() {
		
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setQueueCapacity(queueCapacity);
		taskExecutor.setThreadNamePrefix("myTaskExecutor-");
		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.initialize();
		return taskExecutor;
	}




}
