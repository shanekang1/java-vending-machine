# ☕ Java 객체지향 콘솔 자판기 미니 프로젝트

이 프로젝트는 Java의 핵심 OOP 원칙(상속, 인터페이스)과 예외 처리,
그리고 컬렉션 프레임워크(`Map`) 사용을 목적으로 진행되었습니다.

## 1. 실행 화면

프로그램을 실행했을 때의 기본 화면과 사용자가 상호작용하는 예시입니다.

## 2. 프로젝트 핵심 설계 (Class Diagram)

(이 다이어그램은 이 프로젝트가 어떻게 체계적으로 설계되었는지 보여줍니다.)

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
