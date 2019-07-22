package com.bobo.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import tk.mybatis.spring.annotation.MapperScan;

import com.alibaba.fastjson.JSONObject;
import com.bobo.framework.docs.ScanNotes;

@Controller
@SpringBootApplication
@ComponentScan(basePackages = { "com.bobo","com.bobo.framework"})
@MapperScan(basePackages={"com.bobo.core.mapper.master"})

public class Application extends SpringBootServletInitializer implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("user服务启动完成!");
		ScanNotes.scan(Application.class);

	}

	@ResponseBody
	@RequestMapping("/")
	public String index() {
		return "index ResponseBody";
	}
	@ResponseBody
	@PostMapping("/test")
	public String index11(@RequestBody JSONObject param) {
		return "index ResponseBody";
	}
	
	//下面两个方法引入restTemplate
	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory factory = 
				new HttpComponentsClientHttpRequestFactory();

		factory.setConnectTimeout(60000);
		factory.setReadTimeout(60000);

		return factory;
	}

	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
		 RestTemplate restTemplate=new RestTemplate(clientHttpRequestFactory);
		return restTemplate;
	}
}
