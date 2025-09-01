public class DeadlockExample {
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK1) {
                System.out.println("Thread 1: LOCK1 획득, \"자원 1 소지\"");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 1: LOCK2 대기, \"자원 2 필요\"");
                synchronized (LOCK2) {
                    System.out.println("Thread 1: LOCK2 획득");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (LOCK2) {
                System.out.println("Thread 2: LOCK2 획득, \"자원 2 소지\"");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 2: LOCK1 대기, \"자원 1 필요\"");
                synchronized (LOCK1) {
                    System.out.println("Thread 2: LOCK1 획득");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
