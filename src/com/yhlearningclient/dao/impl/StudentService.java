package com.yhlearningclient.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.yhlearningclient.common.HttpUtil;
import com.yhlearningclient.constant.ServerIP;
import com.yhlearningclient.dao.IStudent;
import com.yhlearningclient.db.DB.TABLES.STUDENT;
import com.yhlearningclient.db.DBHelper;
import com.yhlearningclient.model.Student;
import com.yhlearningclient.model.StudentExtend;
import com.yhlearningclient.utils.FastJsonTools;

public class StudentService implements IStudent {
	private DBHelper dbHelper = null;

	public StudentService(Context context) {

		dbHelper = new DBHelper(context);

	}

	/**
	 * 数据库的增加操作
	 * 
	 * @param product
	 * @throws SQLException
	 */
	
	public void insert(Student c) {
		//this.ps.add(p);
		ContentValues values =  new ContentValues();
		values.put(STUDENT.FIELDS._ID, c.get_id());
		values.put(STUDENT.FIELDS.NAME, c.getName());
		values.put(STUDENT.FIELDS.GROUPID,c.getGroupId());
		values.put(STUDENT.FIELDS.ISMARK,c.getIsMark());
		this.dbHelper.insert(STUDENT.TABLENAME, values);
	}

	/**
	 * 数据库的删除操作
	 * 
	 * @param 
	 * @throws SQLException
	 */
	public void delete(String condition) throws SQLException {
		String sql = String.format(STUDENT.SQL.DELETE_BY_ID, condition);
		dbHelper.ExecuteSQL(sql);
	}

	/**
	 * 数据库的修改操作
	 * 
	 * @param product
	 * @throws SQLException
	 * 
	 * 
	 */
	
	public void update(Student c) throws SQLException {
		ContentValues values =  new ContentValues();
		values.put(STUDENT.FIELDS.NAME, c.getName());
		values.put(STUDENT.FIELDS.GROUPID,c.getGroupId());
		values.put(STUDENT.FIELDS.ISMARK,c.getIsMark());
		this.dbHelper.update(STUDENT.TABLENAME, values, STUDENT.FIELDS.ID+"= ? ", new String[]{c.getId()+""});
		
	}


	/**
	 * 标记状态的设置
	 * 
	 * @param state
	 */
	public void initIsMark(String state,String condition) {
		String sql = String.format(STUDENT.SQL.INIT_ISMARK, state,condition);
		System.out.println(sql + "  mark");
		dbHelper.ExecuteSQL(sql);
	}
	/**
	 * 改变组别
	 * @param sql
	 */
	public void changeGroup(String sql){
		dbHelper.ExecuteSQL(sql);
		
		
	}

	/**
	 * 获得所有联系人
	 * 
	 * @return
	 */
	public List<Student> getAll() {
		try {
			String sql = "Select distinct(b.name) as dname, b.id, b._id, b.groupid," +
					" b.isMark From user_test_paper a, student b where a.user_id=b._id";
	//		String sql = String.format(STUDENT.SQL.SELECT, " 1=1 ");
			return this.selectExtend(sql);
		} finally {
			dbHelper.closeDataBase();
		}
	}

	/**
	 * 通过条件获得联系人
	 * 
	 * @param condition
	 * @return
	 */
	public List<Student> getProductByCondition(String condition, int testPaperId) {
		//System.out.println("3");
		
		try {
			String sql = "Select distinct(b.name) as dname, b.id, b._id, b.groupid," +
					" b.isMark From user_test_paper a, student b where a.user_id=b._id and a.test_paper_id="+ testPaperId + "  and "+condition;
		//	String sql = String.format(sql1,  condition );
			//System.out.println(sql);
			Log.i("Item", sql);
		
			return this.selectExtend(sql);
		} finally {
			dbHelper.closeDataBase();
		}
	}

	/**
	 * 通过商品ID获得联系人
	 * 
	 * @param productId
	 * @return
	 */
	public Student getById(int productId) {
		String sql = String.format(STUDENT.SQL.SELECT, STUDENT.FIELDS.ID + "="
				+ productId);
		try {
			return this.select(sql).get(0);
		} catch (Exception ex) {
			return null;
		} finally {
			dbHelper.closeDataBase();
		}

	}

	/**
	 * 数据库的查询操作
	 * 
	 * @param condition
	 * @return
	 * @throws SQLException
	 */

