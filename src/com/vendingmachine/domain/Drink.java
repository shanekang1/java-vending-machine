// /src/com/vendingmachine/domain/Drink.java
package com.vendingmachine.domain;

/**
 * 모든 음료의 공통점을 뽑아낸 '추상 클래스'입니다.
 */
public abstract class Drink {
    private String name;
    private int price;

    public Drink(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}