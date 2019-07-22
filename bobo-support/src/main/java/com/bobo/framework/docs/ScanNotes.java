package com.bobo.framework.docs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * @ClassName: ScanNotes
 * @Description: 扫描注释
 * @author: leven
 */
public class ScanNotes {
	private static Logger logger = LoggerFactory.getLogger(ScanNotes.class);
	// 扫描
	public static String comp = "com.bobo";

	protected static Map<String, String> fieldNotesMap = new HashMap<String, String>();
	protected static Map<String, String> methodNotesMap = new HashMap<String, String>();
	protected static Map<String, String> classNotesMap = new HashMap<String, String>();

	protected static Class<?> thisClazz = ScanNotes.class;
	protected static List<String> javaList = Lists.newArrayList();

	public static void main(String[] args) {
		// ScanNotes.scan();
	}

	protected static void scanJarJavaList(JarFile jar) {
		Enumeration<JarEntry> entry = jar.entries();
		while (entry.hasMoreElements()) {
			JarEntry jarEntry = entry.nextElement();

			if (!jarEntry.isDirectory()) {
				String name = jarEntry.getName();
				if (name.endsWith(".java") && !name.endsWith(thisClazz.getSimpleName() + ".java")) {
					javaList.add("/" + name);
				}
			}
		}
	}

