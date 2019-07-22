package com.bobo.framework.mybatis.dbo;

import java.io.Serializable;
import java.lang.reflect.Method;
import com.bobo.framework.utils.JpException;
import com.bobo.framework.utils.SpringUtils;

public class DataOperateMapperBean extends DataOperateAbstractBean {

	private static final long serialVersionUID = 1L;
	
	//数据操作类型
	protected String dbOperation;
		
	//被操作model对象对应的mapper对应类名
	protected String mapperClassName;
	
	//执行sql所需要的参数
	protected Object[] args;	
	
	//默认构造器
	public DataOperateMapperBean() {
		
	}
	
	//简便构造器1
	@SuppressWarnings({ "rawtypes", "unused" })
	public DataOperateMapperBean(String dbOperation, String mapperClassName, Object... args) {
		
		if(dbOperation == null || "".equals(dbOperation.trim())){
			throw new JpException("SYS-102", "DOB组件异常：dbOperation不能为空");
		}		
		
		if(mapperClassName == null || "".equals(mapperClassName.trim())){
			throw new JpException("SYS-102", "DOB组件异常：mapperClassName不能为空");
		}
		
		boolean isStandardOperateion = DataOperateType.isStandardDataOperateMapperType(dbOperation);
		
		Class[] clazz;
		if(args == null || args.length == 0){
			clazz = null;
		}else{
			clazz = new Class[args.length];
			this.args = new Object[args.length];
			for(int i = 0; i < args.length; i++){
				this.args[i] = args[i];
				//如果是标准操作方法传入的参数为Object类型
				if(isStandardOperateion){
					clazz[i] = Object.class;
				}else{
					clazz[i] = args[i].getClass();
				}
			}
		}
		try {
//			Method[] methods=(Class.forName(mapperClassName)).getMethods();  
//	        for(Method m :methods){  
//	            System.out.println(m.getDeclaringClass());  
//	            System.out.println(m.getName());
//	            Class[] params = m.getParameterTypes();
//	            if(params.length > 0){
//	            	for(int i = 0; i < params.length; i++){
//	            		System.out.println(params[i].getName());
//	            	}
//	            }
//	        }
	        
			Method method = (Class.forName(mapperClassName)).getMethod(dbOperation, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JpException("SYS-102", "DOB组件异常：mapperClass未找到对应方法" + dbOperation);
		}
		this.dbOperation = dbOperation;
		this.mapperClassName = mapperClassName;
	}		
	
	//全参数构造器
	public DataOperateMapperBean(String dbOperation, String mapperClassName,
			Serializable primary, String dataKey, boolean isCheckResult,
			Integer expectValue, Object...args) {
		this(dbOperation, mapperClassName, args);
		setProperties(dataKey, isCheckResult, expectValue);
	}

	public String getDbOperation() {
		return dbOperation;
	}

	public void setDbOperation(String dbOperation) {
		this.dbOperation = dbOperation;
	}

	public String getMapperClassName() {
		return mapperClassName;
	}

	public void setMapperClassName(String mapperClassName) {
		this.mapperClassName = mapperClassName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object commit() throws Exception {
		//执行数据库交互后的结果
		Object result = null;
		try{
			//获取对象数据操作类型，如果操作类型不存在抛出异常
			String dbOperation = getDbOperation();
			if(dbOperation == null || "".equals(dbOperation.trim())){
				throw new JpException("SYS-102", "DOB组件异常：dbOperation不支持");
			}
			
			String mapperClassName = getMapperClassName();
			if(mapperClassName == null || "".equals(mapperClassName.trim())){
				throw new JpException("SYS-102", "DOB组件异常：mapperClassName不能为空");
			}
			
			boolean isStandardOperateion = DataOperateType.isStandardDataOperateMapperType(dbOperation);
			
			Object[] args = getArgs();
			
			Class[] clazz;
			if(args == null || args.length == 0){
				clazz = null;
			}else{
				clazz = new Class[args.length];
				for(int i = 0; i < args.length; i++){					
					//如果是标准操作方法传入的参数为Object类型
					if(isStandardOperateion){
						clazz[i] = Object.class;
					}else{
						clazz[i] = args[i].getClass();
					}
				}
			}
			try {
				Method method = ((Class.forName(mapperClassName))).getMethod(dbOperation, clazz);
				result =method.invoke(SpringUtils.getBean(Class.forName(mapperClassName)), args);
			} catch (Exception e) {
				e.printStackTrace();
				throw new JpException("SYS-102", "DOB组件异常：mapperClass未找到对应方法" + dbOperation + "或者执行方法失败");
			}				
						
		}catch(Exception e){
			throw e;
		}
		return result;
	}

	@Override
	public Object getData() {
		if(args==null){
			return null;
		}
		if(args.length==1){
			return args[0];
		}
		return args;
	}	 
	
}
