package com.bobo.core.service.login.msg;

import java.io.Serializable;

import lombok.Data;
/**
 * @ClassName: Login001Req
 * @Description: 用户注册   请求参数
 * @author: bobo
 * @date: 2019-07-12 10:19:19
 */
@Data
public class Login001Req  implements Serializable
{

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 电话
     */
    private String phone;
}
