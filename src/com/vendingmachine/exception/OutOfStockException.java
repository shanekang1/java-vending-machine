// /src/com/vendingmachine/exception/OutOfStockException.java
package com.vendingmachine.exception;

/**
 * "재고가 부족할 때" 우리가 직접 발생시킬 예외 클래스입니다.
 */
public class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}