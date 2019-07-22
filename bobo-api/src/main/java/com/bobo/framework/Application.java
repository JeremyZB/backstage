package com.bobo.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication(scanBasePackages ={"com.bobo"},exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})

public class Application extends SpringBootServletInitializer  implements CommandLineRunner
{
	private Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
	

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(this.getClass());
	}


	@Override
	public void run(String... args) throws Exception
	{
		logger.info("服务启动完成!");
	}

	@ResponseBody
	@RequestMapping("/")
	public String index()
	{
		return "index ResponseBody";
	}
}
