import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

public class DeadlockExampleFixed2 {
    private static final ReentrantLock LOCK1 = new ReentrantLock();
    private static final ReentrantLock LOCK2 = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> doWork("Thread 1", LOCK1, LOCK2));
        Thread t2 = new Thread(() -> doWork("Thread 2", LOCK2, LOCK1));
        t1.start();
        t2.start();
    }

    private static void doWork(String name, ReentrantLock first, ReentrantLock second) {
        while (true) { // 재시도 루프
            boolean gotFirst = false;
            boolean gotSecond = false;
            try {
                gotFirst = first.tryLock(200, TimeUnit.MILLISECONDS);
                if (!gotFirst) {
                    backoff(name, "첫 번째 락 획득 실패");
                    continue;
                }
                System.out.println(name + ": 첫 번째 락 획득");

                // 작업 일부 (예시)
                Thread.sleep(100);

                gotSecond = second.tryLock(200, TimeUnit.MILLISECONDS);
                if (!gotSecond) {
                    System.out.println(name + ": 두 번째 락 대기 시간 초과, 롤백");
                    // 두 번째 못 얻었으니 첫 번째 반납 후 재시도
                    continue;
                }
                System.out.println(name + ": 두 번째 락 획득");

                // 두 락 모두 가진 상태에서의 임계구역
                System.out.println(name + ": 임계구역 실행");
                Thread.sleep(100);
                return; // 작업 완료
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                if (gotSecond) second.unlock();
                if (gotFirst) first.unlock();
            }
        }
    }

    private static void backoff(String name, String reason) {
        int sleep = ThreadLocalRandom.current().nextInt(50, 151);
        System.out.println(name + ": " + reason + " → 백오프 " + sleep + "ms");
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
