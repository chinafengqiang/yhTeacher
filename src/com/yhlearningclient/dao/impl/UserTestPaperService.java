package com.yhlearningclient.dao.impl;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.yhlearningclient.common.HttpUtil;
import com.yhlearningclient.constant.ServerIP;
import com.yhlearningclient.dao.IUserTestPaperService;
import com.yhlearningclient.db.DB;
import com.yhlearningclient.db.DB.TABLES.TEST_PAPER_QUESTION;
import com.yhlearningclient.db.DB.TABLES.USER_TEST_PAPER;
import com.yhlearningclient.db.DBHelper;
import com.yhlearningclient.model.UserTestPaper;
import com.yhlearningclient.model.UserTestPaperExtend;
import com.yhlearningclient.utils.FastJsonTools;
import com.yhlearningclient.utils.ImageService;
import com.yhlearningclient.utils.ImageTools;


/**
 * 试卷方法
 * @author Administrator
 */
public class UserTestPaperService implements IUserTestPaperService{
	
	private DBHelper helper = null;
	
	public UserTestPaperService(Context context) {
		helper = new DBHelper(context);
	}

	/**
	 * 保存试卷信息
	 * @param testPaper
	 */
	public void InsertUserTestPaper(UserTestPaper userTestPaper) {

		ContentValues values = new ContentValues();
		values.put(USER_TEST_PAPER.FIELDS._ID, userTestPaper.get_id());
		values.put(USER_TEST_PAPER.FIELDS.USER_ID, userTestPaper.getUserId());
		values.put(USER_TEST_PAPER.FIELDS.QUESTION_ID, userTestPaper.getQuestionId());
		values.put(USER_TEST_PAPER.FIELDS.TIME, userTestPaper.getTime());
		values.put(USER_TEST_PAPER.FIELDS.SCORE, userTestPaper.getScore());
		values.put(USER_TEST_PAPER.FIELDS.IS_CORRECT, userTestPaper.getIsCorrect());
		values.put(USER_TEST_PAPER.FIELDS.TEST_PAPER_ID, userTestPaper.getTestPaperId());
		values.put(USER_TEST_PAPER.FIELDS.NO_SELECT_ANSWER, userTestPaper.getNoSelectAnswer());
		
		this.helper.insert(USER_TEST_PAPER.TABLENAME, values);
	}
	
