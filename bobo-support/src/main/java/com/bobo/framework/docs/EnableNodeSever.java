package com.bobo.framework.docs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class EnableNodeSever implements ImportSelector {

	private Logger logger = LoggerFactory.getLogger(EnableNodeSever.class);

	@Override
	public String[] selectImports(AnnotationMetadata arg0) {
		List<Object> clazzs = arg0.getAllAnnotationAttributes(EnableNode.class.getName()).get("root");
		clazzs.forEach(obj -> {
			Class<?> clzz = (Class<?>) obj;
			logger.info("注释扫描服务加载成功....");
			ScanNotes.scan(clzz);
		});
		return new String[] {};
	}

}
