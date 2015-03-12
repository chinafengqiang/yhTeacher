package com.yhlearningclient.biz;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yhlearningclient.dao.IAdviod;
import com.yhlearningclient.dao.impl.AdviodService;
import com.yhlearningclient.model.Adviod;
import com.yhlearningclient.model.CoursePlan;
import com.yhlearningclient.model.CoursewareNode;
import com.yhlearningclient.model.Video;
import com.yhlearningclient.utils.FastJsonTools;


/**
 * 视频集合管理
 * @author Administrator
 *
 */
public class AdviodManager {
	
	IAdviod adviodService ;
	
	/**
	 *初始化对象 
	 */
	public AdviodManager() {
		adviodService = new AdviodService();
	}
	
	/**
	 * 获得视频集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Adviod> geAdviodByPage(String serverIP,int page, int rows) {
		return adviodService.geAdviodByPage(serverIP,page, rows);
	}
	
	/**
	 *  传递url返回结果集
	 * @param jsonString
	 * @return
	 */
	public List<Adviod> transJsonToList(String jsonString) {
		try {
			String obj = "";
			JSONObject item;
			try {
				item = new JSONObject(jsonString);
				obj = item.getString("rows");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			List<Adviod> videos = FastJsonTools.getObects(obj, Adviod.class);
			return videos;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * 获得视频集合
	 * @param 
	 * @return
	 */
	public List<Video> getVideosByAll(String serverIP, int classId){
		return adviodService.getVideosByAll(serverIP, classId);
	}
	
	/**
	 * 获得视频编号
	 * @param 
	 * @return
	 */
	public Video getVideosById(String serverIP,int id){
		return adviodService.getVideosById(serverIP,id);
	}
	
	/**
	 *  传递url返回结果集
	 * @param jsonString
	 * @return
	 */
	public List<Video> transJsonToLists(String jsonString) {
		try {
//			String obj = "";
//			JSONObject item;
//			try {
//				item = new JSONObject(jsonString);
//				obj = item.getString("rows");
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			
			List<Video> videos = FastJsonTools.getObects(jsonString, Video.class);
			return videos;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * 获得课程教学计划
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursePlan> getPlanByAll(String serverIP) {
		return adviodService.getPlanByAll(serverIP);
	}
	
	public List<CoursePlan> getCourseTableByAll(String serverIP){
		return adviodService.getCourseTableByAll(serverIP);
	}
	
	public List<CoursePlan> getClassroomBookByAll(String serverIP){
		return adviodService.getClassroomBookByAll(serverIP);
	}
	
	/**
	 * 获得课程分类
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursewareNode> getCategoryByAll(String serverIP) {
		return adviodService.getCategoryByAll(serverIP);
	}
	
}
