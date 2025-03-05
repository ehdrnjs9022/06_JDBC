package com.kh.mvc.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.mvc.model.dto.UserDTO;

/**
 * DAO(Data Access Object)
 * 
 * 데이터베이스 관련된 작업(CRUD)
 * DAO안에 모든 메소드들은 데이터베이스와 관련된 기능으로 만들 것
 * 
 * Controller를 통해 호출된(DAO) 기능을 수행
 * DB에 직접 접근한 후 SQL문을 수행하고 결과 받기(JDBC)
 * Controller->DAO , DAO -> Controller
 */

public class UserDAO {
	
	/*
	 * JDBC 용 객체
	 * 
	 * - Connection : DB연결정보를 담고있는 객체(IP주소, Port ,사용자명 ,비번)
	 * 
	 * - Statement : Connection이 가지고 있는 연결정보 DB에
	 * 							 SQL문을 전달하고 실행하고 결과도 받아오는 객체(도화지)
	 * 
	 * - ResultSet : 실행한 SQL문이 SELECT문일 경우 조회된 결과가 처음 담기는 객체
	 * 
	 * - PreparedStatement : SQL문을 미리 준비하는 개념
	 * 											?(위치홀더)로 확보해놓은 공간을
	 * 											사용자가 입력한 값들과 바인딩해서 SQL문을 수행
	 * 
	 * ** 처리 절차
	 * 
	 * 1) JDBC Driver등록 : DMBC(데이터베이스 관리 시스템)에서 제공하는 클래스를 동적으로 등록
	 * 2) Connection 객체 생성 : 접속하고자하는 DB의 정보를 입력해서 생성
	 * 														(URL, 사용자이름, 비밀번호)
	 * 3_1) PreparedStatment 객체 생성 : (SQL 편집기 역할)
	 * 			Connection 객체를 이용해서 생성(미완성된 SQL문을 미리 전달)
	 * 3_2) 미완성된 SQL문을 완성형태로 만들어주어야 함
	 * => 미완성된 경우에만 해당 / 완성된 경우에는 생략
	 * 4) SQL문을 실행 : executeXXX() => SQL을 인자로 전달하지 않음!
	 * 									> SELECT(DQL) : executeQuery()
	 * 									> DML         : executeUpdate()
	 * 5) 결과받기 : 
	 * 							> SELECT : ResultSet타입 객체(조회된데이터담김)
	 * 							> DML : int(처리된 행의 개수)
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나하나씩 뽑아서 DTO객체 필드에
	 * 			옮겨담은 후 조회 결과가 여러 행일 경우 List로 관리
	 * 6_2) 트랜잭션 처리
	 * 7) (생략될 수 있음) 자원반납 (close) => 생성의 역순으로
	 * 8) 결과반환 -> Controller
	 * 								SELECT > 6_1에서 만든거
	 * 								DML    > 처리된 행의 개수
	 * 
	 * 
	 */
	
		// final 변수는 대문자로
		private final String URL = "jdbc:oracle:thin:@112.221.156.34:12345:XE";
		private final String USERNAME = "KH25_JDK";		
		private final String PASSWORD = "KH1234";
	
	
	//한번만 제일먼저실행
		static {
			try {
			Class.forName("oracle.jdbc.OracleDriver");
			}catch(ClassNotFoundException e) {
					System.out.println("ojdbc 잘넣었는지" 
														+ "\n 오타 안났나요?");		
			}
		}
	
