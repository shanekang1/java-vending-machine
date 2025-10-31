// /src/com/vendingmachine/domain/Cola.java
package com.vendingmachine.domain;

/**
 * Drink 추상 클래스를 '상속'받은 '콜라' 클래스입니다.
 */
public class Cola extends Drink {
    public Cola() {
        // 부모(Drink)의 생성자를 호출하며 이름과 가격을 고정
        super("콜라", 1200); // "코카콜라" -> "콜라"로 변경
    }
}