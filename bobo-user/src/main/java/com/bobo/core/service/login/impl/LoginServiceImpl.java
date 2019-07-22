package com.bobo.core.service.login.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.bobo.core.base.UserBaseService;
import com.bobo.core.model.User;
import com.bobo.core.service.login.LoginService;
import com.bobo.core.service.login.msg.Login001Req;
import com.bobo.core.service.login.msg.Login001Resp;
import com.bobo.core.service.login.msg.Login002Req;
import com.bobo.core.service.login.msg.Login002Resp;
import com.bobo.framework.msg.RequestMsg;
import com.bobo.framework.msg.ResponseMsg;

/**
 * @author bobo
 *
 */
@Component
@Service(interfaceClass = LoginService.class, executes = 1000, timeout = 1200000)
public class LoginServiceImpl extends UserBaseService implements LoginService {

	/**
	 * @Description: 用户注册
	 */
	@Override
	public ResponseMsg<Login001Resp> login001(RequestMsg<Login001Req> req) {
		ResponseMsg<Login001Resp> resp = new ResponseMsg<Login001Resp>();
		Login001Resp login001Resp = new Login001Resp();
		Login001Req login001Req = req.getParam();

		try {
			
			
			User record = new User();
			record.setUsername(login001Req.getUsername());
			int cnt=userMapper.selectCount(record);
			if(cnt>0){
				return (ResponseMsg<Login001Resp>) setFailRet(resp, "用户名已注册");
			}
			record.setPassword(login001Req.getPassword());
			record.setPhone(login001Req.getPhone());
			userMapper.insertSelective(record);

			resp.setResult(login001Resp);
			setResCode(resp, succes);
			return resp;

		} catch (Exception e) {
			e.printStackTrace();
			handleException(resp, e);
			return resp;
		}
	}

	@Override
	public ResponseMsg<Login002Resp> login002(RequestMsg<Login002Req> req) {

		return null;
	}

}
