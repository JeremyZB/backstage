package com.bobo.framework.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bobo.framework.dubbo.IDubboService;
import com.bobo.framework.utils.JsonUtil;


@Controller
@RequestMapping("")
@Scope("prototype")
public class ApiInfo extends BaseApi {

	private static final Logger logger = LoggerFactory.getLogger(ApiInfo.class);
	public static Map<String, String> dfv;

	/**
	 * 公共接口入口，反射调用
	 * 
	 * @Title: common
	 * @Description: TODO
	 * @param param
	 * @return
	 * @return: Json
	 */
	@RequestMapping(value = "{coreName}")
	public ModelAndView main(HttpServletRequest request, @PathVariable("coreName") String coreName) {
		ModelAndView mv = new ModelAndView("/index");
		mv.addObject("coreName", coreName);
		// 拉取版本号
		return mv;
	}

	@RequestMapping(value = "header")
	public ModelAndView header(HttpServletRequest request, String coreName) {
		ModelAndView mv = new ModelAndView("txinfo/header");
		mv.addObject("coreName", coreName);
		
		IDubboService iDubboService = this.getServiceByCoreName(coreName);
		List<String> versions = iDubboService.getVersion(coreName);
		mv.addObject("versions",JsonUtil.writeValueAsString(versions));
		
		return mv;
	}
	@RequestMapping(value = "left")
	public ModelAndView left(HttpServletRequest request, String coreName,String version) {
		System.out.println("left coreName:" + coreName);
		System.out.println("left version:" + version);
		ModelAndView mv = new ModelAndView("txinfo/menu3");
		mv.addObject("coreName", coreName);
		mv.addObject("version", version);
		return mv;
	}

	@RequestMapping("txinfo/doc")
	public ModelAndView doc(String cls, String method, String coreName) {
		System.out.println("cls:" + cls);

		ModelAndView mv = new ModelAndView("txinfo/doc");
		// 获取请求文档
		IDubboService iDubboService = this.getServiceByCoreName(coreName);
		Map<String, Object> result = iDubboService.getDoc(cls, method);
		if (result != null) {
			mv.addObject("req", JsonUtil.writeValueAsString(result.get("req")));
			mv.addObject("resp", JsonUtil.writeValueAsString(result.get("resp")));
		}
		// 获取返回文档
		return mv;
	}

	@RequestMapping(value = "txinfo/txlist")
	@ResponseBody
	public void txlist(HttpServletRequest request, HttpServletResponse response, String coreName,String version) throws IOException {
		IDubboService iDubboService = this.getServiceByCoreName(coreName);
		String s = iDubboService.getTree(coreName,version);
//		System.out.println("txlist==="+s);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(s);
	}

	@RequestMapping(value = "txinfo/txtest")
	public String txtest(HttpServletRequest request, String coreName) {
		String cName = request.getParameter("cls");
		String mName = request.getParameter("method");
//		System.out.println(cName + "  ," + mName);

		IDubboService iDubboService = this.getServiceByCoreName(coreName);
		Map<String, Object> map = iDubboService.getTreeInput(cName, mName);
		String s = JsonUtil.writeValueAsString(map);
//		System.out.println(s);
		request.setAttribute("coreName", coreName);
		request.setAttribute("inputs", s);
		request.setAttribute("cls", cName);
		request.setAttribute("method", mName);

		return "txinfo/txtest";
	}
}
