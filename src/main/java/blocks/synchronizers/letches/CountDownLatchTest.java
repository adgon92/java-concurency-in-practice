package blocks.synchronizers.letches;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {


    private long timeTask(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startingGate = new CountDownLatch(1);
        final CountDownLatch endingGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(String.valueOf(i)) {
                public void run() {
                    try {
                        System.out.println("Awaiting: " + this.getName());
                        startingGate.await();
                        try {
                            task.run();
                        } finally {
                            System.out.println("By the end gate: " + this.getName());
                            endingGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        startingGate.countDown();
        endingGate.await();
        return System.nanoTime() - start;

    }


    public static void main(String[] args) throws InterruptedException {

        Runnable task = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        };

        CountDownLatchTest test = new CountDownLatchTest();
        System.out.println(test.timeTask(100, task));

    }

}
