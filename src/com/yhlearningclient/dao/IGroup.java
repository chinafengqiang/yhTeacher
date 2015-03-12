package com.yhlearningclient.dao;

import java.util.List;

import com.yhlearningclient.model.Group;

public interface IGroup {
	public void insert(Group group);
	public void delete(int id);
	public void update(Group group);
	public List<Group> select(String condition);
	public Group getById(int groupId);
	public int getCount();
	public Group getByName(String name);

}
