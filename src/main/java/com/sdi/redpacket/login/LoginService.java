package com.sdi.redpacket.login;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdi.common.BaseService;
import com.sdi.common.HttpUtil;
import com.sdi.common.RET;

@Service
public class LoginService extends BaseService {

	@Autowired
	LoginDao _dao;
	
	public String login(String uid,String pwd) {
		int res = _dao.login(uid, pwd);
		String token = _dao.loginToken(uid, pwd);
		if(token != null) {
			JSONObject o = new JSONObject();
			o.put("token", token);
			return RET.data(o);
		}
		else {
			return RET.error(1, "Login Error!");
		}
	}
	
	public String getUTCTimeStr() {
		try {
			Calendar cal = Calendar.getInstance();
			return String.valueOf(cal.getTimeInMillis() / 1000);// 返回的就是UTC时间
		}
		catch(Exception e) {
			return "123456789";
		}

	}
	
	 public static String getSha1(String str){
	        if(str==null||str.length()==0){
	            return null;
	        }
	        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
	                'a','b','c','d','e','f'};
	        try {
	            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
	            mdTemp.update(str.getBytes("UTF-8"));

	            byte[] md = mdTemp.digest();
	            int j = md.length;
	            char buf[] = new char[j*2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                buf[k++] = hexDigits[byte0 & 0xf];      
	            }
	            return new String(buf);
	        } catch (Exception e) {
	            // TODO: handle exception
	            return null;
	        }
	    }
	
	 public String getToken(JSONObject userInfoJSON) {
			String userid = userInfoJSON.getString("userid");
			String nickname = userInfoJSON.getString("nickname");
			
			String appkey = "YOUMECEAE17A3B5CA22BB218C0BCF4D95770F43A4DFD7";
			String appsecret = "u/DZub3UPWMfCod7SdKY+VEDda4rHp2rNwvBNr8iJ88M8Jj7prS7Htl7PVQVOoPKudB+nHQs6U4e551GxR5Oz/jJR1ODglmkpo3DDlWC8njGOKTvuOympSbeb14T1UA+PN+y4xIuN66yBjptK01HyYA2MxSxKxWi8IG97h+hpk0BAAE=";
			String apikey = "b2463f0886e5a055143d3c78fea39e64";
			String identifier = "179186438@qq.com";
			String curtime = getUTCTimeStr();
			String checksum = getSha1(appsecret+curtime);	
			String url = "https://api.youme.im/v1/im/add_user_auth?appkey=%s&identifier=%s&curtime=%s&checksum=%s";
			String qq = String.format(url, appkey,identifier,curtime,checksum);
			
			
			JSONArray list = new JSONArray();
			JSONObject o1 = new JSONObject();
			o1.put("UserID", userid);
			o1.put("NickName", nickname);
			list.add(o1);
			JSONObject oo = new JSONObject();
			oo.put("UserList", list);
			
			String response = null;
			
			try {
				response = HttpUtil.postJsonHttps(qq, oo.toJSONString());
			} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(response == null) return "";
			
			JSONObject o = JSONObject.parseObject(response);
			if(o.getInteger("ErrorCode") != 0) {
				return "";
			}
			JSONArray arr = o.getJSONArray("UserList");
			if(arr == null || arr.size() <= 0) {
				return "";
			}
			JSONObject user = arr.getJSONObject(0);
			if(user == null) {
				return "";
			}
			
			return user.getString("Token");
	 }
	 
	public String register(JSONObject userInfoJSON) {
		
		String userid = userInfoJSON.getString("userid");
		String nickname = userInfoJSON.getString("nickname");
		if(_dao.hasinfo(userid) >= 1){
			return RET.error(1, "Account already exists!");
		}
		
		String token = this.getToken(userInfoJSON);

		userInfoJSON.put("token", token);
		
		
		int res = _dao.register(userInfoJSON);
		if(res == 1) {
			JSONObject o = new JSONObject();
			o.put("token", token);
			return RET.data(o);
		}
		else {
			return RET.error(2, "Unknown Error!");
		}
	}
}
