package model.service;

/** 마이페이지 비즈니스 규칙 위반 (화면에 그대로 보여줄 메시지를 담는다) */
public class MyPageException extends Exception {
    public MyPageException(String msg) {
        super(msg);
    }
}
