package com.bobo.framework.mybatis.dbo;

import java.io.Serializable;
import java.util.ArrayList;
import com.bobo.framework.utils.JpException;

/**
 * @ClassName: DataOperateList
 * @Description: 数据库操作javabean容器，作为跨应用调用的返回类型
 * @author: hanson huang
 */
public class DataOperateList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//散列码，用于自动生成所装载的DataOperateBean的dataKey，目前初始化时取当前nanotime
	private String hashCode;
	
	//待操作数据列表
	private ArrayList<DataOperateAbstractBean> todoList;
	
	public DataOperateList() {
		super();
		hashCode = String.valueOf(System.nanoTime());
		todoList = new ArrayList<DataOperateAbstractBean>();
	}
	
	public void clear() {
		todoList.clear();		
	}
	
	public void add(DataOperateAbstractBean e) {
		//如果加入的DataOperateBean未设置dataKey则将hashCode-序号作为DataOperateBean的dataKey
		String dataKey = "";
		if(e.getDataKey() == null){
			dataKey = hashCode + "-" + todoList.size();			
		}else{
			dataKey = e.getDataKey();
			if(DataOperateType.KEYWORDS.contains("|" + dataKey.toUpperCase() + "|")){
				throw new JpException("SYS-102", dataKey + "为预留关键字，请使用其他字符串");
			}
		}
		//判断是否有重复的dataKey
		if(todoList != null && todoList.size() > 0){
			for(DataOperateAbstractBean dob : todoList){
				if(dataKey.equals(dob.getDataKey())){
					throw new JpException("SYS-102", "DataOperateList中DataKey重复");
				}
			}
		}
		//如果结果需要检查，则必须设置expectValue
		if(e.isCheckResult()){
			if(e.getExpectValue() == null){
				throw new JpException("SYS-102", dataKey + "被设置为需要对结果进行检查，请设置预期结果（expectValue）");
			}
		}
		e.setDataKey(dataKey);
		todoList.add(e);
	}

	public ArrayList<DataOperateAbstractBean> getTodoList() {
		return todoList;
	}
	
	public void setTodoList(ArrayList<DataOperateAbstractBean> todoList){
		this.todoList = todoList;
	}

	public int size(){
		return this.todoList.size();
	}
	
	//根据dataKey获取DataOperateBean
	public DataOperateAbstractBean getDataOperateBeanByDataKey(String dataKey){
		DataOperateAbstractBean ret = null;
		if(todoList == null || todoList.size() == 0){
			ret = null;
		}else{
			for(DataOperateAbstractBean dob : todoList){
				if(dataKey.equals(dob.getDataKey())){
					ret = dob;
					break;
				}
			}
		}
		
		return ret;
	}
	
	//合并其他DataOperateList对象
	public void addAll(DataOperateList dataOperateList){
		ArrayList<DataOperateAbstractBean> dol = dataOperateList.getTodoList();
		if(dol != null && dol.size() > 0){
			for(DataOperateAbstractBean dob : dol){
				add(dob);
			}
		}
	}

}
