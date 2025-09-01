
// 1. 락 순서 보장
public class DeadlockExampleFixed1 {
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK1) {
                System.out.println("Thread 1: LOCK1 획득");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                synchronized (LOCK2) {
                    System.out.println("Thread 1: LOCK2 획득");
                }
            }
        });

        Thread t2 = new Thread(() -> {           // 순서를 LOCK1 → LOCK2로 통일
            synchronized (LOCK1) {
                System.out.println("Thread 2: LOCK1 획득");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                synchronized (LOCK2) {
                    System.out.println("Thread 2: LOCK2 획득");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
