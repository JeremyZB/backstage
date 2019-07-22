package com.bobo.framework.manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.bobo.framework.msg.ResponseMsg;
import com.bobo.framework.utils.JpException;

/**
 * @ClassName: ECManager
 * @Description: TODO
 * @author: leven
 * @date: 2016年9月21日 下午5:01:10
 */
public class RetCodeManager implements InitializingBean {
	private static Logger logger =  LoggerFactory.getLogger(RetCodeManager.class);

	private static RetCodeManager global = null;

	String config;

	Map<String, RetCode> codes = new HashMap<String, RetCode>();

	public static RetCodeManager getGlobal() {
		return global;
	}

	public void afterPropertiesSet() throws Exception {
		global = this;
		this.init();
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Map<String, RetCode> getCodes() {
		return codes;
	}

	public void setCodes(Map<String, RetCode> codes) {
		this.codes = codes;
	}

	public RetCode getCode(String code) {
		return codes.get(code);
	}

	public void init() {
		if (config != null && !"".equals(config)) {
			load();
		}
	}

	void load() {
		try {
			logger.info("******************初始化错误代码**********************");
			PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resourcePatternResolver.getResources(config);
			for (int i = 0; i < resources.length; i++) {
				loadFile(resources[i]);
			}
		} catch (Exception e) {
			logger.error("Init error codes failed", e);
		}
	}

	void loadFile(Resource res) throws Exception {
		// Document document=XMLParser.parserExt(config);
		InputStream is = null;
		try {
			// logger.info("-----------------------locale:"+Locale.getDefault());
			is = res.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				// line=new String(line.getBytes(),"GBK");
				if (!line.startsWith("#")) {
					line = line.trim();
					String[] ss = line.split("[\\s]", 2);
					if (ss.length == 2) {
						RetCode ec = new RetCode(ss[0].trim(), ss[1].trim());
						codes.put(ec.getCode(), ec);
						logger.info("错误代码:" + ec.getCode() + "," + ec.getMessage());
					}
				}
			}
		} finally {
			if (is != null)
				is.close();
		}
	}

	public void setErroCode(ResponseMsg<?> response, String code) {
		RetCode ec = this.getCode(code);
		response.setRetCode(code);
		if (ec == null) {
			logger.warn("错误代码没有定义：" + code);
		} else
			response.setRetMsg(ec.getMessage());
	}

	/**
	 * @Title: throwException
	 * @Description: TODO
	 * @param code
	 * @throws Exception
	 * @return: void
	 */
	public void throwException(String code, String fName) throws Exception {
		RetCode ec = this.getCode(code);
		String msg = "";
		if (ec == null) {
			logger.warn("错误代码没有定义：" + code);
			msg = code;
		} else {
			msg = ec.getMessage();
		}
		throw new JpException(code, "[" + fName + "]" + msg);
	}

	/**
	 * @Title: throwException
	 * @Description: TODO
	 * @param code
	 * @throws Exception
	 * @return: void
	 */
	public void throwException(String code) throws Exception {
		RetCode ec = this.getCode(code);
		String msg = "";
		if (ec == null) {
			logger.warn("错误代码没有定义：" + code);
			msg = code;
		} else {
			msg = ec.getMessage();
		}
		throw new JpException(code, msg);
	}
	
	
	public void throwException(String code, String[] params) throws Exception
    {
	    RetCode ec = this.getCode(code);
        String msg = "";
        if (ec == null)
        {
            logger.warn("错误代码没有定义：" + code);
            msg = code;
        }
        else
        {
            msg = ec.getMessage();

            if (null == params || params.length == 0)
            {
                msg = msg.replaceAll("@_@", "");
            }
            else
            {
                for (int i = 0; i < params.length; i++)
                {
                    msg = msg.replaceFirst("@_@", params[i]);
                }
                msg = msg.replaceAll("@_@", "");
            }
        }
        throw new JpException(code, msg);
    }
}
