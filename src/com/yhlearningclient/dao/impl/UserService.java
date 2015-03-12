package com.yhlearningclient.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.yhlearningclient.common.HttpUtil;
import com.yhlearningclient.constant.ServerIP;
import com.yhlearningclient.dao.IUser;
import com.yhlearningclient.db.DB;
import com.yhlearningclient.db.DB.TABLES.SYSMESSAGE;
import com.yhlearningclient.db.DB.TABLES.USERINFO;
import com.yhlearningclient.db.DBHelper;
import com.yhlearningclient.model.Manager;
import com.yhlearningclient.model.SysMessage;
import com.yhlearningclient.model.User;
import com.yhlearningclient.model.UserInfo;
import com.yhlearningclient.utils.FastJsonTools;


/**
 * 用户登录接口
 * @author Administrator
 */
public class UserService implements IUser{
	
	private DBHelper helper = null;

	public UserService(Context context) {
		helper = new DBHelper(context);
	}

	/**
	 * 用户登录方法
	 * @param name
	 * @param password
	 * @return 返回用户对象
	 * @throws Exception 
	 */
	public UserInfo login(String serverIP, String name, String password) throws Exception {
		
		String path = String.format(ServerIP.server_head + serverIP + ":8080" + ServerIP.SERVLET_LOGIN,name, password);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String success = "";
		UserInfo userInfo = null;
		
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				String juser = item.getString("obj");
				userInfo = FastJsonTools.getObect(juser, UserInfo.class);
				
				Log.i("learning userinfo name====", userInfo.getName());
				
			} else {
				throw new Exception("用户和密码错误！"); 
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return userInfo;
	}
	
	/**
	 * 用户登录方法
	 * @param name
	 * @param password
	 * @return 返回用户对象
	 * @throws Exception 
	 */
	public Manager loginManager(String serverIP, String name, String password) throws Exception {
		
		String path = String.format(ServerIP.server_head + serverIP + ":8080" + ServerIP.SERVLET_LOGIN,name, password);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String success = "";
		Manager manager = null;
		
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			String msg = item.getString("msg");
			if("密码错误".equals(msg)){
				Manager user = new Manager();
				user.setName(msg);
				return user;
						
			}
			if("true".equals(success)){
				String juser = item.getString("obj");
				manager = FastJsonTools.getObect(juser, Manager.class);
				
				Log.i("learning manager name====", manager.getName());
				
			} else {
				throw new Exception("用户和密码错误！"); 
			}
			
		} catch (JSONException e) {
			throw new Exception("网络连接超时！"); 
		}
		
