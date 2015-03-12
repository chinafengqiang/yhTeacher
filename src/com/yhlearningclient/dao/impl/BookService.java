package com.yhlearningclient.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yhlearningclient.common.HttpUtil;
import com.yhlearningclient.constant.ServerIP;
import com.yhlearningclient.dao.IBook;
import com.yhlearningclient.model.Advise;
import com.yhlearningclient.model.Book;
import com.yhlearningclient.model.Note;
import com.yhlearningclient.model.OnlineForum;
import com.yhlearningclient.utils.FastJsonTools;
import com.yhlearningclient.utils.PaginateResult;

/**
 * 电子书实现类
 * @author Administrator
 */
public class BookService implements IBook{
	
	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> geBookByPage(String serverIP, int classId, int page, int rows) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_BOOK, classId, page, rows);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String obj = "";
		JSONObject item;
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<Book> books = FastJsonTools.getObects(obj, Book.class);
		
		return books;
	}
	

	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> geBookByPage(String serverIP, int classId, int page, int rows, Integer categoryId) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_BOOK_CATEGORY, classId, page, rows, categoryId);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String obj = "";
		JSONObject item;
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<Book> books = FastJsonTools.getObects(obj, Book.class);
		
		return books;
	}
	
	
	/**
	 * 获得记事本
	 * @return
	 */
	public List<Note> queryNoteAll(String serverIP,Long userId) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_NOTE, userId);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		List<Note> notes = FastJsonTools.getObects(jsonString, Note.class);
		
		return notes;
	}
	
	/**
	 * 创建记事本
	 * @param note
	 * @throws Exception 
	 */
	public void createNote(String serverIP,Long userId, String note) throws Exception{
		String path = serverIP + ServerIP.SERVLET_CREATE_NOTE;
		String params = "";
		String success = "";
		try {
			params = "userId="+userId+"&note="+URLEncoder.encode(note, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] data = HttpUtil.getDataFromUrl(path,params);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				throw new Exception("添加成功！"); 
			} else {
				throw new Exception("用户和密码错误！"); 
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获得电子书集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getHomeworkByall(String serverIP, int page, int rows) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_HOMEWORK, page, rows);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String obj = "";
		JSONObject item;
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<Book> books = FastJsonTools.getObects(obj, Book.class);
		
		return books;
	}
	
	
	/**
	 * 获得在线交流集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public PaginateResult getOnlineForumByPage(String serverIP,int page, int rows, Long classId) {
		
		PaginateResult pr = new PaginateResult();
		
		String path = String.format(serverIP + ServerIP.SERVLET_FORUM, page, rows, classId);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("Forum json", jsonString);
		String obj = "";
		JSONObject item;
		String totalCount = "";
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
			totalCount = item.getString("total");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<OnlineForum> forums = FastJsonTools.getObects(obj, OnlineForum.class);
		pr.setList((ArrayList<OnlineForum>) forums);
		pr.setRecordCount(Integer.parseInt(totalCount));
		
		return pr;
	}
	
	/**
	 * 添加
	 * @param advise
	 * @throws Exception
	 */
	public boolean  AddAdvise(String serverIp, Advise advise) throws Exception {
		
		String path = serverIp + ServerIP.SERVLET_ADDADVISE;
		String params = "";
		String success = "";
		try {
			String question = URLEncoder.encode(advise.getQuestion(), "UTF-8");
			params = "rootId="+advise.getRootId()+"&question="+question
					+"&id="+advise.getId()+"&classId="+advise.getClassId();
			Log.i("learning params==", params);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] data = HttpUtil.getDataFromUrl(path, params);
		
	//	String url =String.format(serverIp + ServerIP.SERVLET_ADDADVISE, advise.getRootId(), question, advise.getId()) ;
//		byte[] data = HttpUtil.getDataFromUrl(url);
		if (data!=null){
			String jsonString = new String(data);
			Log.i("AddAdvise json", jsonString);
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				return true; 
			} else {
				throw new Exception("添加错误！"); 
			}
			
		}
		return false;
	}
	
	/**
	 * 获得在线交流集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<OnlineForum> getChildForumByPage(String serverIP,int id, int page, int rows) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_CHILD, id, page, rows);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("Forum json", jsonString);
		String obj = "";
		JSONObject item;
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<OnlineForum> forums = FastJsonTools.getObects(obj, OnlineForum.class);
		
		return forums;
	}
	
	/**
	 * 获得资源内容
	 * @param serverIP
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Book queryBookById(String serverIP, int id) throws Exception {
		
		String path = String.format(serverIP + ServerIP.SERVLET_GETBOOK, id);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String success = "";
		Book book = null;
		
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				String objBook = item.getString("obj");
				book = FastJsonTools.getObect(objBook, Book.class);
				
				Log.i("learning book name====", book.getName());
				
			} else {
				throw new Exception("无数据！"); 
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return book;
	}
	
	
	/**
	 * 补充
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getAdditionalall(String serverIP, int classId, int page, int rows) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_ADDITIONAL, classId, page, rows);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String obj = "";
		JSONObject item;
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<Book> books = FastJsonTools.getObects(obj, Book.class);
		
		return books;
	}
	
	
	/**
	 * 本地
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	public List<Book> getLocalByall(String serverIP, int classId,  int page, int rows) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_LOCAL,classId, page, rows);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("learning json", jsonString);
		String obj = "";
		JSONObject item;
		try {
			item = new JSONObject(jsonString);
			obj = item.getString("rows");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<Book> books = FastJsonTools.getObects(obj, Book.class);
		
		return books;
	}
	
}
