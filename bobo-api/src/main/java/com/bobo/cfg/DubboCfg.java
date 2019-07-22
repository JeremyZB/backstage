package com.bobo.cfg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;







import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.bobo.framework.dubbo.IDubboService;
import com.bobo.framework.spring.IDubboServiceFactory;

@Configuration
@ConfigurationProperties("dubbox")
public class DubboCfg
{
	private String appName;
	private String address;
	private String protocalName;
	private List<String> versions;

	/**
	 *
	 * �?<dubbo:annotation/>相当.提供方扫描带有@com.alibaba.dubbo.init.annotation.Reference的注解类
	 */
	@Bean
	public static AnnotationBean annotationBean()
	{
		AnnotationBean annotationBean = new AnnotationBean();
		annotationBean.setPackage("com.bobo");// 多个包可使用英文逗号隔开
		return annotationBean;
	}

	/**
	 * 由�?�dubbo:application》转换过�?，当前应用配置
	 *  
	 **/
	@Bean(name = "applicationConfig")
	public ApplicationConfig applicationConfig()
	{
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setLogger("slf4j");
		applicationConfig.setName(appName);
		return applicationConfig;
	}

	/**
	 * �?<dubbo:registry/>相当，连接注册中心配置
	 */
	@Bean(name = "registryConfig")
	public RegistryConfig registryConfig()
	{
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress(address);
		registryConfig.setProtocol(protocalName);
		registryConfig.setId("dubbo");
		registryConfig.setTimeout(60000);
		registryConfig.setCheck(false);
		return registryConfig;
	}

	/**
	 * �?<dubbo:consumer/>相当，服务消费者配置
	 */
	@Bean
	public ConsumerConfig consumerConfig()
	{
		ConsumerConfig config = new ConsumerConfig();
		config.setTimeout(5000);
		config.setCheck(false);
		config.setRegistry(registryConfig());
		config.setRetries(0);
		return config;
	}

	// ----service注册
	@Bean()
	public IDubboServiceFactory registerService()
	{
		IDubboServiceFactory factory = new IDubboServiceFactory();
		Map<String, IDubboService> serviceFactory = new HashMap<String, IDubboService>();
		
		
		for (String v : versions)
		{
			//引用远程服务配置
			ReferenceConfig<IDubboService> config = new ReferenceConfig<IDubboService>();
			config.setInterface(IDubboService.class);
			config.setApplication(this.applicationConfig());
			config.setRegistry(this.registryConfig());		
			config.setTimeout(60000);
			config.setVersion(v);
			config.setCheck(false);
			config.setRetries(0);
			serviceFactory.put(v, config.get());
		}

		factory.setServiceFactory(serviceFactory);
		return factory;
	}

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getProtocalName()
	{
		return protocalName;
	}

	public void setProtocalName(String protocalName)
	{
		this.protocalName = protocalName;
	}


	public List<String> getVersions()
	{
		return versions;
	}

	public void setVersions(List<String> versions)
	{
		this.versions = versions;
	}

}
