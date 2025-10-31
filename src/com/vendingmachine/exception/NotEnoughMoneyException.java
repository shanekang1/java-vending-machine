// /src/com/vendingmachine/exception/NotEnoughMoneyException.java
package com.vendingmachine.exception;

/**
 * "돈이 부족할 때" 우리가 직접 발생시킬 예외 클래스입니다.
 * Java의 기본 Exception을 '상속'받아 만듭니다.
 */
public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String message) {
        super(message); // 부모(Exception) 생성자에게 메시지 전달
    }
}