package com.bobo.cfg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisCfg
{

	private String host;
	private int port;
	private int timeout;

	@Bean()
	@ConfigurationProperties(prefix = "redis.pool")
	public JedisPoolConfig getJedisPoolConfig()
	{
		JedisPoolConfig bean = new JedisPoolConfig();
		return bean;
	}

	@Bean()
	public JedisPool getJedisPool()
	{
		JedisPool bean = new JedisPool(getJedisPoolConfig(), host, port, timeout);
		return bean;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

}
