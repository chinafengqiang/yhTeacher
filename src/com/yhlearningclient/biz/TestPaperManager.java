package com.yhlearningclient.biz;

import java.util.List;

import android.content.Context;

import com.yhlearningclient.dao.ITestPaperService;
import com.yhlearningclient.dao.impl.TestPaperService;
import com.yhlearningclient.model.TestPaper;
import com.yhlearningclient.model.TestPaperCategory;
import com.yhlearningclient.model.TestPaperExtend;
import com.yhlearningclient.model.TestPaperQuestion;


/**
 * 用户登录集合管理
 * @author Administrator
 *
 */
public class TestPaperManager {
	
	ITestPaperService testPaperService ;
	
	/**
	 *初始化对象 
	 */
	public TestPaperManager(Context context) {
		testPaperService = new TestPaperService(context);
	}
	
	/**
	 * 获得试卷集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<TestPaperExtend> getTestPaperByPage(String serverIP,int page, int rows) {
		return testPaperService.getTestPaperByPage(serverIP, page, rows);
	}
	
	/**
	 * 获得试卷列表
	 * @return
	 */
	public List<TestPaper> getAllTestPaperList(String serverIP, int categoryId, int page, int rows, int classId){
		return testPaperService.getAllTestPaperList(serverIP, categoryId, page, rows, classId);
	}
	
	/**
	 * 获得分类列表
	 * @return
	 */
	public List<TestPaperCategory> getAllTestPaperCategoryList(String serverIP){
		return testPaperService.getAllTestPaperCategoryList(serverIP);
	}
	
	/**
	 * 获得试卷列表
	 * @return
	 */
	public List<TestPaperQuestion> getAllTestPaperQuestionList(String serverIP,  int testPaperId){
		return testPaperService.getAllTestPaperQuestionList(serverIP, testPaperId);
	}
	
	/**
	 * 得到用于分页的数据源的总条数，一般要配合getByPager()方法共同使用 
	 * @param pageSize
	 * @param condtion 1=1 代表无条件
	 * @return
	 */
	public int getByPageCount(int pageSize,String condtion){
		return testPaperService.getByPageCount(pageSize, condtion);
	}
	
	public List<TestPaperQuestion> getByPager(int pageIndex,int pageSize,String condition){
		return testPaperService.getByPager(pageIndex, pageSize, condition);
	}
	
	/**
	 * 获得所有试卷
	 * @param condtion
	 * @return
	 */
	public List<TestPaperQuestion> getByPagerAll(String condtion) {
		return testPaperService.getByPagerAll(condtion);
	}
	
	/**
	 * 获得试卷信息
	 * @return
	 */
	public List<TestPaper> getTestPaperById(int id) {
		return testPaperService.getTestPaperById(id);
	}
	
	public List<TestPaperQuestion> getQuestionById(String condition) {
		return testPaperService.getQuestionById(condition);
	}
	
	/**
	 * 获得分类
	 * @return
	 */
	public List<TestPaperCategory> getAllTestPaperCategoryDB(){
		return testPaperService.getAllTestPaperCategoryDB();
	}
	
	/**
	 * 本地试卷
	 * @return
	 */
	public List<TestPaper> getAllTestPaperDB(int categoryId) {
		return testPaperService.getAllTestPaperDB(categoryId);
	}
	
	public boolean isTestPaperQuestionList(String serverIP,  int testPaperId){
		return testPaperService.isTestPaperQuestionList(serverIP, testPaperId);
	}
	
	/**
	 * 本地试卷题目数据
	 * @return
	 */
	public List<TestPaperQuestion> getAllTestPaperQuestionDB(int testPaperId) {
		return testPaperService.getAllTestPaperQuestionDB(testPaperId);
	}
	
}
