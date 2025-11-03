# ☕ Java 객체지향 콘솔 자판기 미니 프로젝트

이 프로젝트는 Java의 핵심 OOP 원칙(상속, 인터페이스)과 예외 처리,
그리고 컬렉션 프레임워크(`Map`) 사용을 목적으로 진행되었습니다.

## 1. 실행 화면

프로그램을 실행했을 때의 기본 화면과 사용자가 상호작용하는 예시입니다.
![Animationstart](https://github.com/user-attachments/assets/c6eafd59-af12-4d8f-9491-ff3162b4d698)

![Animationstartbuycoffee](https://github.com/user-attachments/assets/c7b8590b-5851-4d83-b849-b4097ddc267a)



![Animationstartsold](https://github.com/user-attachments/assets/18d0686a-2552-48b2-af50-886052538d55)

## 2. 프로젝트 핵심 설계 (Class Diagram)


```mermaid
classDiagram

    class Main
    class Exception
    class VendingMachineService {
        <<Interface>>
        +insertMoney(int)
        +selectItem(String)
        +returnChange()
        +showItems()
    }
    class VendingMachineImpl {
        -inventory : Map~String, ItemSlot~
        -currentBalance : int
        +insertMoney(int)
        +selectItem(String)
        +returnChange()
        +showItems()
    }
    class Drink {
        <<Abstract>>
        -name : String
        -price : int
    }
    class ItemSlot {
        -drink : Drink
        -quantity : int
    }

    class Cola
    class Water
    class Coffee
    class Milk
    
    class NotEnoughMoneyException
    class OutOfStockException

    
    %% --- 1. Main dependency ---
    Main ..> VendingMachineService : uses
    Main ..> VendingMachineImpl : creates
    Main ..> NotEnoughMoneyException : catches
    Main ..> OutOfStockException : catches

    
    %% --- 2. Interface implementation ---
    VendingMachineImpl ..|> VendingMachineService : implements

    
    %% --- 3. Object composition (Map) ---
    VendingMachineImpl *-- "many" ItemSlot : has-a
    ItemSlot --> "1" Drink : has-a

    
    %% --- 4. Inheritance ---
    Drink <|-- Cola
    Drink <|-- Water
    Drink <|-- Coffee
    Drink <|-- Milk

    
    %% --- 5. Exception inheritance ---
    Exception <|-- NotEnoughMoneyException
    Exception <|-- OutOfStockException

    
    %% --- 6. Exception throwing ---
    VendingMachineImpl ..> NotEnoughMoneyException : throws
    VendingMachineImpl ..> OutOfStockException : throws
```

## 3 에러&오류 개선 처리과정

1-1 재고 관리 방식 (List => Map)

@ BEFORE 코드작성 (ArrayList)  
<VendingMachineImpl.java>   

<img width="384" height="26" alt="map수정 전1" src="https://github.com/user-attachments/assets/f0f45615-dd71-4613-b061-7d6f24ed5297" />  \
<img width="538" height="115" alt="map수정 전2" src="https://github.com/user-attachments/assets/fade5792-43cc-4a6c-bdb7-492c8d27a9f6" />  \
<img width="422" height="238" alt="map수정 전3" src="https://github.com/user-attachments/assets/f9682ddd-6a57-446b-b29b-629b4866ed3d" />\



                ▼    ▼    ▼    ▼

@ AFTER 코드작성 (Map)
<VendingMachineImpl.java>  현재 코드)

<img width="549" height="190" alt="map수정1" src="https://github.com/user-attachments/assets/0e7ee2d8-b0a3-4fab-8bf9-4656ac3347cd" />
<img width="539" height="161" alt="map수정2" src="https://github.com/user-attachments/assets/78bd5875-3c3d-4f49-9546-0cf10850ab85" />\


처음에는 ArrayList로 재고를 관리하려 했습니다.
하지만 'A1'을 구매할 때마다 for문으로 리스트 전체를 탐색하는 것이 매우 비효율적이라고 판단하여
'A1'이라는 Key로 재고(ItemSlot)라는 Value를 즉시 찾아낼 수 있는HashMap으로 자료구조를 리팩토링했습니다.


========================================================

1-2 오류 처리 방식 (if-else => Exception)

<VendingMachineImpl.java> 변경 전/후\
<img width="435" height="217" alt="오류처리방식 수정 전(main)2-3" src="https://github.com/user-attachments/assets/4be6b3dc-5a83-4ce9-a4d8-57b1c582d203" />
<img width="759" height="308" alt="오류처리방식 수정2-1" src="https://github.com/user-attachments/assets/aee2e268-dafe-4a1c-907e-83caf6cb0ce6" />

\
< Main.java> 변경 전/후 \
<img width="495" height="180" alt="오류처리방식 수정 전(main)2-4" src="https://github.com/user-attachments/assets/a74b8334-71e0-4ec1-b3d0-f37633952ab8" />\
<img width="468" height="191" alt="오류처리방식 수정(main)2-2" src="https://github.com/user-attachments/assets/ebcea3bf-f8d1-4603-891f-9f2311daa00b" />\





처음에는 구매 실패 시 false를 반환하도록 만들었습니다.
하지만 이 방식은 Main에서 false가 재고부족 때문인지,잔액부족 때문인지
구별할 수 없는 문제가 있었습니다.
이 문제를 해결하기 위해 NotEnoughMoneyException과 OutOfStockException라는
사용자 정의예외(Custom Exception)를 만들어 throw하도록 수정했습니다.

그 결과, Main에서는 try-catch를 통해 실패 원인을 명확하게 파악하고
사용자에게 정확한 안내 메시지를 보여줄 수 있도록 코드를 개선했습니다.
