package com.yhlearningclient.biz;

import java.util.List;

import com.yhlearningclient.dao.IBook;
import com.yhlearningclient.dao.impl.BookService;
import com.yhlearningclient.model.Advise;
import com.yhlearningclient.model.Book;
import com.yhlearningclient.model.Note;
import com.yhlearningclient.model.OnlineForum;
import com.yhlearningclient.utils.PaginateResult;


/**
 * 电子书集合管理
 * @author Administrator
 *
 */
public class BookManager {
	
	IBook bookService ;
	
	/**
	 *初始化对象 
	 */
	public BookManager() {
		bookService = new BookService();
	}
	
	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> geBookByPage(String serverIP,int classId, int page, int rows) {
		return bookService.geBookByPage(serverIP,classId, page, rows);
	}
	
	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> geBookByPage(String serverIP, int classId, int page, int rows, Integer categoryId) {
		return bookService.geBookByPage(serverIP,classId, page, rows, categoryId);
	}
	
	/**
	 * 获得记事本
	 * @return
	 */
	public List<Note> queryNoteAll(String serverIP,Long userId) {
		return bookService.queryNoteAll(serverIP,userId);
	}
	
	/**
	 * 创建记事本
	 * @param note
	 * @throws Exception 
	 */
	public void createNote(String serverIP,Long userId, String note) throws Exception{
		bookService.createNote(serverIP,userId, note);
	}
	
	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getHomeworkByall(String serverIP,int page, int rows) {
		return bookService.getHomeworkByall(serverIP,page, rows);
	}
	
	/**
	 * 获得在线交流集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public PaginateResult getOnlineForumByPage(String serverIP,int page, int rows, Long classId) {
		return bookService.getOnlineForumByPage(serverIP,page, rows, classId);
	}
	
	/**
	 * 添加
	 * @param advise
	 * @throws Exception
	 */
	public boolean AddAdvise(String serverIp, Advise advise) throws Exception {
		return bookService.AddAdvise(serverIp, advise);
	}
	
	/**
	 * 获得在线交流集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<OnlineForum> getChildForumByPage(String serverIP,int id, int page, int rows) {
		return bookService.getChildForumByPage(serverIP, id, page, rows);
	}
	
	/**
	 * 获得资源内容
	 * @param serverIP
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Book queryBookById(String serverIP, int id) throws Exception {
		return bookService.queryBookById(serverIP, id);
	}
	
	/**
	 * 补充
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getAdditionalall(String serverIP, int classId, int page, int rows){
		return bookService.getAdditionalall(serverIP, classId, page, rows);
	}
	
	/**
	 * 本地
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getLocalByall(String serverIP, int classId, int page, int rows){
		return bookService.getLocalByall(serverIP, classId, page, rows);
	}
}
