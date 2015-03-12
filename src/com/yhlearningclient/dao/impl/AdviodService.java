package com.yhlearningclient.dao.impl;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yhlearningclient.common.HttpUtil;
import com.yhlearningclient.constant.ServerIP;
import com.yhlearningclient.dao.IAdviod;
import com.yhlearningclient.model.Adviod;
import com.yhlearningclient.model.CoursePlan;
import com.yhlearningclient.model.CoursewareNode;
import com.yhlearningclient.model.Video;
import com.yhlearningclient.utils.FastJsonTools;

/**
 * 视频实现类
 * @author Administrator
 */
public class AdviodService implements IAdviod{

	/** 
	 * 获得视频集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Adviod> geAdviodByPage(String serverIP,int page, int rows) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_ADVIOD, page, rows);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String obj = "";
		JSONObject item;
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<Adviod> adviods = FastJsonTools.getObects(obj, Adviod.class);
		
		return adviods;
	}
	
	
	/**
	 * 获得视频集合
	 * @param 
	 * @return
	 */
	public List<Video> getVideosByAll(String serverIP, int classId) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_VIDEO, classId);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json111==", jsonString);
		
		List<Video> adviods = FastJsonTools.getObects(jsonString, Video.class);
		
		return adviods;
	}
	
	/**
	 * 获得视频编号
	 * @param 
	 * @return
	 */
	public Video getVideosById(String serverIP,int id) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_VIDEOID,id);
		Log.i("learning path==", path);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		
		Video adviods = FastJsonTools.getObect(jsonString, Video.class);
		
		return adviods;
	}
	
	/**
	 * 获得课程教学计划
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursePlan> getPlanByAll(String serverIP) {
		
		String path = serverIP + ServerIP.SERVLET_PLAN;
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		List<CoursePlan> adviods = FastJsonTools.getObects(jsonString, CoursePlan.class);
		
		return adviods;
	}
	
	/**
	 * 获得课程教学计划
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursePlan> getCourseTableByAll(String serverIP) {
		
		String path = serverIP + ServerIP.SERVLET_COURSETABLE;
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		List<CoursePlan> adviods = FastJsonTools.getObects(jsonString, CoursePlan.class);
		
		return adviods;
	}
	
	/**
	 * 获得课程教学计划
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursePlan> getClassroomBookByAll(String serverIP) {
		
		String path = serverIP + ServerIP.SERVLET_CLASSROOMBOOK;
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		List<CoursePlan> adviods = FastJsonTools.getObects(jsonString, CoursePlan.class);
		
		return adviods;
	}
	
	
	/**
	 * 获得课程分类
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursewareNode> getCategoryByAll(String serverIP) {
		
		String path = serverIP + ServerIP.SERVLET_CATEGORY;
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		List<CoursewareNode> category = FastJsonTools.getObects(jsonString, CoursewareNode.class);
		
		return category;
	}
	
}
