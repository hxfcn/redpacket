package com.sdi.redpacket.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sdi.common.BaseController;
import com.sdi.common.RET;

@RestController 
public class LoginController extends BaseController {
	
	@Autowired
	LoginService _service;
	
	
	@RequestMapping(value="/login",method= {RequestMethod.POST},produces="application/json;charset=UTF-8")  
	public String login(HttpServletRequest request) {
		JSONObject params = this.getJSONParam(request);
		if(params == null) {
			return RET.BAD_REQUEST;
		}
		
		String userid = params.getString("userid");
		String pwd = params.getString("pwd");
		if(userid == null || pwd == null ) {
			return RET.PARAMS_ERROR;
		}
		return _service.login(userid, pwd);
	}
	
	@RequestMapping(value="/register",method= {RequestMethod.POST},produces="application/json;charset=UTF-8")  
	public String register(HttpServletRequest request) {
		JSONObject params = this.getJSONParam(request);
		if(params == null) {
			return RET.BAD_REQUEST;
		}
		
		String userid = params.getString("userid");
		String nickname = params.getString("nickname");
		String pwd = params.getString("pwd");

		
		if(userid == null || pwd == null ) {
			return RET.PARAMS_ERROR;
		}
		
//		JSONObject o = new JSONObject();
//		o.put("userid", userid);
//		o.put("nickname", nickname);
//		o.put("pwd", pwd);
		
		return _service.register(params);
	}
	
	@RequestMapping(value="/testreg",method= {RequestMethod.GET})  
	public String testreg(HttpServletRequest request) {
	
		String userid = ("userid");
		String nickname = ("nickname");
		String pwd = ("pwd");

		
		JSONObject o = new JSONObject();
		o.put("userid", userid);
		o.put("nickname", nickname);
		o.put("pwd", pwd);
		
		return _service.register(o);
	}
	@RequestMapping(value="/testlogin",method= {RequestMethod.GET})  
	public String testlogin(HttpServletRequest request) {
	
		String userid = ("userid");
		String pwd = ("pwd");

		
		return _service.login(userid, pwd);
	}
}