		return manager;
	}
	
	/**
	 * 用户登录方法
	 * @param name
	 * @param password
	 * @return 返回用户对象
	 * @throws Exception 
	 */
	public boolean loginBool(String serverIP, String name, String password) throws Exception {
		
		String path = String.format(ServerIP.server_head + serverIP + ":8080" + ServerIP.SERVLET_LOGIN, name, password);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String success = "";
		UserInfo userInfo = null;
		boolean flag = false ;
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				flag = true;
				String juser = item.getString("obj");
				userInfo = FastJsonTools.getObect(juser, UserInfo.class);
				
				Log.i("learning userinfo name====", userInfo.getName());
				return flag;
				
			} else {
				return flag;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
		
	}
	
	
	/**
	 * 获得通知信息
	 * @param serverIP服务地址
	 * @return
	 */
	public List<SysMessage> getAllSysMessageList(String serverIP, Long classId) {
		int last_id = this.getLastSysMessageId();
		getNewSysMessageList(serverIP, last_id, classId);
		return getAllSysMessageFromDB();
	}
	
	
	
	
	/**
	 * 获得课程表集合
	 * @param classId
	 * @return
	 */
	public List<SysMessage> getNewSysMessageList(String serverIP, int last_message_id, Long classId) {
		
		Log.i("last_message_id json==", "last_message_id json="+last_message_id);
		
		String path = String.format(serverIP+ServerIP.SERVLET_MESSAGE, classId);
	//	String path = serverIP + ServerIP.SERVLET_MESSAGE;
		Log.i("SysMessage path==", path);
		
		byte[] data = HttpUtil.getDataFromUrl(path);
		
		String jsonString = new String(data);
		Log.i("SysMessage json==", jsonString);
		
//		List<SysMessage> list = FastJsonTools.getObects(jsonString, SysMessage.class);
//		return list;
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<SysMessage> sysMessages = new ArrayList<SysMessage>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject item = null;
			try {
				item = jsonArray.getJSONObject(i);

				SysMessage sysMessage = new SysMessage();

				sysMessage.set_id(item.getInt("id"));
				sysMessage.setName(item.getString("name"));
				sysMessage.setContent(item.getString("content"));
				sysMessage.setTime(item.getString("createdTime"));
				sysMessage.setIsRead(0);

				
				if (sysMessage.get_id() > last_message_id) {
					this.Insert(sysMessage);
					
				}

				sysMessages.add(sysMessage);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		
		return sysMessages;
	}
	
	
	/**
	 * 获得课程表集合
	 * @param classId
	 * @return
	 */
	public List<SysMessage> getRefreshSysMessageList(String serverIP, Long classId) {
		
		String path = String.format(serverIP+ServerIP.SERVLET_MESSAGE, classId);
//		String path = serverIP + ServerIP.SERVLET_MESSAGE;
		Log.i("SysMessage path==", path);
		
		byte[] data = HttpUtil.getDataFromUrl(path);
		
		String jsonString = new String(data);
		Log.i("SysMessage json==", jsonString);
		
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<SysMessage> sysMessages = new ArrayList<SysMessage>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject item = null;
			try {
				item = jsonArray.getJSONObject(i);

				SysMessage sysMessage = new SysMessage();

				sysMessage.set_id(item.getInt("id"));
				sysMessage.setName(item.getString("name"));
				sysMessage.setContent(item.getString("content"));
				sysMessage.setTime(item.getString("createdTime"));
				sysMessage.setIsRead(0);

				sysMessages.add(sysMessage);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		
		if(sysMessages != null){
			this.remove();
		}
		
		for(SysMessage s : sysMessages){
			this.Insert(s);
		}
		
		return getAllSysMessageFromDB();
	}
	
	
	
	/**
	 * 插入通知对象
	 * @param sysMessage
	 */
	private void Insert(SysMessage sysMessage) {

		ContentValues values = new ContentValues();
		values.put(SYSMESSAGE.FIELDS._ID, sysMessage.get_id());
		values.put(SYSMESSAGE.FIELDS.NAME, sysMessage.getName());
		values.put(SYSMESSAGE.FIELDS._CONTENT, sysMessage.getContent());
		values.put(SYSMESSAGE.FIELDS.TIME, sysMessage.getTime());
		values.put(SYSMESSAGE.FIELDS.ISREAD, sysMessage.getIsRead());
		this.helper.insert(SYSMESSAGE.TABLENAME, values);

	}
	
	
	/**
	 * 插入通知对象
	 * @param sysMessage
	 */
	private void remove() {

		this.helper.ExecuteSQL(SYSMESSAGE.SQL.DELETE);

	}
	
	
	/**
	 * 获得最后值
	 * @return
	 */
	public int getLastSysMessageId() {
		String sql = "select _id from sysmessage order by id desc";
		int last_id = 0;
		try {
			Cursor cursor = helper.SELECT(sql);
			if (cursor.moveToFirst()) {
				last_id = cursor.getInt(cursor
						.getColumnIndex(SYSMESSAGE.FIELDS._ID));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("sq", "sysmessage last_id=" + last_id);
		return last_id;
	}
	
	
	/**
	 * 本地获得通知消息
	 * @return
	 */
	public List<SysMessage> getAllSysMessageFromDB() {
		String condition = " 1=1 limit 20";

		String sql = String.format(DB.TABLES.SYSMESSAGE.SQL.SELECT, condition);
		List<SysMessage> sysMessages = new ArrayList<SysMessage>();
		
		try {
			Cursor cursor = helper.SELECT(sql);
			while (cursor.moveToNext()) {
				SysMessage sysMessage = new SysMessage();
				
				sysMessage.set_id(cursor.getInt(cursor.getColumnIndex(SYSMESSAGE.FIELDS._ID)));
				sysMessage.setName(cursor.getString(cursor.getColumnIndex(SYSMESSAGE.FIELDS.NAME)));
				sysMessage.setContent(cursor.getString(cursor.getColumnIndex(SYSMESSAGE.FIELDS._CONTENT)));
				sysMessage.setIsRead(cursor.getInt(cursor.getColumnIndex(SYSMESSAGE.FIELDS.ISREAD)));
				sysMessage.setTime(cursor.getString(cursor.getColumnIndex(SYSMESSAGE.FIELDS.TIME)));
				
				sysMessages.add(sysMessage);
				
			}
			cursor.close();
			return sysMessages;
		} catch (Exception e) {
			return null;
		} finally {
			helper.closeDataBase();
		}
	}
	
	
	/**
	 * 更新
	 * @param sysMessage
	 */
	public void updataSysMessage(SysMessage sysMessage) {
		String sql = String.format(DB.TABLES.SYSMESSAGE.SQL.UPDATE,sysMessage.getIsRead(), sysMessage.get_id());
		helper.ExecuteSQL(sql);

	}
	
	/**
	 * 用户插入
	 * @param user
	 */
	public void insert(User user) {

		ContentValues values = new ContentValues();
		values.put(USERINFO.FIELDS.USER_ID, user.getUser_id());
		values.put(USERINFO.FIELDS.USER_NAME, user.getUser_name());
		values.put(USERINFO.FIELDS.USER_PASSWORD, user.getUser_password());
		values.put(USERINFO.FIELDS.CLASS_ID, user.getClassId());
		
		this.helper.insert(USERINFO.TABLENAME, values);
	} 
	
	/**
	 * 本用户
	 * @return
	 */
	public List<User> getAllUserFromLocal() {
		//String condition = " 1=1 order by id limit 20";

		// String sql = String.format(DB.TABLES.USERINFO.SQL.SELECT_ALL, condition);
		 String sql =  DB.TABLES.USERINFO.SQL.SELECT_ALL;
		 List<User> users  = new ArrayList<User>();
	        try {
	        	Cursor cursor = helper.SELECT(sql);
	        		
					while(cursor.moveToNext()){
						User user = new User();
						user.setUser_id(cursor.getString(cursor.getColumnIndex(USERINFO.FIELDS.USER_ID)));
						user.setUser_name(cursor.getString(cursor.getColumnIndex(USERINFO.FIELDS.USER_NAME)));
						user.setUser_password(cursor.getString(cursor.getColumnIndex(USERINFO.FIELDS.USER_PASSWORD)));
						user.setClassId(cursor.getLong(cursor.getColumnIndex(USERINFO.FIELDS.CLASS_ID)));
						users.add(user);
					}
				
				cursor.close();
				return users;
			} catch (SQLException ex) {
				return null;
			}finally
			{
				helper.closeDataBase();
			}
	}
	
	/**
	 * 获得记录数
	 * @param condtion
	 * @return
	 */
	public int getUserCount(String condtion){
		String sql_count = MessageFormat.format(DB.TABLES.USERINFO.SQL.SELECT_COUNT,condtion);
		return (int) helper.rawQuerySingle(sql_count);  //总条数
	}
	
}
