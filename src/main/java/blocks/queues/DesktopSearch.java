package blocks.queues;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DesktopSearch {

    private static final int BOUND = 10;
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    private void startIndexing(File[] fileRoots) {

        BlockingQueue<File> queue = new LinkedBlockingQueue<>(BOUND);
        FileFilter filter = pathname -> true;

        for (File root: fileRoots) {
            new Thread(new FileCrawler(queue, filter, root)).start();
        }

        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }

    }

    public static void main(String[] args) {

        final Path FILE_ROOT = Paths.get(System.getProperty("user.home"), "gitrepos");

        DesktopSearch search = new DesktopSearch();
        File[] roots = new File[]{FILE_ROOT.toFile()};
        search.startIndexing(roots);

    }

}
