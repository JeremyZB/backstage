package com.bobo.core.mapper;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
 
/**
 * 定制版MyBatis Mapper插件接口
 * @Auther: lzw
 * @Date: 2019/3/29 09:35
 * @Description:
 */
public interface MyMapper<T>
        extends
        BaseMapper<T>,
        ConditionMapper<T>,
        IdsMapper<T>,
        InsertListMapper<T> {


}
