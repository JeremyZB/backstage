package com.bobo.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.bobo.framework.manager.AccessTokenWhiteManager;
import com.bobo.framework.manager.RetCodeManager;
import com.bobo.framework.manager.TxIdManager;

@Configuration
public class InitCfg {

	@Bean(name = "txIdManager")
	@Lazy(value = false)
	public TxIdManager getTxIdManager() {
		TxIdManager txIdManager = new TxIdManager();
		txIdManager.setConfig("classpath:/com/bobo/**/service/**/*Service.class");
		return txIdManager;
	}

	@Bean(name = "retCodeManager")
	@Lazy(value = false)
	public RetCodeManager getRetCodeManager() {
		RetCodeManager retCodeManager = new RetCodeManager();
		retCodeManager.setConfig("classpath:/retcode/**/*.txt");
		return retCodeManager;
	}

	@Bean(name = "accessTokenWhiteManager")
	@Lazy(value = false)
	public AccessTokenWhiteManager getAccessTokenWhiteManager() {
		AccessTokenWhiteManager accessTokenWhiteManager = new AccessTokenWhiteManager();
		accessTokenWhiteManager.setPackages("com.bobo.core.service");
		return accessTokenWhiteManager;
	}
}
