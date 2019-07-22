package com.bobo.framework.dubbo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;






public class SerializationOptimizerImpl implements SerializationOptimizer {

	@SuppressWarnings("rawtypes")
	public Collection<Class> getSerializableClasses() {
		List<Class> classes = new LinkedList<Class>();
		classes.add(DubboIn.class);
		classes.add(DubboOut.class);
		return classes;
	}

}
