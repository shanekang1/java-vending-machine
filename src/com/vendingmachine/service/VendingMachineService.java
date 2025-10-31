// /src/com/vendingmachine/service/VendingMachineService.java
package com.vendingmachine.service;

import com.vendingmachine.exception.NotEnoughMoneyException;
import com.vendingmachine.exception.OutOfStockException;

/**
 * 자판기가 반드시 가져야 할 "기능(메서드)"을 정의하는 인터페이스입니다.
 */
public interface VendingMachineService {

    /** 돈을 투입합니다. */
    void insertMoney(int amount);

    /**
     * 음료를 선택합니다.
     * (이 과정에서 '돈 부족', '재고 부족' 예외가 발생할 수 있습니다)
     */
    void selectItem(String slotId) throws NotEnoughMoneyException, OutOfStockException;

    /** 거스름돈을 반환합니다. */
    int returnChange();

    /** 현재 음료 목록과 재고를 화면에 표시합니다. */
    void showItems();
}