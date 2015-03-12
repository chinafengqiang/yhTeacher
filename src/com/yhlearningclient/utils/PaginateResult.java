package com.yhlearningclient.utils;

import java.util.ArrayList;

public class PaginateResult {

	/**
	 * 结果集
	 */
	private ArrayList list;
	
	/**
	 * 记录总数
	 */
	private Integer recordCount;

	public ArrayList getList() {
		return list;
	}

	public void setList(ArrayList list) {
		this.list = list;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
}
