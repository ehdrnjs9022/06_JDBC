package com.kh.mvc.controller;

import java.util.List;
import java.util.Map;

import com.kh.mvc.model.dao.UserDAO;
import com.kh.mvc.model.dto.UserDTO;
import com.kh.mvc.model.service.MemberService;

public class UserController {

/**
 * VIEW에서 온 요청을 처리해주는 클래스입니다.
 * 메소드로 전달된 데이터값을 가공처리한 후 DAO로 전달합니다
 * DAO로부터 반환받은 결과를 사용자가 보게될 View(응답화면)에 반환합니다.
 * UserView->Controller  Controller -> UserView
 */
	
		private UserDAO userDao = new UserDAO();
		private MemberService userService= new MemberService(); 
	
		public List<UserDTO> findAll() {
				return userService.findAll();
				// 여기선 변수에담아서 쓸게아니라 View 에 반환만해주기위함
				// 스택메모리 밖으로 뺴놓음
				// 사라지지 않게 하기위해 변수에 담을떄
				// List<UserDTO> = userDao.findAll();
			
		}

		public int insertUser(String userId, String userPw, String userName) {
				// 여기3가지는 변수
			
			UserDTO user = new UserDTO();
			user.setUserId(userId);
			user.setUserPw(userPw);
			user.setUserName(userName);
			
			return userDao.insertUser(user); // DAO에게 값을 넘겨주는 구문
		}


}
