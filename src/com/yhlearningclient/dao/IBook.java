package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.Advise;
import com.yhlearningclient.model.Book;
import com.yhlearningclient.model.Note;
import com.yhlearningclient.model.OnlineForum;
import com.yhlearningclient.utils.PaginateResult;

/**
 * 电子书接口
 * @author Administrator
 */
public interface IBook {
	
	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> geBookByPage(String serverIP,int classId, int page, int rows);

	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> geBookByPage(String serverIP, int classId, int page, int rows, Integer categoryId);
	
	/**
	 * 获得记事本
	 * @return
	 */
	public List<Note> queryNoteAll(String serverIP,Long userId);
	
	/**
	 * 创建记事本
	 * @param note
	 * @throws Exception 
	 */
	public void createNote(String serverIP,Long userId, String note) throws Exception;
	
	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getHomeworkByall(String serverIP,int page, int rows);
	
	/**
	 * 获得在线交流集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public PaginateResult getOnlineForumByPage(String serverIP,int page, int rows, Long classId);
	
	/**
	 * 添加
	 * @param advise
	 * @return 
	 * @throws Exception
	 */
	public boolean AddAdvise(String serverIp, Advise advise) throws Exception;
	
	/**
	 * 获得在线交流集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<OnlineForum> getChildForumByPage(String serverIP,int id, int page, int rows);
	
	/**
	 * 获得资源内容
	 * @param serverIP
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Book queryBookById(String serverIP, int id) throws Exception;
	
	/**
	 * 补充
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getAdditionalall(String serverIP, int classId, int page, int rows);
	
	/**
	 * 本地
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getLocalByall(String serverIP, int classId, int page, int rows);
	
}
