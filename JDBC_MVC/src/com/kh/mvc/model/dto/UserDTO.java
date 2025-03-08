package com.kh.mvc.model.dto;

import java.sql.Date;

public class UserDTO {
	
	private int userNO;
	private String userId;
	private String userPw;
	private String userName;
	private Date enrollDate;
	
	public UserDTO() {
		super();
	}
	
	public UserDTO(int userNO, String userId, String userPw, String userName, Date enrollDate) {
		super();
		this.userNO = userNO;
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
		this.enrollDate = enrollDate;
	}

	public int getUserNO() {
		return userNO;
	}
	public void setUserNO(int no) {
		this.userNO = no;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	@Override
	public String toString() {
		return "UserDTO [userNO=" + userNO + ", userId=" + userId + ", userPw=" + userPw + ", userName=" + userName
				+ ", enrollDate=" + enrollDate + "]";
	}
	
	
	
	
	
	
}
