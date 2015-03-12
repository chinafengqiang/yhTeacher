package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.UserTestPaper;




/**
 * 试卷数据接口
 * @author Administrator
 *
 */
public interface IUserTestPaperService {

	/**
	 * 保存试卷信息
	 * @param testPaper
	 */
	public void InsertUserTestPaper(UserTestPaper userTestPaper);
	
	/**
	 * 获得分数
	 * @return
	 */
	public double getSumScore(int testPaperId);
	
	/**
	 * 获得是否存在用户记录数据
	 * @return
	 */
	public boolean profileExist(int testPaperId);
	
	/**
	 * 取得考卷对错矩阵
	 * 
	 * @param id
	 * @return
	 */
	public  boolean[] getRwMatrix(int testPaperId);
	
	public List<UserTestPaper> getUserTestPaperById(int testPaperId);
	
	/**
	 * 创建用户试卷记录
	 * @param userTestPaper
	 * @throws Exception 
	 */
	public boolean saveUserTestPaper(String serverIP, UserTestPaper userTestPaper) throws Exception;
	
	public boolean isUserTestPaperList(String serverIP,  int testPaperId);
	
	/**
	 * @param
	 * @return
	 */
	public int getByRCount(String questionId);
	
	/**
	 * @param
	 * @return
	 */
	public int getByTotalCount(String questionId);
	
	/**
	 * @param
	 * @return
	 */
	public int getUserCount(String userId);
	
	/**
	 * 获得答案
	 * @param questionId
	 * @return
	 */
	public List<UserTestPaper> getUserTestPaperByQuestionId(int questionId, int userId);
}
