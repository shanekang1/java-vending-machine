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

    ' --- 1. Main의 의존성 ---'
    Main ..> VendingMachineService : uses
    Main ..> VendingMachineImpl : creates
    Main ..> NotEnoughMoneyException : catches
    Main ..> OutOfStockException : catches

    ' --- 2. 인터페이스 구현 (요구조건 1) ---'
    VendingMachineImpl ..|> VendingMachineService : implements

    ' --- 3. 객체 구성 (자판기가 슬롯을 가짐 / Map) ---'
    VendingMachineImpl *-- "many" ItemSlot : has-a
    ItemSlot --> "1" Drink : has-a

    ' --- 4. 상속 (요구조건 2) ---'
    Drink <|-- Cola
    Drink <|-- Water
    Drink <|-- Coffee
    Drink <|-- Milk

    ' --- 5. 예외 상속 (요구조건 3) ---'
    Exception <|-- NotEnoughMoneyException
    Exception <|-- OutOfStockException

    ' --- 6. 예외 발생 (throws) ---'
    VendingMachineImpl ..> NotEnoughMoneyException : throws
    VendingMachineImpl ..> OutOfStockException : throws
