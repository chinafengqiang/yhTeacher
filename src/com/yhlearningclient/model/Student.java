package com.yhlearningclient.model;

import java.io.Serializable;

public class Student implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5034871569203923761L;
	private int id;
	private int _id; //
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getIsMark() {
		return isMark;
	}
	public void setIsMark(String isMark) {
		this.isMark = isMark;
	}
	private String name;
	private int groupId;
	private String isMark;

}
