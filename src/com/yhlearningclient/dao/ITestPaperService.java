package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.TestPaper;
import com.yhlearningclient.model.TestPaperCategory;
import com.yhlearningclient.model.TestPaperExtend;
import com.yhlearningclient.model.TestPaperQuestion;



/**
 * 用户登录接口
 * @author Administrator
 *
 */
public interface ITestPaperService {

	/**
	 * 获得试卷集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<TestPaperExtend> getTestPaperByPage(String serverIP,int page, int rows);
	
	/**
	 * 获得试卷列表
	 * @return
	 */
	public List<TestPaper> getAllTestPaperList(String serverIP, int categoryId, int page, int rows, int classId);
	
	/**
	 * 获得分类列表
	 * @return
	 */
	public List<TestPaperCategory> getAllTestPaperCategoryList(String serverIP);
	
	/**
	 * 获得试卷列表
	 * @return
	 */
	public List<TestPaperQuestion> getAllTestPaperQuestionList(String serverIP,  int testPaperId);
	
	/**
	 * 得到用于分页的数据源的总条数，一般要配合getByPager()方法共同使用 
	 * @param pageSize
	 * @param condtion 1=1 代表无条件
	 * @return
	 */
	public int getByPageCount(int pageSize,String condtion);
	
	public List<TestPaperQuestion> getByPager(int pageIndex,int pageSize,String condition);
	
	/**
	 * 获得所有试卷
	 * @param condtion
	 * @return
	 */
	public List<TestPaperQuestion> getByPagerAll(String condtion);
	
	/**
	 * 获得试卷信息
	 * @return
	 */
	public List<TestPaper> getTestPaperById(int id);
	
	public List<TestPaperQuestion> getQuestionById(String condition);
	
	/**
	 * 获得分类
	 * @return
	 */
	public List<TestPaperCategory> getAllTestPaperCategoryDB();
	
	/**
	 * 本地试卷
	 * @return
	 */
	public List<TestPaper> getAllTestPaperDB(int categoryId);
	
	public boolean isTestPaperQuestionList(String serverIP,  int testPaperId);
	
	/**
	 * 本地试卷题目数据
	 * @return
	 */
	public List<TestPaperQuestion> getAllTestPaperQuestionDB(int testPaperId);
}
