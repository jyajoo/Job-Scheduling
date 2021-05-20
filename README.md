# 작업 스케줄링 ( Job Scheduling )

n개의 작업, 각 작업의 수행 시간, m개의 동일한 기계가 주어질 때, 모든 작업이 가장 빠르게 종료되도록 직업을 기계에 배정하는 문제 

( 단, 한 작업은 배정된 기계에서 연속적으로 수행되어야 하며, 기계는 1번에 하나의 작업만을 수행한다. )

- 입력 : 작업의 수(n), 각 작업의 수행 시간, 기계의 수(m)
- 출력 : 모든 작업이 종료된 시간



```java
interface algorithm {
    int jobscheduler(int[] time, int m);
}
```



## 1. Brute Force

모든 경우의 수를 다 찾아보고, 가장 빠르게 종료되는 시간을 찾는다

### 코드 설명

```java
class BruteForce implements algorithm {

    static int[] machineNumber;
    static int[][] machine;
    static int cnt;

    @Override
    public int jobscheduler(int[] time, int m) {

        int n = time.length;
        int x = (int) Math.pow(m, n);  // m의 n 제곱만큼 경우의 수가 발생한다.
        machineNumber = new int[n];    // n이 2일 경우, (0,0) (0,1) (1,0) (1,1)이 된다.
        machine = new int[x][n];
        int[] job = new int[m];        // 각 기계에 작업의 수행 시간이 삽입된다.
        int[] endtime = new int[x];    // 총 작업의 종료 시간의 모든 경우의 수가 삽입된다.
        cnt = 0;
        machineCheck(n, m, 0);
        
        ...
```

- **n** : 작업의 개수
- **x** : m의 n 제곱으로, 종료 시간을 계산할 수 있는 총 경우의 수를 의미
- **machineNumber** : `machineCheck`을 통해 각 작업마다 선택될 기계를 의미한다.
- **machine** : `machineCheck` 속에서 machineNumber의 값이 변동되기 전에 값을 담아두기 위한 배열

- **job** : 각 기계에 얼만큼의 수행 시간이 걸리는 지를 확인하기 위한 배열
- **endtime** : 각 경우마다 걸리는 수행 시간이 삽입된다. 



##### 중복 순열

```java
    public void machineCheck(int n, int m, int count) {
        if (count == n) {
            for (int i = 0; i < n; i++) {
                machine[cnt][i] = machineNumber[i];   // machine에 순서대로 (0,0) (0,1) (1,0) (1,1)이 담긴다.
            }
            cnt++;
            return;
        }

        for (int i = 0; i < m; i++) {
            machineNumber[count] = i;
            machineCheck(n, m, count + 1);
        }
    }
```

- m개의 기계에서 중복을 허용하여 n개의 작업을 선택하여 준다.

> Ex) 작업의 개수(n) 이 2개이고, 기계의 개수(m) 이 2일 경우,   기계의 번호를 0, 1이라고 가정
>
> 재귀적으로 **machineNumber**에 (0, 0) (0, 1) (1, 0) (1, 1)이 순서대로 담기게 된다.
>
> count를 계산하며 작업의 개수(n)과 동일해지면, **machineNumber**에 담겨있는 것을 **machine**으로 옮겨 기록해준다.
>
> 그리고 다음을 또 반복해준다.



다시  `BruteForce class`로 돌아오며,

```java
        for (int i = 0; i < x; i++) {

            // 초기화
            for (int j = 0; j < m; j++) {
                job[j] = 0;
            }
            for (int t = 0; t < n; t++) {
                job[machine[i][t]] += time[t];
            }
            Arrays.sort(job);
            endtime[i] = job[m - 1];
        }

//        System.out.println("작업 종료 시간의 모든 경우의 수는 다음과 같습니다.");
//        for (int k = 0; k < x; k++) {
//            System.out.print(endtime[k] + " ");
//        }
//        System.out.println();

        Arrays.sort(endtime);
        return endtime[0];
    }
```

