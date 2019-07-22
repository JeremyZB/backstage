package com.bobo.cfg;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bobo.cfg.interceptor.ReleaseInterceptor;

@Configuration
public class WebMvcCfg extends WebMvcConfigurerAdapter
{
	
	@Resource(name="redisClient0")
	RedisClient0	redisClient0;
	
 

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		ReleaseInterceptor releaseInterceptor = new ReleaseInterceptor();
		releaseInterceptor.setRedisClient0(redisClient0);
		registry.addInterceptor(releaseInterceptor).addPathPatterns("/*/*").excludePathPatterns("/txinfo/*");
	}
}
