package blocks.queues;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;

public class FileCrawler implements Runnable {

    private final BlockingQueue<File> fileQueue;
    private final FileFilter fileFilter;
    private final File root;

    FileCrawler(BlockingQueue<File> fileQueue, final FileFilter fileFilter, File root) {
        this.fileQueue = fileQueue;
        this.root = root;
        this.fileFilter = pathname -> pathname.isDirectory() || fileFilter.accept(pathname);
    }

    @Override
    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(File file) throws InterruptedException {
        File[] entries = file.listFiles(fileFilter);
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    crawl(entry);
                } else if (!alreadyIndexed(entry)) {
                    System.out.println("[" + Thread.currentThread().getId() + "]Adding file: " + entry.getName() + " to queue.");
                    fileQueue.put(entry);
                }
            }
        }
    }

    private boolean alreadyIndexed(File file) {
        return false;
    }

}