	/**
	 * 获得分数
	 * @return
	 */
	public double getSumScore(int testPaperId) {
		String sql = "Select sum(score) From user_test_paper where test_paper_id = "+testPaperId;
		double result = 0;
		try {
			result = helper.rawQuerySingle(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("TestPaperQuestion", "getSumScore result=" + result);
		return result;
	}
	
	
	/**
	 * 获得是否存在用户记录数据
	 * @return
	 */
	public boolean profileExist(int testPaperId) {
		List<UserTestPaper> userTestPapers = getUserTestPaperById(testPaperId);
		if(userTestPapers.size() > 0){
			return true;
		}
		return false;
	}

	public List<UserTestPaper> getUserTestPaperById(int testPaperId) {
		String condition = " test_paper_id = "+ testPaperId +" order by id";

		String sql = String.format(DB.TABLES.USER_TEST_PAPER.SQL.SELECT, condition);
		
		Log.i("TestPaperQuestion", "sql condition==="+sql);
		
		List<UserTestPaper> userTestPapers = new ArrayList<UserTestPaper>();
		
		try {
			Cursor cursor = helper.SELECT(sql);
			while (cursor.moveToNext()) {
				UserTestPaper userTestPaper = new UserTestPaper();
				userTestPaper.setId(cursor.getInt(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.ID)));
				userTestPaper.setUserId(cursor.getInt(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.USER_ID)));
				userTestPaper.setTime(cursor.getString(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.TIME)));
				userTestPaper.setScore(cursor.getString(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.SCORE)));
			//	userTestPaper.setNoSelectAnswer(cursor.getString(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.NO_SELECT_ANSWER)));
				userTestPaper.setIsCorrect(cursor.getString(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.IS_CORRECT)));
				userTestPaper.setQuestionId(cursor.getInt(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.QUESTION_ID)));
				userTestPaper.setTestPaperId(cursor.getInt(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.TEST_PAPER_ID)));
				
				userTestPapers.add(userTestPaper);
			}
			cursor.close();
			return userTestPapers;
		} catch (Exception e) {
			return null;
		} finally {
			helper.closeDataBase();
		}
	}
	
	/**
	 * 取得考卷对错矩阵
	 * 
	 * @param id
	 * @return
	 */
	public  boolean[] getRwMatrix(int testPaperId) {
	
		List<UserTestPaper> userTestPapers = getUserTestPaperById(testPaperId);
		if (userTestPapers != null) {
			
			boolean[] rwMatrix = new boolean[userTestPapers.size()];
			for(int i = 0; i < userTestPapers.size(); i++){
				UserTestPaper userTestPaper = userTestPapers.get(i);
				if(userTestPaper.getIsCorrect().equals("R")){
					rwMatrix[i] = true;
				} else {
					rwMatrix[i] = false;
				}
			}
			return rwMatrix;
		}
		return null;
	}
	
	/**
	 * 创建用户试卷记录
	 * @param userTestPaper
	 * @throws Exception 
	 */
	public boolean saveUserTestPaper(String serverIP, UserTestPaper userTestPaper) throws Exception{
		String path = serverIP + ServerIP.SERVLET_USER_TESETPAPER;
		String success = "";
		String params = "userId="+userTestPaper.getUserId()+"&questionId="+userTestPaper.getQuestionId() +
				"&score="+userTestPaper.getScore() + "&isCorrect="+userTestPaper.getIsCorrect() +
				"&testPaperId="+userTestPaper.getTestPaperId()+"&converSelectAnswer="+userTestPaper.getNoSelectAnswer();
		
		byte[] data = HttpUtil.getDataFromUrl(path,params);
		String jsonString = new String(data);
		Log.i("learning json==", jsonString);
		
		try {
			JSONObject item = new JSONObject(jsonString);
			success = item.getString("success");
			
			if("true".equals(success)){
				return true; 
			} else {
				throw new Exception("添加错误！"); 
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	/**
	 * 获得用户结果集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	private void getUserTestPaperByALL(String serverIP, int last_id, int testPaperId) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_GET_USER_TESETPAPER, testPaperId);
		Log.i("TestPaperQuestion", "path=="+path);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("TestPaperQuestion", "jsonString=="+jsonString);
//		String obj = "";
//		JSONObject item;
//		try {
//			
//			JSONObject jObject = new JSONObject(jsonString);
//			String obj1 = jObject.getString("data");
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		List<UserTestPaperExtend> userTestPaperExtends = FastJsonTools.getObects(jsonString, UserTestPaperExtend.class);
		
		Log.i("TestPaperQuestion", "总数=="+userTestPaperExtends.size());
		
		for(UserTestPaperExtend utpExtend : userTestPaperExtends){
			UserTestPaper userTestPaper = new UserTestPaper();
			
			userTestPaper.set_id(Integer.parseInt(utpExtend.getId().toString()));
			userTestPaper.setUserId(Integer.parseInt(utpExtend.getUserId().toString()));
			userTestPaper.setQuestionId(Integer.parseInt(utpExtend.getQuestionId().toString()));
			userTestPaper.setTime("");
			userTestPaper.setScore(utpExtend.getScore());
			userTestPaper.setIsCorrect(utpExtend.getIsCorrect());
			userTestPaper.setTestPaperId(Integer.parseInt(utpExtend.getTestPaperId().toString()));
			
			if(utpExtend.getTime() != null && !"".equals(utpExtend.getTime())){
			
				String imageNote = serverIP + utpExtend.getTime();
				
				Log.i("TestPaperQuestion", "imageNote="+imageNote);
				byte[] result = null;
				try {
					InputStream is  = ImageService.getInputStream(imageNote);
					Bitmap  bitmap = BitmapFactory.decodeStream(is);
					if(bitmap == null){
						return;
					}
					
					result = ImageTools.getByteFromBitmap(bitmap);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				userTestPaper.setNoSelectAnswer(result);
				
				//插入
				if(getUserTestPaperCount(userTestPaper.get_id()) == 0){
					this.InsertUserTestPaper(userTestPaper);
				}
			}
			
//			//插入
//			if (userTestPaper.get_id() > last_id) {
//				InsertUserTestPaper(userTestPaper);
//			}
			
	
			
		}
	}
	
	
	public int getUserTestPaperCount(int id){
		String condtion = "_id="+id;
		String sql_count = MessageFormat.format(DB.TABLES.USER_TEST_PAPER.SQL.SELECT_COUNT,condtion);
		int totalCount = (int) helper.rawQuerySingle(sql_count);  //总条数
		
		return totalCount;
	}
	
	public boolean isUserTestPaperList(String serverIP,  int testPaperId){
		
		boolean flag = false;
		int last_id = this.getUserTestPaperLastId();
		Log.i("isUserTestPaperList", "last_id="+last_id);
		getUserTestPaperByALL(serverIP, last_id, testPaperId);
		Log.i("isUserTestPaperList", "初始化数据结束");
		flag = true;
		return flag;
	}
	
	
	/**
	 * 获得试卷分类最后值
	 * @return
	 */
	public int getUserTestPaperLastId() {
		String sql = "select _id from user_test_paper order by _id desc";
		int last_id = 0;
		try {
			Cursor cursor = helper.SELECT(sql);
			if (cursor.moveToFirst()) {
				last_id = cursor.getInt(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS._ID));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("sq", "user_test_paper last_id=" + last_id);
		return last_id;
	}
	
	/**
	 * @param
	 * @return
	 */
	public int getByTotalCount(String questionId){
		String totalSQLCount = "Select count(user_id) From user_test_paper  where question_id="+questionId;
		int totalCount = (int) helper.rawQuerySingle(totalSQLCount);  //总条数
		
		return totalCount;

	}
	
	/**
	 * @param
	 * @return
	 */
	public int getByRCount(String questionId){
		String rSQLCount = "Select count(user_id) From user_test_paper where question_id="+questionId+" and is_correct='R'";
		int rCount = (int) helper.rawQuerySingle(rSQLCount);  //总条数
		
		return rCount;

	}
	
	
	/**
	 * @param
	 * @return
	 */
	public int getUserCount(String userId){
		String rSQLCount = "Select count(*) From user_test_paper where user_id="+userId;
		int rCount = (int) helper.rawQuerySingle(rSQLCount);  //总条数
		
		return rCount;

	}
	
	/**
	 * 获得答案
	 * @param questionId
	 * @return
	 */
	public List<UserTestPaper> getUserTestPaperByQuestionId(int questionId, int userId) {
		String condition = " user_id ="+userId+" and question_id = "+ questionId +" order by id";

		String sql = String.format(DB.TABLES.USER_TEST_PAPER.SQL.SELECT, condition);
		
		Log.i("TestPaperQuestion", "sql condition==="+sql);
		
		List<UserTestPaper> userTestPapers = new ArrayList<UserTestPaper>();
		
		try {
			Cursor cursor = helper.SELECT(sql);
			while (cursor.moveToNext()) {
				UserTestPaper userTestPaper = new UserTestPaper();
				userTestPaper.setNoSelectAnswer(cursor.getBlob(cursor.getColumnIndex(USER_TEST_PAPER.FIELDS.NO_SELECT_ANSWER)));
				userTestPapers.add(userTestPaper);
			}
			cursor.close();
			return userTestPapers;
		} catch (Exception e) {
			return null;
		} finally {
			helper.closeDataBase();
		}
	}
	
}
