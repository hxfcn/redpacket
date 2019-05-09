package com.sdi.redpacket.login;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdi.common.BaseDao;

@Service
public class LoginDao extends BaseDao {
	
	int hasinfo(String uid) {
		String sql = "Select * from userinfor where userid = '" + uid + "'";
    	List<Map<String, Object>> rows = mJdbcTemplate.queryForList(sql);
    	return rows.size();
	}
	
	String loginToken(String uid,String pwd) {
		
		String sql = "Select * from userinfor where userid = '" + uid + "'";
    	List<Map<String, Object>> rows = mJdbcTemplate.queryForList(sql);
    	Iterator it = rows.iterator();
    	if(it.hasNext()) {
    		Map<String, Object> r = (Map<String, Object>)it.next();
    		String pwd2 = (String)r.get("pwd");
    		String token = (String)r.get("token");
    		if(pwd.equals(pwd2) ) {
    			return token;
    		}else {
    			return null;
    		}
    	}
    	return null;
	}
	
	int login(String uid,String pwd) {
		
		String sql = "Select * from userinfor where userid = '" + uid + "'";
    	List<Map<String, Object>> rows = mJdbcTemplate.queryForList(sql);
    	Iterator it = rows.iterator();
    	if(it.hasNext()) {
    		Map<String, Object> r = (Map<String, Object>)it.next();
    		String pwd2 = (String)r.get("pwd");
    		if(pwd.equals(pwd2) ) {
    			return 0;
    		}else {
    			return 1;
    		}
    	}
    	return 2;
	}
	
	int register(JSONObject userInfoJSON) {

		String userid = userInfoJSON.getString("userid");
		String nickname = userInfoJSON.getString("nickname");
		String pwd = userInfoJSON.getString("pwd");
		String token = userInfoJSON.getString("token");

		
		String sql = 
		"INSERT INTO userinfor (userid,pwd,token,nickname) VALUES ('%s','%s','%s','%s')";
		String qq = String.format(sql, userid,pwd,token,nickname);
		int res = mJdbcTemplate.update(qq);
		return res;
	}
}
