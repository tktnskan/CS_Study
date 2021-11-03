# GCD

GCD란 Grand Central Dispatch의 약자로 C 기반의 Low API 입니다.

생성된 Task들을 받게 되면, 이 Task들 효율적으로 각 쓰레드에 할당하여 안전하게 수행 및 처리시켜줍니다.



이러한 GCD는 큐(Queue)를 통해 이를 관리하는데, DispatchQueuedpsms 크게 세가지 타입이 존재합니다.



### Main Queue

메인 쓰레드에서 사용하는 큐로, serial의 특징을 가집니다. 

일반적으로 UI를 다룰 때, 사용합니다.





### Global Queue

글로벌 큐의 경우, Qos에 따라 6가지 타입의 큐로 나뉘어져 있고, 기본적으로는 Concurrent의 특징을 가집니다.



|                  Qos 종류                  | 큐 우선순위 |
| :----------------------------------------: | :---------: |
| DispatchQueue.global(qos: .userInterative) |      1      |
| DispatchQueue.global(qos: .userInitiated)  |      2      |
|       DispatchQueue.global() 디폴트        |      3      |
|    DispatchQueue.global(qos: .utility)     |      4      |
|   DispatchQueue.global(qos: .background)   |      5      |
|  DispatchQueue.global(qos: .unspecified)   |      6      |



		#### 	1. userInterative

​		- global queue의 우선순위 중에 가장 높으며, 유저와의 직접적인 상호작용이 있는 UI 관련 작업을 수행할 때 사용합니다.

 	#### 	2. userInitiated

​		- 유저와 상호작용이 있는 작업이지만, userInterative보다는 덜 중요하고 사용자가 대기 시간이 있음을 인지하는 작업에 주		  로 사용됩니다. 예를 들면 앱 내에서 파일을 여는 작업과 같은데 사용됩니다.

#### 	3. default

   	 - 우선 순위를 크게 신경쓰지 않는 일반적인 작업에 주로 사용됩니다. 

####     4. utility

​        - 통신과 같이 일반적으로 loading Indicator와 함께 실행되는 작업 시간이 좀 걸리는 작업에 사용됩니다. (초~분)

####     5. background

​        - 유저가 직접적으로 인식할 필요가 없는 서버 동기화 및 백업 같은 작업에 사용됩니다.

####     6. unspecified

​		- legacy API 지원 (보통 거의 안씀)





### Custom Queue

커스텀으로 만든 큐로, 디폴트는 Serial로 동작하나 Concurrent로 설정해주는 것도 가능합니다. 

Qos도 설정이 가능하나, 설정하지 않아도 OS가 Qos를 알아서 판단해줍니다.





## 주의사항

큐에서 sync를 사용할 때는 항상 주의를 해야하는데, 반드시 사용하면 안되는 경우를 살펴보겠습니다.

```swift
// 1. 메인큐에서 다른 큐로 보낼때

// 현재 메인큐
DispatchQueue.global().sync {
  // Task1
}
// 이 경우에는 메인에서 동작 중인 UI 반응이 Task1을 수행하는 동안 멈출 수 밖에 없기 때문에, 절대 이렇게 코드를 짜면 안됩니다 ㅜㅜ


// 2. 현재의 큐에서 현재의 큐로 작업을 보낼 때

// 현재 메인큐
DispatchQueue.main.sync {
  // Task2
}

// 현재 글로벌큐
DispatchQueue.global().sync {
  
}
// 보통 이렇게 코드를 짜면, Task2를 다시 현재 작업중인 큐에게 던져주기 때문에, 데드락이 발생하게 됩니다.
// 메인 큐는 메인쓰레드 하나만 가지고 있기에 코드 작성 시, 항상 에러가 발생합니다.
// 글로벌 큐의 경우, 다른 쓰레드에 할당해주는 경우가 있어 항상 데드락이 발생하지는 않지만 여전히 위험성을 가지고 있어
// 같은 큐에서 동기적 사용은 피해주어야 합니다!
```