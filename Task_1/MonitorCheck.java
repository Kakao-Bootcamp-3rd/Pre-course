import java.lang.management.*;

public class MonitorCheck {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(5000); // 락 오래 잡고 있게
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        Thread.sleep(1000); // t1이 락 잡을 시간 줌

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
ThreadInfo[] infos = bean.dumpAllThreads(true, true);
for (ThreadInfo info : infos) {
    System.out.println(info.getThreadName() + " / " + info.getThreadState());
    // 대기 중인 락(획득하려는/기다리는 락)
    if (info.getLockName() != null) {
        System.out.println("  waiting/blocked on: " + info.getLockName()
            + " (owner=" + info.getLockOwnerName() + ")");
    }
    // 소유 중인 모니터
    for (MonitorInfo mi : info.getLockedMonitors()) {
        System.out.println("  owns monitor: " + mi);
    }
    // 소유 중인 동기화 객체(ReentrantLock 등)
    for (LockInfo li : info.getLockedSynchronizers()) {
        System.out.println("  owns synchronizer: " + li);
    }
}

    }
}
