// /src/com/vendingmachine/domain/Water.java
package com.vendingmachine.domain;

/**
 * Drink 추상 클래스를 '상속'받은 '물' 클래스입니다.
 */
public class Water extends Drink {
    public Water() {
        super("생수", 800);
    }
}