package com.yhlearningclient.constant;


/**
 * 配置运程服务址
 * @author Administrator
 */
public class ServerIP {
	
	/**
	 * 主服务地址
	 */
	public static String SERVER_ID = "";
	
	public static String server_head = "http://";
	
//	public static final String SERVER_ID = "http://192.168.30.100:8080";

	/**
	 * 登录
	 */
	public static final String SERVLET_LOGIN = SERVER_ID
			+ "/managerController/loginManager.html?name=%s&password=%s";
	
	
	/**
	 * 教学安排
	 */
	public static final String SERVLET_LESSON = SERVER_ID
			+ "/courseController/getLesson.html?classId=%s";
	
	/**
	 * 电子书
	 */
	public static final String SERVLET_BOOK = SERVER_ID
			+ "/coursewareController/dataGridBook.html?classId=%s&page=%s&rows=%s";
	
	/**
	 * 电子书
	 */
	public static final String SERVLET_BOOK_CATEGORY = SERVER_ID
			+ "/coursewareController/dataGridBook.html?classId=%s&page=%s&rows=%s&categoryId=%s";
	
	/**
	 * 视频
	 */
	public static final String SERVLET_ADVIOD = SERVER_ID
			+ "/coursewareController/dataGridCourseware.html?page=%s&rows=%s";
	
	/**
	 * 视频文件
	 */
	public static final String SERVLET_VIDEO = SERVER_ID
			+ "/courseController/getCourseByAll.html?classId=%s";
	
	
	/**
	 * 视频编号
	 */
	public static final String SERVLET_VIDEOID = SERVER_ID
			+ "/courseController/getCourseById.html?courseId=%s";
	
	
	/**
	 * 记事本
	 */
	public static final String SERVLET_NOTE = SERVER_ID
			+ "/coursewareController/queryNote.html?userId=%s";
	
	/**
	 * 写记事本
	 */
	public static final String SERVLET_CREATE_NOTE = SERVER_ID
			+ "/coursewareController/createNote.html?";
	
	/**
	 * 教学计划
	 */
	public static final String SERVLET_PLAN = SERVER_ID
			+ "/courseController/queryCoursePlan.html?";
	
	/**
	 * 教学计划
	 */
	public static final String SERVLET_COURSETABLE = SERVER_ID
			+ "/courseController/queryCourseTable.html";
	
	/**
	 * 教学计划
	 */
	public static final String SERVLET_CLASSROOMBOOK = SERVER_ID
			+ "/courseController/queryClassBook.html";
	
	/**
	 * 作业
	 */
	public static final String SERVLET_HOMEWORK = SERVER_ID
			+ "/coursewareController/dataGridHomework.html?page=%s&rows=%s";
	
	/**
	 * 服务
	 */
	public static final String SERVLET_CREATE = SERVER_ID
			+ "/sysKeyController/createServerIP.html?keyValue=%s";
	/**
	 * 获得服务地址
	 */
	public static final String SERVLET_GETIP = SERVER_ID
			+ "/sysKeyController/getServerIP.html";
	
	/**
	 * 教学通知
	 */
	public static final String SERVLET_MESSAGE = SERVER_ID
			+ "/sysMessageController/querySysMessage.html?classId=%s";
	
	/**
	 * 分类
	 */
	public static final String SERVLET_CATEGORY = SERVER_ID
			+ "/coursewareController/queryCategory.html";

	/**
	 * 在线交流
	 */
	public static final String SERVLET_FORUM = SERVER_ID
			+ "/sysMessageController/dataGridForum.html?page=%s&rows=%s&classId=%s";
	
	
	/**
	 * 在线评
	 */
	public static final String SERVLET_ADDADVISE = SERVER_ID
			+ "/sysMessageController/createchildforum.html?";
	
	/**
	 * 回复
	 */
	public static final String SERVLET_CHILD = SERVER_ID
			+ "/sysMessageController/dataGridChildForum.html?id=%s&page=%s&rows=%s";
	
	/**
	 * 添加通知
	 */
	public static final String SERVLET_ADD_MESSAGE = SERVER_ID
			+ "/sysMessageController/createmessage.html?";
	
	/**
	 * 添加交流
	 */
	public static final String SERVLET_ADD_Forum = SERVER_ID
			+ "/sysMessageController/createManagerForum.html?";
	
	
	/**
	 * 获得资源
	 */
	public static final String SERVLET_GETBOOK = SERVER_ID
			+ "/coursewareController/queryBookById.html?id=%s";
	
	/**
	 * 补充
	 */
	public static final String SERVLET_ADDITIONAL = SERVER_ID
			+ "/coursewareController/dataGridAdditional.html?classId=%s&page=%s&rows=%s";
	
	/**
	 * 本地
	 */
	public static final String SERVLET_LOCAL = SERVER_ID
			+ "/coursewareController/dataGridLocal.html?classId=%s&page=%s&rows=%s";
	
	/**
	 * 试卷
	 */
	//http://localhost:8080/TestPaper/getTestPaperListBySearch.action?paginateParamters.pageNo=1&paginateParamters.perPageNumber=10
	public static final String SERVLET_TESTPAPER = SERVER_ID
			+ "/TestPaper/getTestPaperListBySearch.action?paginateParamters.pageNo=%s&paginateParamters.perPageNumber=%s&category=%s";
	
	/**
	 * 试卷分类
	 */
	public static final String SERVLET_TESTPAPER_CATEGORY = SERVER_ID
			+ "/System/getSysParamValue.action?sysParamName=TestPaperCategory";
	
	/**
	 * 试卷分类
	 */
	public static final String SERVLET_TESTPAPER_image = SERVER_ID
			+ "/Question/getImageStream.action";
	
	/**
	 * 试卷题目
	 */
	public static final String SERVLET_TESTPAPER_QUESTION = SERVER_ID
			+ "/TestPaper/getTestPaperData.action?testPaperId=%s";
	
	/**
	 * 保存记录
	 */
	public static final String SERVLET_USER_TESETPAPER = SERVER_ID
			+ "/userController/createUserTestPaper.html?";
	
	/**
	 * 获得用户作业记录
	 */
	public static final String SERVLET_GET_USER_TESETPAPER = SERVER_ID
			+ "/userController/queryUserTestPaper.html?id=%s";
	
	/**
	 * 获得用户
	 */
	public static final String SERVLET_QUERY_STUDENT = SERVER_ID
			+ "/userController/queryStudentByClassId.html?classId=%s";
	
	
	/**
	 * 图片
	 */
	public static final String VIDEO_INFO_URL = SERVER_ID;
}
