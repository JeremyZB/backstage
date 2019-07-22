package com.bobo.framework.mybatis.dbo;

/**
 * @ClassName: DataOperateType
 * @Description: 数据库操作类型常量定义
 * @author: hanson huang
 */
public class DataOperateType {
	//插入操作常量
	public static final String INSERT = "INSERT_OPERATION";
	//更新操作常量
	public static final String UPDATE = "UPDATE_OPERATION";
	//删除操作常量
	public static final String DELETE = "DETELE_OPERATION";
	//自定义插入SQL操作常量
	public static final String CUSTOM_INSERT = "CUSTOM_INSERT_OPERATION";
	//自定义更新SQL操作常量
	public static final String CUSTOM_UPDATE = "CUSTOM_UPDATE_OPERATION";
	//自定义删除SQL操作常量
	public static final String CUSTOM_DELETE = "CUSTOM_DELETE_OPERATION";
	//自定义SQL操作常量
	public static final String CUSTOM_SQL = "CUSTOM_SQL_OPERATION";
	
	//组件预留关键字
	public static final String KEYWORDS = "|CHECKRESULT|";
	
	public static boolean checkDataOperateModelType(String dataOperateType){
		if(dataOperateType == null || 
				!DataOperateType.DataOperateModelType.INSERT.equals(dataOperateType) && 
				!DataOperateType.DataOperateModelType.UPDATE.equals(dataOperateType) &&
				!DataOperateType.DataOperateModelType.DELETE.equals(dataOperateType)){
			return false;
		}
		return true;
	}
	
	public static boolean checkDataOperateSqlType(String dataOperateType){
		if(dataOperateType == null || 
				!DataOperateType.DataOperateSqlType.CUSTOM_INSERT.equals(dataOperateType) && 
				!DataOperateType.DataOperateSqlType.CUSTOM_UPDATE.equals(dataOperateType) && 
				!DataOperateType.DataOperateSqlType.CUSTOM_DELETE.equals(dataOperateType) && 
				!DataOperateType.DataOperateSqlType.CUSTOM_SQL.equals(dataOperateType)){
			return false;
		}
		return true;
	}		
	
	public static class DataOperateModelType{
		//插入操作常量
		public static final String INSERT = DataOperateType.INSERT;
		//更新操作常量
		public static final String UPDATE = DataOperateType.UPDATE;
		//删除操作常量
		public static final String DELETE = DataOperateType.DELETE;
	}
	
	public static class DataOperateSqlType{
		//自定义插入SQL操作常量
		public static final String CUSTOM_INSERT = DataOperateType.CUSTOM_INSERT;
		//自定义更新SQL操作常量
		public static final String CUSTOM_UPDATE = DataOperateType.CUSTOM_UPDATE;
		//自定义删除SQL操作常量
		public static final String CUSTOM_DELETE = DataOperateType.CUSTOM_DELETE;
		//自定义SQL操作常量
		public static final String CUSTOM_SQL = DataOperateType.CUSTOM_SQL;
	}
	
	public static class DataOperateMapperType{
		//删除操作
		public static final String DELETE = "delete";
		public static final String DELETE_BY_EXAMPLE = "deleteByExample";
		public static final String DELETE_BY_PRIMARY_KEY = "deleteByPrimaryKey";
		//插入操作
		public static final String INSERT = "insert";
		public static final String INSERT_SELECTIVE = "insertSelective";
		public static final String INSERT_USE_GENERATED_KEYS = "insertUseGeneratedKeys";
		public static final String INSERT_LIST = "insertList";
		//更新操作
		public static final String UPDATE_BY_EXAMPLE_SELECTIVE = "updateByExampleSelective";
		public static final String UPDATE_BY_EXAMPLE = "updateByExample";
		public static final String UPDATE_BY_PRIMARY_KEY = "updateByPrimaryKey";
		public static final String UPDATE_BY_PRIMARY_KEY_SELECTIVE = "updateByPrimaryKeySelective";
	}
	
	public static boolean isStandardDataOperateMapperType(String dataOperateType){
		if(dataOperateType == null || "".equals(dataOperateType.trim())){
			return false;
		}
		
		if(!DataOperateMapperType.DELETE.equals(dataOperateType) && 
				!DataOperateMapperType.DELETE_BY_EXAMPLE.equals(dataOperateType) && 
				!DataOperateMapperType.DELETE_BY_PRIMARY_KEY.equals(dataOperateType) && 
				!DataOperateMapperType.INSERT.equals(dataOperateType) && 
				!DataOperateMapperType.INSERT_SELECTIVE.equals(dataOperateType) && 
				!DataOperateMapperType.INSERT_USE_GENERATED_KEYS.equals(dataOperateType) && 
				!DataOperateMapperType.INSERT_LIST.equals(dataOperateType) && 
				!DataOperateMapperType.UPDATE_BY_EXAMPLE_SELECTIVE.equals(dataOperateType) && 
				!DataOperateMapperType.UPDATE_BY_EXAMPLE.equals(dataOperateType) && 
				!DataOperateMapperType.UPDATE_BY_PRIMARY_KEY.equals(dataOperateType) && 
				!DataOperateMapperType.UPDATE_BY_PRIMARY_KEY_SELECTIVE.equals(dataOperateType)){
			return false;
		}
		
		return true;
	}
	
}
