import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

interface algorithm {
    int jobscheduler(int[] time, int m);
}

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
}

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
                if (job[j] < job[min])
                    min = j;
            }
            job[min] += time[i];
        }

        Arrays.sort(job);
        return job[m - 1];
    }
}

public class JobScheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("작업의 개수를 입력해주세요.");
        int n = sc.nextInt();    // 작업의 개수 [ 4, 8, 16 입력 예정 ]
        int[] time = new int[n];       // 각 작업의 수행 시간
        int m = 2;               // 기계의 개수 = 2개 !!

        System.out.println("각 작업의 수행 시간은 ");
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            time[i] = r.nextInt(10) + 1;
            System.out.printf(time[i] + " ");
        }
        System.out.println();
        System.out.println();

        BruteForce bruteForce = new BruteForce();
        int bruteforce_jobschedule = bruteForce.jobscheduler(time, m);

        System.out.println("[ Brute Force ]");
        System.out.println("작업이 가장 빠르게 종료된 시간은 " + bruteforce_jobschedule + "입니다.");
        System.out.println();

        Greedy greedy = new Greedy();
        int greedy_jobschedule = greedy.jobscheduler(time, m);

        System.out.println("[ Greedy ]");
        System.out.println("작업이 가장 빠르게 종료된 시간은 " + greedy_jobschedule + "입니다.");
    }
}