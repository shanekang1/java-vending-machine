// /src/com/vendingmachine/Main.java
package com.vendingmachine;

import com.vendingmachine.exception.NotEnoughMoneyException;
import com.vendingmachine.exception.OutOfStockException;
import com.vendingmachine.service.VendingMachineImpl;
import com.vendingmachine.service.VendingMachineService;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 프로그램을 시작하고 사용자의 입력을 받아
 * 자판기 기능을 호출(실행)하는 메인 클래스입니다.
 */
public class Main {

    public static void main(String[] args) {
        
        // 1. 자판기 인스턴스 생성 (우리가 만든 구현체 VendingMachineImpl 사용)
        VendingMachineService machine = new VendingMachineImpl();
        
        // 2. 사용자 입력을 받기 위한 Scanner 준비
        Scanner scanner = new Scanner(System.in);

        // 3. 메인 루프 (while(true)로 무한 반복)
        while (true) {
            try {
                // 현재 상태 표시 (메뉴판 출력)
                machine.showItems();
                
                System.out.println("1. 돈 넣기 | 2. 음료 선택 | 3. 거스름돈 반환 | 9. 종료");
                System.out.print("> 메뉴를 선택하세요: ");

                int choice = scanner.nextInt(); // 사용자가 숫자를 입력할 때까지 대기

                switch (choice) {
                    case 1:
                        System.out.print("> 투입할 금액을 입력하세요: ");
                        int amount = scanner.nextInt();
                        machine.insertMoney(amount);
                        break;
                    
                    case 2:
                        System.out.print("> 구매할 음료의 코드(예: A1)를 입력하세요: ");
                        String slotId = scanner.next(); // 텍스트 입력 받기
                        
                        // [핵심] 예외 처리 (try-catch)
                        try {
                            machine.selectItem(slotId);
                        } catch (NotEnoughMoneyException | OutOfStockException e) {
                            // 우리가 정의한 예외 발생 시, 사용자에게 친절하게 메시지 출력
                            System.out.println(e.getMessage());
                        }
                        break;
                        
                    case 3:
                        machine.returnChange();
                        break;
                        
                    case 9:
                        System.out.println("[시스템] 자판기를 종료합니다.");
                        machine.returnChange(); // 종료 전 거스름돈 반환
                        scanner.close();       // 스캐너 자원 해제
                        return; // 프로그램 완전 종료 (main 메서드 종료)
                        
                    default:
                        System.out.println("[오류] 1, 2, 3, 9번 중에 선택해주세요.");
                        break;
                }
                
            } catch (InputMismatchException e) {
                // [보너스 예외 처리]
                // 사용자가 1, 2, 3 대신 "안녕" 같은 문자를 입력했을 때
                // 프로그램이 죽는 것을 방지합니다.
                System.out.println("[오류] ❌ 잘못된 입력입니다. 숫자(1, 2, 3, 9)를 입력해주세요.");
                scanner.next(); // 버퍼에 남아있는 잘못된 입력을 비워줍니다.
            }
            
            System.out.println(); // 메뉴 반복 시 한 줄 띄워서 가독성 확보
        }
    }
}