		public List<UserDTO> findAll(Connection conn) {
			
			/*
			 * VO / DTO / Entity
			 * 
			 * 1명의 회원의 정보는 1개의 UserDTO객체의 필드에 값을 담아야겠다.
			 * 
			 * 문제점 : userDTO가 몇개가 나올지 알 수 없음
			 * 
			 */
			List<UserDTO> list = new ArrayList<UserDTO>();
			String sql = "SELECT "
												+	 "USER_NO "
												+", USER_ID"
												+", USER_PW"
												+", USER_NAME"
												+", ENROLL_DATE "
										+ "FROM "
													+ "TB_USER "
										+ "ORDER "
												+"BY "
													+ "ENROLL_DATE DESC";
			
					 
			/** 지역변수 사용하려면 초기화 해야함
			 * - null 값을 주는 이유는 try문에서 예외가 발생 했을 때 
			 * 지역변수가 초기화되지 않은 문제를 방지하기 위해서
			 * 
			 * - null(초기화)를 해주면 try문 안에서 conn,pstmt,rset이 정상적으로
			 * 값을 가지면 그대로 사용하고, 만약 예외가 발생해도 해당 변수들이
			 * 존재하는 상태로 남아있게됨 
			 */
			
			
			//Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			
			try {
//				conn =          // 커넥션 내놔
//					DriverManager.getConnection("jdbc:oracle:thin:@112.221.156.34:12345:XE",
//																			"KH25_JDK", "KH1234");
					
				 pstmt = conn.prepareStatement(sql);
				 rset = pstmt.executeQuery();
				
				while(rset.next()) {
					UserDTO user = new UserDTO();
					user.setUserNO(rset.getInt("USER_NO")); 
					user.setUserId(rset.getNString("USER_ID"));
					user.setUserPw(rset.getNString("USER_PW"));
					user.setUserName(rset.getNString("USER_NAME"));
					user.setEnrollDate(rset.getDate("ENROLL_DATE"));
					list.add(user);
					
				} 
			
			}	catch (SQLException e) {
					System.out.println("오타가 나지 않았나요?");
					e.printStackTrace();
			}	
			// catch 위에 return 이 없기떄문에 finally 필요X
			// 하지만 다른사람이 봤을때 무조건 실행해야한다는 표현일떄 사용하기도함
			finally {
			// 객체가 정상적으로 작동했을때만 close를 하기위해
			// null이 아니다 라는 의미는 보통 객체가 정상적으로 생성되었을 가능성이 높다
			// null 이면 참조주소가없다 -> 객체자체가 없기 때문에 close()를 할 수 없다
				
				try { 
					if(rset != null) {
							rset.close();
					}
				} catch (SQLException e) {
						System.out.println("DB서버 이상함");
				}
			
				try {
					if(pstmt != null) {
							pstmt.close();
					}
				} catch (SQLException e) {
						System.out.println("DB서버 이상함");
				}
			
				try {
					if(conn != null) {
							conn.close();
					}
				} catch (SQLException e) {
						e.printStackTrace();
				}
			}
			
			return list;
		}
	
		/**
		 * 
		 * @param user 사용자가 입력한 아이디 / 비밀번호 / 이름이 각각 필드에
		 * 대입되어 있음
		 * @return 아직 머 돌려줄지 안정함
		 */
		
		public int insertUser(UserDTO user) {
			
				Connection conn = null;
				PreparedStatement pstmt = null;
			
				String sql = "INSERT "
										+ "INTO "
														+ "TB_USER "
										+ "VALUES "
														+ "("
														+ "SEQ_USER_NO.NEXTVAL"
												+ ", ?"
												+ ", ?"
												+ ", ?"
												+ ", SYSDATE"
													+ ")";
			int result = 0;
			try {
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				
				/*
				 * conn =
				 *    DriverManager.getConnection("jdbc:oracle:thin:@112.221.156.34:12345:XE",
				 * "KH25_JDK", "KH1234");
				 */
					//conn.setAutoCommit(false);
					pstmt = conn.prepareStatement(sql);
					
					
					pstmt.setString(1, user.getUserId());
					pstmt.setString(2, user.getUserPw());
					pstmt.setString(3, user.getUserName());
					
					result = pstmt.executeUpdate();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}	finally {
			} try {
					if(pstmt != null && !pstmt.isClosed()) pstmt.close();
			} catch (SQLException e) {
					e.printStackTrace();
			}
			 try {
				 	if(conn !=null) conn.close();
			 }catch (Exception e) {
				 e.printStackTrace();
			 }
				return result;
			}
		
		
	
	
}



