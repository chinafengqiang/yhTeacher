package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.Adviod;
import com.yhlearningclient.model.CoursePlan;
import com.yhlearningclient.model.CoursewareNode;
import com.yhlearningclient.model.Video;

/**
 * 视频接口
 * @author Administrator
 */
public interface IAdviod {

	/**
	 * 获得视频集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Adviod> geAdviodByPage(String serverIP,int page, int rows);
	
	/**
	 * 获得视频集合
	 * @param 
	 * @return
	 */
	public List<Video> getVideosByAll(String serverIP, int classId);
	
	/**
	 * 获得视频编号
	 * @param 
	 * @return
	 */
	public Video getVideosById(String serverIP,int id);
	
	public List<CoursePlan> getPlanByAll(String serverIP);
	
	/**
	 * 获得课程教学计划
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursePlan> getCourseTableByAll(String serverIP);
	
	/**
	 * 获得课程教学计划
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursePlan> getClassroomBookByAll(String serverIP);
	
	/**
	 * 获得课程分类
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public List<CoursewareNode> getCategoryByAll(String serverIP);
	
}
