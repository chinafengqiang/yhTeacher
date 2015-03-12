package com.yhlearningclient.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yhlearningclient.common.HttpUtil;
import com.yhlearningclient.constant.ServerIP;
import com.yhlearningclient.dao.ILesson;
import com.yhlearningclient.model.LessonExtend;
import com.yhlearningclient.utils.FastJsonTools;


/**
 * 教学课程表安排业务类
 * @author Administrator
 */
public class LessonService implements ILesson{

	/**
	 * 获得课程表集合
	 * @param classId
	 * @return
	 */
	public List<LessonExtend> getLessonsByClassId(String serverIP,Long classId) {
		
		String path = String.format(serverIP+ServerIP.SERVLET_LESSON, classId);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		List<LessonExtend> lessons = FastJsonTools.getObects(jsonString, LessonExtend.class);
		
		return lessons;
	}
	
	
	/**
	 * 创建通知
	 * @param 
	 * @throws Exception 
	 */
	public boolean createMessage(String serverIP, Long userId, String name, String content, Integer canSendAll, Long classId) throws Exception{
		String path = serverIP + ServerIP.SERVLET_ADD_MESSAGE;
		String params = "";
		String success = "";
		try {
			params = "creator="+userId+"&name="+URLEncoder.encode(name, "UTF-8")
					+"&content="+URLEncoder.encode(content, "UTF-8")
					+"&canSendAll="+canSendAll+"&classId="+classId;
			Log.i("learning params==", params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] data = HttpUtil.getDataFromUrl(path, params);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				return true; 
			} else {
				throw new Exception("添加错误！"); 
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	
	/**
	 * 创建通知
	 * @param 
	 * @throws Exception 
	 */
	public boolean createManagerForum(String serverIP, Long classId, String name, String question, Long creator) throws Exception{
		String path = serverIP + ServerIP.SERVLET_ADD_Forum;
		String params = "";
		String success = "";
		try {
			params = "creator="+creator+"&name="+URLEncoder.encode(name, "UTF-8")
					+"&question="+URLEncoder.encode(question, "UTF-8")
					+"&classId="+classId;
			Log.i("learning params==", params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] data = HttpUtil.getDataFromUrl(path, params);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				return true; 
			} else {
				throw new Exception("添加错误！"); 
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
}
