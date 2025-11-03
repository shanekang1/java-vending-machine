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


## 3. 에러&오류 개선 처리과정

1-1 재고 관리 방식 (List vs Map)

@ BEFORE 코드작성 (ArrayList)
<VendingMachineImpl.java> 
private List<ItemSlot> inventory = new ArrayList<>();

public VendingMachineImpl() {
    // 슬롯 ID("A1")를 ItemSlot이 직접 갖게 됨
    inventory.add(new ItemSlot("A1", new Cola(), 5));
    inventory.add(new ItemSlot("A2", new Water(), 10));
}

@Override
public void selectItem(String slotId) {
    ItemSlot selectedSlot = null;
    for (ItemSlot slot : inventory) {
        if (slot.getSlotId().equals(slotId.toUpperCase())) {
            selectedSlot = slot;
            break;
        }
    } 
    if (selectedSlot == null) {
        System.out.println("[오류] 슬롯이 없습니다.");
        return;
    }

                ▼    ▼    ▼    ▼

@ AFTER 코드작성 (Map)
<VendingMachineImpl.java>  현재 코드)
private Map<String, ItemSlot> inventory = new HashMap<>();

public VendingMachineImpl() {
    // Key("A1")가 Value(ItemSlot)를 즉시 찾아줌
    inventory.put("A1", new ItemSlot(new Cola(), 5));
    inventory.put("A2", new ItemSlot(new Water(), 10));
}

@Override
public void selectItem(String slotId) {
    // [최적의 해결책!]
    // Map의 Key를 이용해 단 한 번의 연산으로 재고를 찾아냄 (O(1))
    ItemSlot selectedSlot = inventory.get(slotId.toUpperCase()); 
    
    if (selectedSlot == null) {
         System.out.println("[오류] '" + slotId + "'는 존재하지 않는 슬롯입니다.");
         return;
    }

처음에는 ArrayList로 재고를 관리하려 했습니다.
하지만 'A1'을 구매할 때마다 for문으로 리스트 전체를 탐색하는 것이 매우 비효율적이라고 판단하여
'A1'이라는 Key로 재고(ItemSlot)라는 Value를 즉시 찾아낼 수 있는HashMap으로 자료구조를 리팩토링했습니다.


========================================================

1-2

<VendingMachineImpl.java>
// selectItem이 boolean을 반환
public boolean selectItem(String slotId) {
    ItemSlot selectedSlot = inventory.get(slotId.toUpperCase());
    
    if (selectedSlot.isOutOfStock()) {
        // 재고가 없으면 false 반환
        return false; 
    }
    if (this.currentBalance < selectedSlot.getPrice()) {
        // 돈이 없어도 false 반환
        return false; 
    }
    return true;
}

< Main.java>
// ...
case 2:
    String slotId = scanner.next();
    boolean success = machine.selectItem(slotId); // boolean으로 받음
    
    if (success) {
        System.out.println("[구매 성공!]");
    } else {
        // [치명적 문제]
        // 왜 실패했는지 알 수 없음! (재고 부족? 돈 부족?)
        System.out.println("[오류] 구매에 실패했습니다.");
    }
    break;
// ...

                ▼    ▼    ▼    ▼


<VendingMachineImpl.java>  변경 후 코드
// 반환 타입이 void 이고, 대신 예외를 '던진다(throws)'
public void selectItem(String slotId) 
        throws NotEnoughMoneyException, OutOfStockException {
    
    ItemSlot selectedSlot = inventory.get(slotId.toUpperCase());
    // ...
    if (selectedSlot.isOutOfStock()) {
        // [해결책 1] 재고 부족 예외를 던진다
        throw new OutOfStockException("[재고 부족] ...");
    }
    if (this.currentBalance < selectedSlot.getPrice()) {
        // [해결책 2] 잔액 부족 예외를 던진다
        throw new NotEnoughMoneyException("[잔액 부족] ...");
    }
    // ...
}

<Main.java>
// ...
case 2:
    String slotId = scanner.next();
    try {
        machine.selectItem(slotId); // 일단 시도
    } catch (NotEnoughMoneyException | OutOfStockException e) {
        // 실패한 이유(e.getMessage())를 정확히 알 수 있음
        System.out.println(e.getMessage());
    }
    break;
// ...

처음에는 구매 실패 시 false를 반환하도록 만들었습니다.
하지만 이 방식은 Main에서 false가 재고부족 때문인지,잔액부족 때문인지
구별할 수 없는 문제가 있었습니다.
이 문제를 해결하기 위해 NotEnoughMoneyException과 OutOfStockException라는
사용자 정의예외(Custom Exception)를 만들어 throw하도록 수정했습니다.

그 결과, Main에서는 try-catch를 통해 실패 원인을 명확하게 파악하고
사용자에게 정확한 안내 메시지를 보여줄 수 있도록 코드를 개선했습니다.