	public List<Student> selectExtend(String sql) throws SQLException {
		List<Student> list_contact = new ArrayList<Student>();

		try {
			Cursor cursor = dbHelper.SELECT(sql);

			while (cursor.moveToNext()) {

				Student contact = new Student();
				contact.setName(cursor.getString(cursor.getColumnIndex("dname")));
				contact.setId(cursor.getInt(cursor.getColumnIndex(STUDENT.FIELDS.ID)));
				contact.set_id(cursor.getInt(cursor.getColumnIndex(STUDENT.FIELDS._ID)));
				contact.setGroupId(cursor.getInt(cursor.getColumnIndex(STUDENT.FIELDS.GROUPID)));
				contact.setIsMark(cursor.getString(cursor.getColumnIndex(STUDENT.FIELDS.ISMARK)));

				list_contact.add(contact);
			}
			cursor.close();

			return list_contact;
		} catch (Exception e) {
			return null;
		} finally {
			dbHelper.closeDataBase();
		}

	}
	
	/**
	 * 数据库的查询操作
	 * 
	 * @param condition
	 * @return
	 * @throws SQLException
	 */

	public List<Student> select(String sql) throws SQLException {
		List<Student> list_contact = new ArrayList<Student>();

		try {
			Cursor cursor = dbHelper.SELECT(sql);

			while (cursor.moveToNext()) {

				Student contact = new Student();
				contact.setId(cursor.getInt(cursor.getColumnIndex(STUDENT.FIELDS.ID)));
				contact.set_id(cursor.getInt(cursor.getColumnIndex(STUDENT.FIELDS._ID)));
				contact.setName(cursor.getString(cursor.getColumnIndex(STUDENT.FIELDS.NAME)));
				contact.setGroupId(cursor.getInt(cursor.getColumnIndex(STUDENT.FIELDS.GROUPID)));
				contact.setIsMark(cursor.getString(cursor.getColumnIndex(STUDENT.FIELDS.ISMARK)));

				list_contact.add(contact);
			}
			cursor.close();

			return list_contact;
		} catch (Exception e) {
			return null;
		} finally {
			dbHelper.closeDataBase();
		}

	}
	/**
	 * 获得联系人总数
	 */
	public int getCount(String condition, int testPaperId) {
		try {
			
			String sql_count =	"Select count(distinct(name)) From user_test_paper a, " +
					"student b where a.user_id=b._id and  a.test_paper_id="+ testPaperId + " and "+ condition;
					
		//	String sql_count = String.format(STUDENT.SQL.COUNT, condition);

			Cursor cursor = dbHelper.SELECT(sql_count);
			System.out.println(sql_count);
			int totalCount = -1;
			if (cursor.moveToNext()) {
				totalCount = (int) cursor.getDouble(0);

			}
			cursor.close();
			return totalCount;
		} finally {
			dbHelper.closeDataBase();
		}

	}
	
	
	/**
	 * 获得用户
	 * @return
	 */
	public List<Student> getAllStudentList(String serverIP, Long classId){
		
		Log.i("getAllStudentList", "初始化数据开始");

		int last_id = this.getStudentLastId();
		getStudentByClassId(serverIP, last_id, classId);
		Log.i("getAllStudentList", "初始化数据结束");
		return getAll();
	}
	
	
	/**
	 * 获得试卷分类最后值
	 * @return
	 */
	public int getStudentLastId() {
		String sql = "select _id from STUDENT order by _id desc";
		int last_id = 0;
		try {
			Cursor cursor = dbHelper.SELECT(sql);
			if (cursor.moveToFirst()) {
				last_id = cursor.getInt(cursor.getColumnIndex(STUDENT.FIELDS._ID));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("sq", "getStudentLastId last_id=" + last_id);
		return last_id;
	}
	
	/**
	 * 获得试卷集合
	 * @param page 默认1,  rows 默认10
	 * @return
	 */
	private void getStudentByClassId(String serverIP, int last_id, Long classId) {
		
		String path = String.format(serverIP + ServerIP.SERVLET_QUERY_STUDENT, classId);
		byte[] data = HttpUtil.getDataFromUrl(path);
		String jsonString = new String(data);
		Log.i("getStudentByClassId json", jsonString);
		
		List<StudentExtend> studentExtends = FastJsonTools.getObects(jsonString, StudentExtend.class);
		
		for(StudentExtend set : studentExtends){
			
			Student s = new Student();
			s.set_id(set.getId());
			s.setName(set.getName());
			s.setGroupId(1);
			s.setIsMark("false");
			
			//插入
			if (s.get_id() > last_id) {
				insert(s);
			}
			
		}
		
		
	}

}
