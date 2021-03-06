##  프로세스란?

- 운영체제로부터 시스템 자원을 할당 받는 작업의 단위
- 하나의 프로세스는 Code, Data, Stack, Heap 영역으로 이루어져 있음

##  스레드란?

- 할당 받은 자원을 이용하는 실행의 단위 (한 프로세스내에 여러개의 쓰레드가 생길 수 있음)

![img](https://miro.medium.com/max/1400/0*39Jqwl1DcgCaTEGr.png)



## 멀티 프로세스란?

- 하나의 프로그램을 여러개의 프로세스로 구성하여 각 프로세스가 하나의 작업을 처리하도록 하는 것을 말함

## 멀티 쓰레드란?

- 하나의 프로그램을 여러개의 쓰레드로 구성하고, 각 스레드가 하나의 작업을 처리하도록 하는 것을 말함

|               |                             장점                             |                             단점                             |
| :-----------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| 멀티 프로세스 | 안정성이 좋다  -> 서로 자원을 공유하지 않아 프로세스 중 하나의 문제가 발생하도, 다른 프로세스에 영향을 주지 않는다. | 메모리 사용량이 많다.  -> 스케쥴링에 따른 *Context Switch가 많아져, 성능 저하의 우려가 있다. |
|  멀티 스레드  | 자원 공유(Code, Data, Heap)가 쉽다. -> 시스템의 자원 소모 감소 및 처리 비용 감소로 실행속도가 빠르다. | 자원을 공유하기에 스레드 하나가 잘 못될 경우, 모든 프로세서가 종료 될 수 있고, 또한 *동기화 문제 역시 발생할 수 있다. |



#### *context switching 이란? 

 -> 하나의 프로세스가 CPU를 사용 중인 상태에서 다른 프로세스가 CPU를 사용하도록 하기 위해, 이전의 프로세스 상태를 보관하고 새로운 프로세스 상태를 적재하는 작업을 말한다. 이 작업은 스케쥴러(Scheduler)에 의해 발생하는데, FCFS, SJF 등등 스케쥴링 알고리즘에 따라 처리방법이 나뉜다.

#### *동기화 문제란?

 -> 멀티 쓰레드를 사용하면 각 쓰레드의 실행 순서를 알수 없기에, 서로 다른 쓰레드에서 동일 자원을 사용할 경우, 해당 자원에 접근하지 못하거나 이미 다른 쓰레드에서 바꿔 버린 자원에 접근하게 되는 오류를 말한다. 이런 문제가 발생하지 않도록 쓰레드간 통신 시에는 충돌 문제를 피하기 위해 Thread Safe(쓰레드 세이프)해야 한다. 이를 해결하는 방법으로는 Mutex, Semaphores 등이 있다.