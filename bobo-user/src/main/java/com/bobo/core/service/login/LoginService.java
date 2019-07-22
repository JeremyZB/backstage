package com.bobo.core.service.login;

import com.bobo.core.service.login.msg.Login001Req;
import com.bobo.core.service.login.msg.Login001Resp;
import com.bobo.core.service.login.msg.Login002Req;
import com.bobo.core.service.login.msg.Login002Resp;
import com.bobo.framework.msg.RequestMsg;
import com.bobo.framework.msg.ResponseMsg;


/**
 * @ClassName: LoginService
 * @Description: 登陆服务   返回参数
 * @author: bobo
 * @date: 2019-07-12 10:19:19
 */
 
 public interface LoginService{

   
	 /**
	 * @Description: 用户注册
	 */
	 //@AccessTokenWhite
	public abstract ResponseMsg<Login001Resp> login001(RequestMsg<Login001Req> req);

	 /**
	 * @Description: 用户登陆
	 */
	 //@AccessTokenWhite
	public abstract ResponseMsg<Login002Resp> login002(RequestMsg<Login002Req> req);

}