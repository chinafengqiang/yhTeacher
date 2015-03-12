package com.yhlearningclient.model;

import java.io.Serializable;

public class StudentExtend implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5034871569203923761L;
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;

}