- 작업 수행 시간의 총 경우의 수(x)를 가지고, 각 경우의 수에서 걸리는 작업 수행 시간을 계산한다.
- 각 기계에 걸릴 시간을 계산한 후, 가장 오래 걸리는 시간이 그 작업의 종료 시간이 된다.
- 따라서, sort를 이용하여 오름차순 정렬해준 후, 마지막 인덱스의 값을 endtime에 삽입해준다.
- 모든 경우의 수를 따져 작업의 종료 시간들이 담긴 endtime에서 가장 적게 걸린 시간이 리턴된다.



## 2. Greedy

작업을 배정하고 가장 빠르게 끝나는 기계에 다음 작업을 배정하며, 각 기계에서 종료된 수행 시간 중 가장 큰 값을 찾는다.

### 코드 설명

```java
class Greedy implements algorithm {

    @Override
    public int jobscheduler(int[] time, int m) {

        int n = time.length;
        int x = (int) Math.pow(m, n);
        int[] job = new int[m];

        // 초기화
        for (int i = 0; i < m; i++) {
            job[i] = 0;
        }

        for (int i = 0; i < n; i++) {
            int min = 0;
            for (int j = 1; j < m; j++) {
                if(job[j] < job[min])
                    min = j;
            }
            job[min] += time[i];
        }

        Arrays.sort(job);
        return job[m - 1];
    }
}
```

- **job** : 각 기계에 작업이 수행되는 시간이 삽입된다. 
- 가장 빠르게 끝나는 기계에 차례대로 작업을 수행하도록 한다.
- 작업의 개수만큼 for문이 돌며 job에 수행 시간이 삽입되며, 가장 오래 걸린 시간이 총 작업의 종료 시간이 된다.
- 따라서 sort를 이용하여 오름차순 정렬을 하고, 마지막 인덱스 값을 리턴한다.



## 실행 결과

> < 입력 조건 >
>
> 작업의 개수 : n = [ 4, 8, 16 ] 
>
> 각 작업의 수행 시간 : 10초 이내의 랜덤값
>
> 기계의 개수 : m = 2

![image-20210520032631001](https://user-images.githubusercontent.com/73464584/118979312-4b631000-b9b3-11eb-962d-98574f0aaa35.png)



#### 근사 비율

근사해를 **OPT'** 라 하며 최적해를 **OPT**라 할 때, `OPT' <= 2 x OPT` 이다.

근사 비율은 근사해를 최적해로 나누 값으로 1과 가까울수록 정확성이 높아진다.

| 작업의 개수(n) | 2    | 3    | 4        | 5        | 8    | 16       |
| -------------- | ---- | ---- | -------- | -------- | ---- | -------- |
| 근사해         | 6    | 11   | 16       | 20       | 21   | 54       |
| 최적해         | 6    | 10   | 14       | 18       | 20   | 53       |
| 근사 비율      | 1    | 1.1  | 1.142857 | 1.111111 | 1.05 | 1.018868 |



### 시간 복잡도

#### **Brute Force**

모든 경우의 수 찾기 + 작업 종료 시간 탐색

`O(m^n) + O(m) = O(m^n)`

#### Greedy

n개의 작업 배정 + 작업 종료 시간 탐색 

`n x O(m) + O(m) = O(nm)`



Brute Force 알고리즘을 이용한 경우, 모든 경우의 수를 찾아보기 때문에 최적해를 찾을 수 있다.

그렇지만, 작업의 개수(n)이 많아진다면, 시간이 오래 걸리는 단점이 있다.

이를 해결하기 위해 Greedy 알고리즘을 사용하는 경우, 근사해를 찾을 수 있다.

근사 비율을 보면 대부분 1과 가까운 값들이 나오는 것으로 보아, 근사해가 최적해와 거의 동일하여 정확성이 높음을 알 수 있다.

