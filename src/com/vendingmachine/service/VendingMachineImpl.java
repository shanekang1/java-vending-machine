// /src/com/vendingmachine/service/VendingMachineImpl.java
package com.vendingmachine.service;

// 도메인 클래스 임포트
import com.vendingmachine.domain.Coffee;
import com.vendingmachine.domain.Cola;
import com.vendingmachine.domain.ItemSlot;
import com.vendingmachine.domain.Milk;
import com.vendingmachine.domain.Water;
// 예외 클래스 임포트
import com.vendingmachine.exception.NotEnoughMoneyException;
import com.vendingmachine.exception.OutOfStockException;
// Java 유틸리티 임포트
import java.util.HashMap;
import java.util.Map;

/**
 * VendingMachineService 인터페이스를 '구현'하는 실제 자판기 클래스
 */
public class VendingMachineImpl implements VendingMachineService {

    private Map<String, ItemSlot> inventory = new HashMap<>();
    private int currentBalance = 0; // 현재 투입 금액

    /**
     * 생성자: 자판기 객체가 처음 만들어질 때 재고를 초기화합니다.
     */
    public VendingMachineImpl() {
        inventory.put("A1", new ItemSlot(new Cola(), 5));    // A1 슬롯: 콜라 5개
        inventory.put("A2", new ItemSlot(new Water(), 10));  // A2 슬롯: 물 10개
        inventory.put("B1", new ItemSlot(new Coffee(), 1));  // B1 슬롯: 커피 1개
        inventory.put("B2", new ItemSlot(new Milk(), 3));    // B2 슬롯: 우유 3개
    }

    @Override
    public void insertMoney(int amount) {
        if (amount > 0) {
            this.currentBalance += amount;
            System.out.println("[투입] " + amount + "원 (현재 잔액: " + this.currentBalance + "원)");
        } else {
            System.out.println("[오류] 0원 이하의 금액은 투입할 수 없습니다.");
        }
    }

    @Override
    public void selectItem(String slotId) 
            throws NotEnoughMoneyException, OutOfStockException {
        
        ItemSlot selectedSlot = inventory.get(slotId.toUpperCase());
        
        if (selectedSlot == null) {
            System.out.println("[오류] '" + slotId + "'는 존재하지 않는 슬롯입니다.");
            return;
        }

        if (selectedSlot.isOutOfStock()) {
            throw new OutOfStockException("[재고 부족] '" + selectedSlot.getName() + "' 상품이 모두 소진되었습니다.");
        }

        int price = selectedSlot.getPrice();
        if (this.currentBalance < price) {
            throw new NotEnoughMoneyException("[잔액 부족] " + (price - this.currentBalance) + "원이 부족합니다.");
        }

        this.currentBalance -= price;
        selectedSlot.decreaseStock();

        System.out.println("[구매 성공] 🥤 '" + selectedSlot.getName() + "'가 나왔습니다! (남은 재고: " + selectedSlot.getQuantity() + "개)");
    }

    @Override
    public int returnChange() {
        int change = this.currentBalance;
        this.currentBalance = 0;
        
        if (change > 0) {
            System.out.println("[반환] 거스름돈 " + change + "원이 반환됩니다.");
        }
        return change;
    }

    @Override
    public void showItems() {
        // 1. 4개 슬롯의 '그림'을 미리 준비합니다.
        String[] a1 = formatSlotForDisplay("A1");
        String[] a2 = formatSlotForDisplay("A2");
        String[] b1 = formatSlotForDisplay("B1");
        String[] b2 = formatSlotForDisplay("B2");

        // 2. 제목을 출력합니다.
        System.out.println("=========================================");
        System.out.println("        🏪 Java Vending Machine 🏪");
        System.out.println("=========================================");

        // 3. 첫 번째 줄 (A1, B1)을 그립니다.
        System.out.printf("   %s   %s\n", a1[0], b1[0]); // ┌───┐   ┌───┐
        System.out.printf("   %s   %s\n", a1[1], b1[1]); //  이름     이름
        System.out.printf("   %s   %s\n", a1[2], b1[2]); //  가격     가격
        System.out.printf("   %s   %s\n", a1[3], b1[3]); // └───┘   └───┘
        System.out.printf("        [ A1 ]             [ B1 ]\n");
        System.out.println(); // 한 줄 띄우기

        // 4. 두 번째 줄 (A2, B2)을 그립니다.
        System.out.printf("   %s   %s\n", a2[0], b2[0]);
        System.out.printf("   %s   %s\n", a2[1], b2[1]);
        System.out.printf("   %s   %s\n", a2[2], b2[2]);
        System.out.printf("   %s   %s\n", a2[3], b2[3]);
        System.out.printf("        [ A2 ]             [ B2 ]\n");

        // 5. 하단 UI를 그립니다.
        System.out.println("=========================================");
        System.out.println("[현재 투입 금액: " + this.currentBalance + "원]");
    }

    /**
     * [헬퍼 메서드] (최종 수정 버전)
     * 슬롯 ID를 받아서, 콘솔에 그릴 4줄짜리 ASCII 아트 문자열 배열을 반환합니다.
     * @param slotId (예: "A1")
     * @return 4줄로 구성된 문자열 배열 (그림)
     */
    private String[] formatSlotForDisplay(String slotId) {
        String[] display = new String[4];
        ItemSlot slot = inventory.get(slotId);

        final int INNER_WIDTH = 12; // 박스 내부의 총 시각적 너비 (테두리 제외)

        if (slot == null) {
            // 1. 슬롯이 비어있을 때
            display[0] = "┌────────────┐";
            display[1] = " " + padRight(" [ 비었음 ]", INNER_WIDTH) + " "; // │ 제거
            display[2] = " " + padRight("", INNER_WIDTH) + " ";          // │ 제거
            display[3] = "└────────────┘";
        } else if (slot.isOutOfStock()) {
            // 2. 품절일 때
            display[0] = "┌────────────┐";
            display[1] = " " + padRight(" " + slot.getName(), INNER_WIDTH) + " "; // │ 제거
            display[2] = " " + padRight(" (품절)", INNER_WIDTH) + " ";          // │ 제거
            display[3] = "└────────────┘";
        } else {
            // 3. 판매 중일 때
            display[0] = "┌────────────┐";
            
            String nameLine = " " + slot.getName();
            display[1] = " " + padRight(nameLine, INNER_WIDTH) + " "; // │ 제거
            
            String priceStr = String.format("(%,d원)", slot.getPrice());
            String priceLine = " " + priceStr;
            display[2] = " " + padRight(priceLine, INNER_WIDTH) + " "; // │ 제거
            
            display[3] = "└────────────┘";
        }
        return display;
    }

    /**
     * [헬퍼 메서드 1]
     * 문자열의 '시각적 너비'를 계산합니다. (한글/특수문자 = 2, 나머지 = 1)
     */
    private int getVisualWidth(String s) {
        int width = 0;
        for (char c : s.toCharArray()) {
            if (c >= '\uAC00' && c <= '\uD7A3' || c >= '\u2500' && c <= '\u257F') {
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }

    /**
     * [헬퍼 메서드 2]
     * 문자열을 '목표 시각적 너비'에 맞게 오른쪽(' ')으로 채워줍니다.
     */
    private String padRight(String s, int targetVisualWidth) {
        int currentWidth = getVisualWidth(s);
        if (currentWidth >= targetVisualWidth) {
            return s;
        }
        
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < (targetVisualWidth - currentWidth); i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}