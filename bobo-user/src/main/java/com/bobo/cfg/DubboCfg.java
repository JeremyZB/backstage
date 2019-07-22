package com.bobo.cfg;

import java.util.Map;










import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

















//import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.google.common.collect.Maps;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.bobo.framework.dubbo.IDubboService;

@Configuration
@ConfigurationProperties("dubbox")
public class DubboCfg {

	private String address;
	private String appName;
	private int port;
	private String protocalName;
	private String version;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocalName() {
		return protocalName;
	}

	public void setProtocalName(String protocalName) {
		this.protocalName = protocalName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 *
	 * 与<dubbo:annotation/>相当.提供方扫描带有@com.alibaba.dubbo.init.annotation.
	 * Reference的注解类
	 */
	@Bean
	public static AnnotationBean annotationBean() {
		AnnotationBean annotationBean = new AnnotationBean();
		annotationBean.setPackage("com.bobo");// 多个包可使用英文逗号隔开
		return annotationBean;
	}

	/**
	 * 与<dubbo:consumer/>相当
	 */
	@Bean
	public ConsumerConfig consumerConfig() {
		ConsumerConfig config = new ConsumerConfig();
		config.setTimeout(5000);
		config.setCheck(false);
		config.setRegistry(registryConfig());
		config.setRetries(0);
		return config;
	}

	/**
	 * 由《dubbo:application》转换过来
	 **/
	@Bean(name = "applicationConfig")
	public ApplicationConfig applicationConfig() {
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setLogger("slf4j");
		applicationConfig.setName(appName);
		return applicationConfig;
	}

	/**
	 * 与<dubbo:registry/>相当
	 */
	@Bean(name = "registryConfig")
	public RegistryConfig registryConfig() {
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress(address);
		registryConfig.setProtocol(protocalName);
		registryConfig.setId("dubbo");
		registryConfig.setTimeout(60000);
		registryConfig.setCheck(false);
		registryConfig.setFile(".cache/dubbo-registry-re.cache");
		return registryConfig;
	}

	/**
	 * 与<dubbo:protocol/>相当
	 */
	@Bean(name = "protocolConfig")
	public ProtocolConfig protocolConfig() {
		ProtocolConfig config = new ProtocolConfig();
		config.setName("dubbo");
		config.setPort(port);
		return config;
	}

	/**
	 * 与<dubbo:provider/>相当
	 */
	@Bean(name = "providerConfig")
	public ProviderConfig providerConfig() {
	/*	ProviderConfig providerConfig = new ProviderConfig();
		Map<String, String> parameters = Maps.newHashMap();
		parameters.put("redisClient0", "redisClient0");

		providerConfig.setParameters(parameters);
		providerConfig.setFilter("accessTokenFilter");
		providerConfig.setFilter("logFilter");
		providerConfig.setTimeout(1200000);
		return providerConfig;*/
		
		ProviderConfig providerConfig = new ProviderConfig();
		Map<String, String> parameters = Maps.newHashMap();
		parameters.put("redisClient0", "redisClient0");
	//	parameters.put("coreLogsMapper", "coreLogsMapper");

		providerConfig.setParameters(parameters);
		//providerConfig.setFilter("accessTokenFilter,logFilter");
		return providerConfig;
		
		
	}

	/**
	 * 与<dubbo:service/>相当
	 */
	@Bean
	@DependsOn({ "applicationConfig", "registryConfig", "protocolConfig", "providerConfig" })
	public ServiceConfig<IDubboService> serviceConfig(@Qualifier(value = "iDubboService") IDubboService iDubboService) {

		System.out.println("init serviceConfig................................");

		ServiceConfig<IDubboService> config = new ServiceConfig<IDubboService>();
		config.setApplication(this.applicationConfig());
		config.setRegistry(this.registryConfig());
		config.setProtocol(this.protocolConfig());
		config.setProvider(this.providerConfig());
		config.setInterface(IDubboService.class);
		config.setRef(iDubboService);
		config.setExecutes(1000);
		config.setTimeout(1200000);
		config.setVersion(version);
		config.export();
		return config;
	}
	
	/**
	 * 回调的时候会掉这个方法*/
	@Bean(name = "ttsService")
	public IDubboService referenceConfig() {
		ReferenceConfig<IDubboService> config = new ReferenceConfig<IDubboService>();
		config.setId("ttsService");
		config.setInterface(IDubboService.class);
		config.setApplication(this.applicationConfig());
		config.setRegistry(this.registryConfig());
		config.setTimeout(60000);
		config.setVersion("tts");
		config.setCheck(false);
		return config.get();
	}

}
