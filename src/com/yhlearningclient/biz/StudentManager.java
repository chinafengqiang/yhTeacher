package com.yhlearningclient.biz;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.yhlearningclient.dao.IStudent;
import com.yhlearningclient.dao.impl.StudentService;
import com.yhlearningclient.db.DB.TABLES.STUDENT;
import com.yhlearningclient.model.Student;

public class StudentManager {
	private IStudent contactService;

	public StudentManager(Context context) {
		contactService = new StudentService(context);

	}

	public List<Student> getContactByCondition(String condition, int testPaperId) {

		condition = STUDENT.FIELDS.NAME + " like " + "'" + condition + "%'";

		if (condition.equals("")) {
			condition = "1=1";
		}
		return contactService.getProductByCondition(condition, testPaperId);

	}

	public int getCountByGroupId(int groupId, int testPaperId) {
		String condition = STUDENT.FIELDS.GROUPID + " = " + groupId;
		return contactService.getCount(condition, testPaperId);

	}

	public int getAllCount(int testPaperId) {
		return contactService.getCount("1=1", testPaperId);
	}

	public List<Student> getAllContacts() {
		return contactService.getAll();
	}

	public void addContact(Student contact) {
		contactService.insert(contact);

	}

	public void modifyContact(Student contact) {
		contactService.update(contact);

	}

	public Student getContactById(int id) {
		return contactService.getById(id);

	}

	/**
	 * 通过标记删除
	 */
	public void deletByIsMark() {
		contactService.delete(STUDENT.FIELDS.ISMARK + " = " + "'true'");
	}

	/**
	 * 通过ID删除
	 */
	public void deleteById(int id) {
		contactService.delete(STUDENT.FIELDS.ID + " = " + id);

	}

	/**
	 * 通过组别ID获得联系人
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Student> getContactByGroupId(int groupId,  int testPaperId) {
		System.out.println("1");
		System.out.println("2");
		return contactService.getProductByCondition(STUDENT.FIELDS.GROUPID+ "=" + groupId, testPaperId);
	}

	// public Contact getContactById(int groupId,int contactId){
	// return
	// contactService.getProductByCondition(CONTACT.FIELDS.GROUPID+"="+groupId).get(0);
	//
	// }
	/**
	 * 全组成员换组
	 */
	public void changeGroupByGroup(int reGroupId, int toGroupId) {
		String sql = String.format(STUDENT.SQL.CHANGE_GROUP, toGroupId,
				STUDENT.FIELDS.GROUPID + " = " + reGroupId);
		Log.i("Item", sql);
		contactService.changeGroup(sql);

	}

	/**
	 * 标记换组
	 */
	public void changeGroupByMark(int toGroupId) {
		String sql = String.format(STUDENT.SQL.CHANGE_GROUP, toGroupId,
				STUDENT.FIELDS.ISMARK + " = " + "'true'");
		Log.i("Item", sql);
		contactService.changeGroup(sql);

	}

	/**
	 * 单人换组
	 * 
	 * @param contactId
	 * @param toGroupID
	 */
	public void changeGroupByCotact(int contactId, int toGroupId) {
		String sql = String.format(STUDENT.SQL.CHANGE_GROUP, toGroupId,
				STUDENT.FIELDS.ID + " = " + contactId);
		Log.i("Item", sql);
		contactService.changeGroup(sql);
	}

	public void initIsMark(String state) {
		contactService.initIsMark(state, "1=1");

	}

	public void initIsMarkById(String state, int id) {
		contactService.initIsMark(state, STUDENT.FIELDS.ID + " = " + id);
	}
	
	/**
	 * 获得用户
	 * @return
	 */
	public List<Student> getAllStudentList(String serverIP, Long classId){
		return contactService.getAllStudentList(serverIP, classId);
	}

}
