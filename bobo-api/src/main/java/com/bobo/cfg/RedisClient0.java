package com.bobo.cfg;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author test
 * 
 */
@Component
public class RedisClient0
{

	static final int db = 0;

	private static final Logger log = LoggerFactory.getLogger(RedisClient0.class);

	@Autowired(required = false)
	private JedisPool jedisPool;

	/**
	 * 初始化Jedis
	 * 
	 * @param dbIndex
	 *            DataBase
	 * @return
	 */
	private Jedis initJedis()
	{
		Jedis jedis = jedisPool.getResource();
		jedis.select(db);
		return jedis;
	}

	/***
	 * <p>
	 * Description: 返回资源
	 * </p>
	 * 
	 * @author wenquan
	 * @date 2017�?1�?5�?
	 * @param
	 */
	public static void returnResource(Jedis jedis)
	{
		if (jedis != null)
		{
			jedis.close();
		}
	}

	/**
	 * 根据选择的DataBase 获取key对应的string�?,如果key不存在返回null
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key)
	{
		String result = null;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.get(key);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 根据选择的DataBase 获取key对应的string�?,如果key不存在返回null
	 * 
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key)
	{
		byte[] result = null;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.get(key);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 存储数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value)
	{
		String result = null;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.set(key, value);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * @Title: setex
	 * @Description:
	 */
	public String setex(String key, int seconds, String value)
	{
		String result = null;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.setex(key, seconds, value);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * @Title: expire
	 * @Description: 更新key的过期时�?
	 */
	public void expire(String key, int seconds)
	{
		Jedis jedis = initJedis();
		try
		{
			jedis.expire(key, seconds);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
	}

	/**
	 * 判断�?个key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key)
	{
		Boolean result = false;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.exists(key);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 根据key删除�?条缓存数�?
	 * 
	 * @param key
	 * @return
	 */
	public Long del(String key)
	{
		Long result = null;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.del(key);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置key对应的�?�为string类型的value，并指定此键值对应的有效期�??
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return
	 */
	public String setex(byte[] key, int seconds, byte[] value)
	{
		String result = null;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.setex(key, seconds, value);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 
	 * @Title: push
	 * @Description: 放入队列
	 * @param key
	 * @param value
	 * @return
	 * @return: Long
	 */
	public Long push(String key, String value)
	{
		Long result = null;
		Jedis jedis = initJedis();
		try
		{
			log.info("**********************K:" + key + ",V:" + value + "**********************");
			result = jedis.rpush(key, value);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 
	 * @Title: lrange
	 * @Description: TODO
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @return: List<String>
	 */
	public List<String> lrange(String key, long start, long end)
	{
		List<String> result = null;
		Jedis jedis = initJedis();
		try
		{
			result = jedis.lrange(key, start, end);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			returnResource(jedis);
		}
		return result;
	}
}