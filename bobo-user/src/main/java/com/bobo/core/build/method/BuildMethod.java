package com.bobo.core.build.method;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
/**
 * 业务框架构建类
 * @author bobo
 *
 */
public class BuildMethod {


	static String _package = "com.bobo.core.service.login"; // 修改生成的模块
	static String _package2 = _package+".msg";
	

	static String _author = "bobo";
	static String _date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	static String encoding = "UTF-8";
	static String serviceName = "Login";
	static String serviceDesc = "登陆服务";

	public static void main(String[] args) throws IOException {
		String root = System.getProperty("user.dir");
		String path = root.concat("/src/main/java/").concat(_package.replace(".", "/"));
		String path2 = root.concat("/src/main/java/").concat(_package2.replace(".", "/"));
		// 读取模版
		String reqMsgTemplate = FileUtils.readFileToString(
				new File(root.concat("/src/main/java/com/bobo/core/build/method/template/ReqMsgTemplate.txt")),
				"UTF-8");
		String respMsgTemplate = FileUtils.readFileToString(
				new File(root.concat("/src/main/java/com/bobo/core/build/method/template/RespMsgTemplate.txt")),
				"UTF-8");
		String serviceInterfaceTemplate = FileUtils.readFileToString(
				new File(root.concat("/src/main/java/com/bobo/core/build/method/template/ServiceInterfaceTemplate.txt")),
				"UTF-8");
		String serviceMethodTemplate = FileUtils.readFileToString(
				new File(root.concat("/src/main/java/com/bobo/core/build/method/template/ServiceMethodTemplate.txt")),
				"UTF-8");
		// 读取文件
		List<String> lines = FileUtils.readLines(
				new File(root.concat("/src/main/java/com/bobo/core/build/method/template/desc.txt")), encoding);
		
		// serviceInterface
				String serviceInterface = serviceInterfaceTemplate.replaceAll("#package#", _package)
						.replaceAll("#ServiceName#", serviceName)
						.replaceAll("#Description#", serviceDesc)
						.replaceAll("#author#", _author)
						.replaceAll("#date#", _date);
				
				String serviceMethod="";
		
		for (String line : lines) {

			String[] temp = line.split(":");

			//String _className = temp[1].trim().toUpperCase();
			String _msgClassName = temp[1].trim().substring(0, 1).toUpperCase()+temp[1].trim().substring(1);
			String _description = temp[0].trim();


			// 生成请求bean
			String reqMsgOut = reqMsgTemplate;
			reqMsgOut = reqMsgOut.replaceAll("#package#", _package2);
			reqMsgOut = reqMsgOut.replaceAll("#ClassName#", _msgClassName);
			reqMsgOut = reqMsgOut.replaceAll("#author#", _author);
			reqMsgOut = reqMsgOut.replaceAll("#date#", _date);
			reqMsgOut = reqMsgOut.replaceAll("#Description#", _description);

			FileUtils.writeStringToFile(new File(path2 + "/" + _msgClassName + "Req.java"), reqMsgOut, encoding);

			// 生成返回bean
			String respMsgOut = respMsgTemplate;
			respMsgOut = respMsgOut.replaceAll("#package#", _package2);
			respMsgOut = respMsgOut.replaceAll("#ClassName#", _msgClassName);
			respMsgOut = respMsgOut.replaceAll("#author#", _author);
			respMsgOut = respMsgOut.replaceAll("#date#", _date);
			respMsgOut = respMsgOut.replaceAll("#Description#", _description);

			FileUtils.writeStringToFile(new File(path2 + "/" + _msgClassName + "Resp.java"), respMsgOut, encoding);

			// 生成service
			serviceMethod = serviceMethod
					+ serviceMethodTemplate
							.replaceAll("#Description#", _description)
							.replaceAll("#ClassName#", _msgClassName)
							.replaceAll("#Method#", _msgClassName.toLowerCase());
		}
		
		serviceInterface=serviceInterface.replace("#serviceMethod#", serviceMethod);
		FileUtils.writeStringToFile(new File(path + "/" + serviceName + "Service.java"), serviceInterface, encoding);
		System.out.println(serviceInterface);
	}

}
