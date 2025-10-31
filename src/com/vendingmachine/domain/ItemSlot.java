// /src/com/vendingmachine/domain/ItemSlot.java
package com.vendingmachine.domain;

/**
 * Map에 저장될 '값(Value)'입니다.
 * 특정 '음료(Drink)'가 '몇 개(quantity)' 남았는지 묶어서 관리합니다.
 */
public class ItemSlot {
    private Drink drink;
    private int quantity; // 재고

    public ItemSlot(Drink drink, int quantity) {
        this.drink = drink;
        this.quantity = quantity;
    }

    // Getter (정보 조회)
    public Drink getDrink() {
        return drink;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public int getPrice() {
        return drink.getPrice();
    }
    
    public String getName() {
        return drink.getName();
    }

    // Setter (정보 수정)
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** 재고가 남아있는지 확인하는 헬퍼(helper) 메서드 */
    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    /** 구매 시 재고 1 감소 */
    public void decreaseStock() {
        this.quantity--;
    }
}