	private static void scanFileJavaList(File file, String rps) {
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				scanFileJavaList(f, rps);
			} else {
				String name = f.getName();
				if (name.endsWith(".java") && !name.endsWith(thisClazz.getSimpleName() + ".java")) {
					javaList.add(f.getPath().replace(rps, "").replace("\\", "/"));
				}
			}
		}
	}

	public static void scan(Class<?> rootClzz) {
		System.out.println(rootClzz.getName());

		logger.info("开始扫描注释....扫描优先级  classse->jar->war");
		boolean scanRes = false;
		if (!scanRes) {
			URL url = ClassUtils.getDefaultClassLoader().getResource("");
			logger.info("开始扫描 classse....");
			if (url != null) {
				File file = new File(url.getPath());
				scanFileJavaList(file, file.getPath());
				logger.info("扫描classse成功");
				scanRes = true;
			}
		}
		if (!scanRes) {
			logger.info("开始扫描 jar....");
			try {
				// 获取外层jar
				JarFile jar = new JarFile(rootClzz.getProtectionDomain().getCodeSource().getLocation().getPath());
				scanJarJavaList(jar);
				logger.info("扫描jar成功");
				scanRes = true;
			} catch (IOException e) {

			}
		}
		if (!scanRes) {
			logger.info("开始扫描 war....");
			// 获取war
			URL url = rootClzz.getResource("");
			if (url != null) {
				File file = new File(url.getPath());
				scanFileJavaList(file, file.getPath());
				logger.info("扫描classse成功");
				scanRes = true;
			}
			logger.info("扫描war成功");
			scanRes = true;

		}

		if (CollectionUtils.isEmpty(javaList)) {
			logger.error("加载javaList失败");
			return;
		}
		try {
			// 扫描java的src目录
			javaList.forEach(item -> {
				try {
					InputStream in = ScanNotes.class.getResourceAsStream(item);

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
		// 打印注释
		classNotesMap.forEach((k, v) -> {
			System.out.println(k + "=" + v);
		});
		methodNotesMap.forEach((k, v) -> {
			System.out.println(k + "=" + v);
		});
		fieldNotesMap.forEach((k, v) -> {
			System.out.println(k + "=" + v);
		});

		logger.info("class注释加载 {} 个", classNotesMap.size());
		logger.info("method注释加载 {} 个", methodNotesMap.size());
		logger.info("field注释加载 {} 个", fieldNotesMap.size());

	}

	// 提取类注释
	protected static void drawClssNotes(JavaClass javaClass) {
		if (javaClass.isInterface()) {
			String ckey = javaClass.getFullyQualifiedName();
			// 提取类注释
			if (!ckey.endsWith("Service") || ckey.endsWith("TaskService"))
				return;
			String ct = "";
			DocletTag[] dts = javaClass.getTags();
			for (DocletTag docletTag : dts) {
				String tagName = docletTag.getName();
				if (StringUtils.hasText(tagName)) {
					if (tagName.toLowerCase().equals("description:"))
						ct = docletTag.getValue();
				}
			}
			if (StringUtils.hasText(ct)) {
				ct = ct.replaceAll("\r|\n", "");
				classNotesMap.put(ckey, ct);
			}
		}
	}

	// 提取方法注释
	protected static void drawMethodNotes(JavaClass javaClass) {
		if (javaClass.isInterface()) {
			String ckey = javaClass.getFullyQualifiedName();
			if (!ckey.endsWith("Service") || ckey.endsWith("TaskService"))
				return;
			JavaMethod[] javaMethods = javaClass.getMethods();
			for (JavaMethod javaMethod : javaMethods) {
				String ct = "";
				String mkey = ckey + "." + javaMethod.getName();

				DocletTag[] dts = javaMethod.getTags();
				for (DocletTag docletTag : dts) {
					String tagName = docletTag.getName();
					if (tagName.toLowerCase().equals("description:") || tagName.toLowerCase().equals("description"))
						ct = docletTag.getValue();
				}
				if (StringUtils.isEmpty(ct)) {
					ct = replaceFieldCt(javaMethod.getComment());
				}
				if (StringUtils.hasText(ct)) {
					ct = ct.replaceAll("\r|\n", "");
					methodNotesMap.put(mkey, ct);
				}
			}
		}
	}

	protected static void drawFieldNotes(JavaClass javaClass) {
		try {
			
			if (javaClass.isInterface()) {
				return;
			}
			String ckey = javaClass.getFullyQualifiedName();
			if (ckey.endsWith("UnionMeta") || ckey.endsWith("TaskService") || ckey.endsWith("Service")
					|| ckey.endsWith("Mapper") || ckey.endsWith("ServiceImpl") || ckey.indexOf("_") > -1
					|| ckey.endsWith("MgrImpl") || ckey.endsWith("Mgr"))
				return;
			JavaField[] javaFields = javaClass.getFields();
			// 源代码
			com.thoughtworks.qdox.model.JavaSource classSource = javaClass.getSource();
			List<String> line = Arrays.asList(classSource.toString().split("\n"));

			for (JavaField javaField : javaFields) {

				String javaFieldName = javaField.getName();
				
				if (javaFieldName.equals("serialVersionUID"))
					continue;
				String fkey = ckey + "." + javaFieldName;
				String ct = replaceFieldCt(javaField.getComment());

				if (StringUtils.isEmpty(ct)) {
					try {
						int lineNum = javaField.getLineNumber();
						for (int i = lineNum; i > 0; i--) {
							String s = line.get(i).trim();
							if (s.startsWith("//")) {
								ct = s.replace("//", "");
								break;
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				if (StringUtils.hasText(ct)) {
					ct = ct.replaceAll("\r|\n", "");
					fieldNotesMap.put(fkey, ct);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String replaceFieldCt(String ct) {
		String cct = "";
		if (StringUtils.isEmpty(ct))
			return cct;
		cct = ct.replaceAll("\\s*", "").replace("*", "");
		return cct.trim();
	}

	public static String getMethodNotes(String className) {
		String notes = ScanNotes.classNotesMap.get(className);
		if (StringUtils.isEmpty(notes))
			return "";
		return notes;
	}

	public static String getMethodNotes(String className, String methodName) {

		String notes = ScanNotes.methodNotesMap.get(className + "." + methodName);
		if (StringUtils.isEmpty(notes))
			return "";
		return notes;
	}

	public static String getFieldNotes(String className, String fieldName) {
		String notes = ScanNotes.fieldNotesMap.get(className + "." + fieldName);
		if (StringUtils.isEmpty(notes))
			return "//";
		return "//" + notes.replace(",", "、");
	}

}
