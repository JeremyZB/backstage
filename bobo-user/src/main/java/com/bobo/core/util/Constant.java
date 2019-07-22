package com.bobo.core.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public final class Constant {

	/**
	 * @author  公共常量
	 */
	public static final class Common {
		public static final String DefaultTopic="repair_topic";
		public static final String sendTag="repair_send_tag";
		public static final String SEND_DELAY_TAG="repair_send_delay_tag";
		public static final String messageTag="repair_message_tag";//创建或者修改计划
		public static final String messagePayTag="repair_message_pay_tag";//支付

		public static final String sfj_Appcode = "REP";
		public static final String sfj_Channel = "ALL";
		public static final String sfj_retcode = "0";
		public static final long Day_Seconds = 24 * 60 * 60;//一天多少秒
		public static final String ORDERBy = "orderBy";



	}
	
	/**
	 * @author  redis
	 */
	public static final class Redis {
		public static final String rep = "jiaparts:rep:";
		public static final String Task002_RuleId = rep+"Task002_RuleId:";
		public static final String SEND_PLAN =  rep+"sendPlan:";
		public static final String mq =rep+"mq:";
		public static final String Task002 =rep+"task002:";
		/**
		 * 要事提醒完成
		 */
		public static final String remind_cpt =rep+"remind_cpt:";

	}
	
	public static enum RetCode {
		
		SUCCESS_CODE("成功","200"),FAIL_CODE("失败","400");
		
		RetCode(String value, String code) {
	        this.value = value;
	        this.code = code;
	    }

	    private String value;

	    private String code;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	    
	    
	  

	}
	
	
	
}
