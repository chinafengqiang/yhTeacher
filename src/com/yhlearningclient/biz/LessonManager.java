package com.yhlearningclient.biz;

import java.util.List;

import com.yhlearningclient.dao.ILesson;
import com.yhlearningclient.dao.impl.LessonService;
import com.yhlearningclient.model.LessonExtend;


/**
 * 课程表集合管理
 * @author Administrator
 *
 */
public class LessonManager {
	
	ILesson lessonService ;
	
	/**
	 *初始化对象 
	 */
	public LessonManager() {
		lessonService = new LessonService();
	}
	
	/**
	 * 获得课程表集合
	 * @param classId
	 * @return
	 */
	public List<LessonExtend> getLessonsByClass(String serverIP,Long classId){
		return lessonService.getLessonsByClassId(serverIP,classId);
	}
	
	/**
	 * 创建通知
	 * @param 
	 * @throws Exception 
	 */
	public boolean createMessage(String serverIP, Long userId, String name, String content, Integer canSendAll, Long classId) throws Exception{
		return lessonService.createMessage(serverIP, userId, name, content, canSendAll, classId);
	}
	
	/**
	 * 创建通知
	 * @param 
	 * @throws Exception 
	 */
	public boolean createManagerForum(String serverIP, Long classId, String name, String question, Long creator) throws Exception{
		return lessonService.createManagerForum(serverIP, classId, name, question, creator);
	}
	
}
