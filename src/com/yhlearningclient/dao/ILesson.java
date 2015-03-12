package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.LessonExtend;

/**
 * 课程安排信息接口
 * 该接口主要用于课程表模块获取数据
 * 从此接口可以：
 * 获得课程安排集合
 * @author Administrator
 */
public interface ILesson {

	/**
	 * 获得课程表集合
	 * @param classId
	 * @return
	 */
	public List<LessonExtend> getLessonsByClassId(String serverIP,Long classId);
	
	
	/**
	 * 创建通知
	 * @param 
	 * @throws Exception 
	 */
	public boolean createMessage(String serverIP, Long userId, String name, String content, Integer canSendAll, Long classId) throws Exception;
	
	/**
	 * 创建通知
	 * @param 
	 * @throws Exception 
	 */
	public boolean createManagerForum(String serverIP, Long classId, String name, String question, Long creator) throws Exception;
	
}
