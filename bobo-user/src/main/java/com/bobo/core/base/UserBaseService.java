package com.bobo.core.base;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.bobo.framework.msg.QueryRequestMsg;
import com.bobo.framework.msg.QueryResponseMsg;
import com.bobo.framework.msg.RequestMsg;
import com.bobo.framework.msg.ResponseMsg;
import com.bobo.framework.service.BaseService;
import com.bobo.framework.utils.CheckUtils;
import com.bobo.framework.utils.DateUtil;
import com.bobo.framework.utils.JsonUtil;
import com.bobo.framework.utils.SpringUtils;
import com.bobo.cfg.OssConfig;
import com.bobo.core.mapper.master.ExceptionLogMapper;
import com.bobo.core.mapper.master.UserMapper;
import com.bobo.core.model.BaseModel;
import com.bobo.core.util.RedisUtils;
import com.bobo.core.util.ValidatorUtils;


@Component
public class UserBaseService extends BaseService {
	private static final Logger logger = LoggerFactory.getLogger(UserBaseService.class);

	@Resource
	protected DataSourceTransactionManager txManager;


	/*@Resource(name = "myTaskExecutor")
	protected ThreadPoolTaskExecutor executor;*/
	
	@Resource
	public RedisUtils redisUtils;

	
	@Resource
	public JdbcTemplate jdbc;
	
	@Resource
	protected JdbcTemplate jdbcTemplate;
	@Resource
	public UserMapper userMapper;

	/**
	 * @Title: bigenTransaction
	 * @Description: 开启事务
	 * @param definition
	 * @return
	 * @return: TransactionStatus
	 */
	public TransactionStatus txBegin(Integer definition) {
		txManager.setRollbackOnCommitFailure(true);
		Integer tdf = null;
		if (definition == null) {
			tdf = TransactionDefinition.PROPAGATION_REQUIRED;
		} else {
			tdf = definition;
		}
		TransactionStatus txstatus = txManager.getTransaction(new DefaultTransactionDefinition(tdf.intValue()));
		logger.info("事务已开启");
		return txstatus;
	}

	/**
	 * @Title: commitTransaction
	 * @Description: 事务提交
	 * @param status
	 * @return: void
	 */
	public void txCommit(TransactionStatus txstatus) {
		txManager.commit(txstatus);
		txstatus = null;
		logger.info("事务已提交");
	}

	/**
	 * @Title: rollbackTransaction
	 * @Description: 事务回滚
	 * @param status
	 * @return: void
	 */
	public void txRollback(TransactionStatus txstatus) {
		txManager.rollback(txstatus);
		logger.info("事务已回滚");
	}

	/**
	 * finally调用
	 */
	public void rollBack(TransactionStatus tStatus) {
		if (tStatus != null && !tStatus.isCompleted()) {
			this.txRollback(tStatus);
		}
	}
	


	/**
	 * 异常报错
	 */
	protected <T> ResponseMsg<T> handleWithException(ResponseMsg<T> resp, Exception e) {
		e.printStackTrace();
		handleException(resp, e);
		return resp;
	}

	/**
	 * 返回不带分页结果
	 */
	public <T> ResponseMsg<T> returnResp(ResponseMsg<T> resp, T result) {
		resp.setResult(result);
		setResCode(resp, succes);
		return resp;
	}

	/**
	 * 返回带分页结果
	 */
	public <T, V> QueryResponseMsg<T> returnPageResp(QueryResponseMsg<T> resp, T result, List<V> list, int pageNo) {
		PageInfo<V> pageInfo = new PageInfo<>(list);
		setResult(result, list);
		resp.setBeginnum(pageNo);
		resp.setTotal((int) pageInfo.getTotal());
		resp.setResult(result);
		setResCode(resp, succes);
		return resp;
	}

	@SuppressWarnings("rawtypes")
	public <T, V> void setResult(T result, List<V> list) {
		if (null != result) {
			Field[] fields = result.getClass().getDeclaredFields();
			for (Field fd : fields) {
				fd.setAccessible(true);

				Class clazz = fd.getType();
				if ("java.util.List".equals(clazz.getName())) {// list才处理
					try {
						fd.set(result, list);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
	}



	/**
	 * 头部check
	 * 
	 * @param 需要分页的传isPage=1 有参数的 isParamCheck=1
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public <T> void comCheck(RequestMsg<T> req, Integer isPage, Integer isParamCheck) throws Exception {

		if (req instanceof QueryRequestMsg) {

			if (isPage != null && isPage == 1) {
				if (((QueryRequestMsg) req).getPageno() == 0 || ((QueryRequestMsg) req).getPagesize() == 0) {
					((QueryRequestMsg) req).setPageno(1);
					((QueryRequestMsg) req).setPagesize(10);
					// CheckUtils.throwExceptionMsg("pageno、pagesize非空");
				}
			}
		}
		Integer organId = req.getOrganId();
		CheckUtils.checkNull(organId, "organId");
		Integer userId = req.getUserId();
		CheckUtils.checkNull(userId, "userId");
		CheckUtils.checkNull(req.getParam(), "param");
		if (isParamCheck != null && isParamCheck == 1) {

			ValidatorUtils.validateEntity(req.getParam());
			
			Field[] fields = req.getParam().getClass().getDeclaredFields();
			if(null!=fields){
				Set<String> basetype = Sets.newHashSet("short", "int", "long", "char", "boolean", "float", "double",
						"java.lang.byte", "java.lang.short", "java.lang.long", "java.lang.float", "java.lang.double",
						"java.lang.boolean", "java.lang.integer", "java.math.bigdecimal", "java.lang.string");
   
				for (Field field : fields) {
					field.setAccessible(true);
					String typeName = field.getType().getName();
					if (!basetype.contains(typeName.toLowerCase())) {// 校验引用类型属性值
						ValidatorUtils.validateEntity(field.get(req.getParam()));
					}
				}			
			}
		}
	}

	/**
	 * 新增设置数据库公共字段值
	 */
	protected <T extends BaseModel> void setDbCommonVal(T pojo, Integer userId) {
		pojo.setCreateUserId(userId);
		pojo.setUpdateUserId(userId);
		pojo.setCreateTime(DateUtil.getToday());
		pojo.setUpdateTime(pojo.getCreateTime());
	}

	/**
	 * 修改设置数据库公共字段值
	 */
	protected <T extends BaseModel> void modDbCommonVal(T pojo, Integer userId) {
		pojo.setUpdateUserId(userId);
		pojo.setUpdateTime(DateUtil.getToday());

	}
	
	/**
	 * @返回阿里云地址
	 */
	public static String getOSSPath() {
		OssConfig cfg = SpringUtils.getBean(OssConfig.class);
		String bucketName = cfg.getBucketName();
		String endPoint = cfg.getEndpoint();
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(bucketName).append(".").append(endPoint).append("/");
		return sb.toString();
	}

	
}
