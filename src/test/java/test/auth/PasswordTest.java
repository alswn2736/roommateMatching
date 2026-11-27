package test.auth;

import model.util.auth.PasswordUtil;

//비밀번호 해시 생성 및 검증 테스트
public class PasswordTest {
	 public static void main(String[] args) {

	        String password = "test1234";
	        String hashed = PasswordUtil.hash(password);

	        // 해시 길이 확인
	        System.out.println("해시 길이: " + hashed.length());

	        // 올바른 비밀번호 검사
	        System.out.println("정상 비밀번호: "
	                + PasswordUtil.matches("test1234", hashed));

	        // 잘못된 비밀번호 검사
	        System.out.println("잘못된 비밀번호: "
	                + PasswordUtil.matches("wrong1234", hashed));
	    }
}
