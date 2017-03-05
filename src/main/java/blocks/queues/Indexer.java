package blocks.queues;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class Indexer implements Runnable {

    private final BlockingQueue<File> fileQueue;

    Indexer(BlockingQueue<File> fileQueue) {
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                indexFile(fileQueue.take());
            }
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File file) {
        System.out.println("[" + Thread.currentThread().getId() + "]Indexing: " + file.getName());
    }

}
