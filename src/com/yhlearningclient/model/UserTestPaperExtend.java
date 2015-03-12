package com.yhlearningclient.model;

public class UserTestPaperExtend implements java.io.Serializable{

	private static final long serialVersionUID = -9129972468113668714L;

	private Long id;

    private Long userId;
    
    private Long questionId;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}

	public Long getTestPaperId() {
		return testPaperId;
	}

	public void setTestPaperId(Long testPaperId) {
		this.testPaperId = testPaperId;
	}

	public byte[] getNoSelectAnswer() {
		return noSelectAnswer;
	}

	public void setNoSelectAnswer(byte[] noSelectAnswer) {
		this.noSelectAnswer = noSelectAnswer;
	}

	private String time;

    private String score;

    private String isCorrect;

    private Long testPaperId;

    private byte[] noSelectAnswer;
    
}
