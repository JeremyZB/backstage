package com.bobo.framework.docs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;

/**
 * @ClassName: ScanNotes
 * @Description: 扫描注释
 * @author: leven
 */
public class ScanNotesJars extends ScanNotes {

	private static Logger logger = LoggerFactory.getLogger(ScanNotesJars.class);

	public static void scan(Class<?>[] rootClzzs) {
		System.out.println(ArrayUtils.toString(rootClzzs));

		logger.info("开始扫描注释....扫描优先级  jar");

		try {
			for (Class<?> clazz : rootClzzs) {
				JarFile jar = new JarFile(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
				logger.info("开始扫描 jar...." + jar.getName());
				scanJarJavaList(jar);
			}

		} catch (IOException e) {

		}

		if (CollectionUtils.isEmpty(javaList)) {
			logger.error("加载javaList失败");
			return;
		}
		try {
			// 扫描java的src目录
			javaList.forEach(item -> {
				try {
					InputStream in = ScanNotesJars.class.getResourceAsStream(item);

					JavaDocBuilder builder = new JavaDocBuilder();
					builder.addSource(new InputStreamReader(in, "UTF-8"));
					JavaClass[] javaClasses = builder.getClasses();

					Arrays.asList(javaClasses).stream().forEach(javaClass -> {
						drawClssNotes(javaClass);
						drawMethodNotes(javaClass);
						drawFieldNotes(javaClass);
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
//		// 打印注释
//		classNotesMap.forEach((k, v) -> {
//			System.out.println(k + "=" + v);
//		});
//		methodNotesMap.forEach((k, v) -> {
//			System.out.println(k + "=" + v);
//		});
//		fieldNotesMap.forEach((k, v) -> {
//			System.out.println(k + "=" + v);
//		});
		logger.info("class注释加载 {} 个", classNotesMap.size());
		logger.info("method注释加载 {} 个", methodNotesMap.size());
		logger.info("field注释加载 {} 个", fieldNotesMap.size());

	}

}