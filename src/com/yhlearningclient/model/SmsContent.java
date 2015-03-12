package com.yhlearningclient.model;

/**
 *对像组装
 */
public class SmsContent {

	private String name;
	
	private String content;
	
	private int canSend;
	
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCanSend() {
		return canSend;
	}

	public void setCanSend(int canSend) {
		this.canSend = canSend;
	}
}
