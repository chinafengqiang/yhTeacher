package com.yhlearningclient.biz;

import java.util.List;

import android.content.Context;

import com.yhlearningclient.dao.IUserTestPaperService;
import com.yhlearningclient.dao.impl.UserTestPaperService;
import com.yhlearningclient.model.UserTestPaper;


/**
 * 用户试卷记录集合管理
 * @author Administrator
 *
 */
public class UserTestPaperManager {
	
	IUserTestPaperService userTestPaperService ;
	
	/**
	 *初始化对象 
	 */
	public UserTestPaperManager(Context context) {
		userTestPaperService = new UserTestPaperService(context);
	}
	
	/**
	 * 保存试卷信息
	 * @param testPaper
	 */
	public void InsertUserTestPaper(UserTestPaper userTestPaper){
		userTestPaperService.InsertUserTestPaper(userTestPaper);
	}
	
	/**
	 * 获得分数
	 * @return
	 */
	public double getSumScore(int testPaperId) {
		return userTestPaperService.getSumScore(testPaperId);
	}
	
	/**
	 * 获得是否存在用户记录数据
	 * @return
	 */
	public boolean profileExist(int testPaperId){
		return userTestPaperService.profileExist(testPaperId);
	}
	
	/**
	 * 取得考卷对错矩阵
	 * @param id
	 * @return
	 */
	public  boolean[] getRwMatrix(int testPaperId) {
		return userTestPaperService.getRwMatrix(testPaperId);
	}
	
	public List<UserTestPaper> getUserTestPaperById(int testPaperId) {
		return userTestPaperService.getUserTestPaperById(testPaperId);
	}
	/**
	 * 创建用户试卷记录
	 * @param userTestPaper
	 * @throws Exception 
	 */
	public boolean saveUserTestPaper(String serverIP, UserTestPaper userTestPaper) throws Exception{
		return userTestPaperService.saveUserTestPaper(serverIP, userTestPaper);
	}
	
	public boolean isUserTestPaperList(String serverIP,  int testPaperId){
		return userTestPaperService.isUserTestPaperList(serverIP, testPaperId);
	}
	
	/**
	 * @param
	 * @return
	 */
	public int getByTotalCount(String questionId){
		return userTestPaperService.getByTotalCount(questionId);
	}
	
	public int getByRCount(String questionId){
		return userTestPaperService.getByRCount(questionId);
	}
	
	/**
	 * @param
	 * @return
	 */
	public int getUserCount(String userId){
		return userTestPaperService.getUserCount(userId);
	}
	
	/**
	 * 获得答案
	 * @param questionId
	 * @return
	 */
	public UserTestPaper getUserTestPaperByQuestionId(int questionId, int userId) {
		List<UserTestPaper> list = userTestPaperService.getUserTestPaperByQuestionId(questionId, userId);
		if(list !=null && list.size() >0){
			UserTestPaper u = list.get(0);
			return u;
		}
		return null; 
	}
	
}
