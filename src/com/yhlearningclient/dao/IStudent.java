package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.Student;

public interface IStudent {
	public void insert(Student contact) ;
	public void delete(String condition);
	public void update(Student contact);
	public void initIsMark(String state,String condition);
	public List<Student> getAll();
	public List<Student> getProductByCondition(String condition,  int testPaperId);
	public Student getById(int productId);
	public int getCount(String condition, int testPaperId);
	public void changeGroup(String sql);
	/**
	 * 获得用户
	 * @return
	 */
	public List<Student> getAllStudentList(String serverIP, Long classId);



}
