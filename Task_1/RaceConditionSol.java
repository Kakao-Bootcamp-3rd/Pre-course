public class RaceConditionSol {
    private static int count = 0;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 10000; i++) {
                 synchronized (LOCK){
                     System.out.println(LOCK);
                     count++;
                }
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("최종 count 값: " + count); // 항상 2000
    }
}
