package services.crawler;

import util.HtmlDocument;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler {

    public static final AtomicInteger RUNNING_TASKS = new AtomicInteger();

    private int crawlPages;
    private String startPage;
    private int threadsNo;

    private ExecutorService CRAWL_SERVICE;

    private BlockingQueue<Callable<HtmlDocument>> pendingPages;
    private BlockingQueue<HtmlDocument> finishedPages;

    private HashSet<String> visitedLinks;

    private int docId;
    private boolean crawlCompleted = false;

    public Crawler(String startPage, int crawlPages, int startingDocId, int threadsNo) throws ExecutionException, InterruptedException {

        this.startPage = startPage;
        this.crawlPages = crawlPages;
        this.threadsNo = threadsNo;
        this.docId = startingDocId;

        initializeCrawler();

    }

    private void initializeCrawler() {

        //Create thread pool for crawling and fetching document data.
        this.CRAWL_SERVICE = Executors.newFixedThreadPool(this.threadsNo);

        //Crawling thread-safe queues
        this.pendingPages = new LinkedBlockingQueue<Callable<HtmlDocument>>();
        this.finishedPages = new LinkedBlockingQueue<HtmlDocument>();

        visitedLinks = new HashSet<String>();
    }

    public void run() throws ExecutionException, InterruptedException {
        this.pendingPages.add(new CrawlTask(this.startPage, 0));
        this.visitedLinks.add(this.startPage);

        int counter = 0;
        boolean exitFlag = false;

        do {

            List<Future<HtmlDocument>> futures = null;

            futures = CRAWL_SERVICE.invokeAll(pendingPages);

            pendingPages.clear();

            for (int i = 0; i < futures.size(); i++) {
                Future<HtmlDocument> future = futures.get(i);
                if (future.isDone()) {
                    try {
                        HtmlDocument result = future.get();
                        result.setDocId(docId++);
                        System.out.println("Document " + result.getDocId() + " finished");
                        visitedLinks.add(result.getURL());
                        finishedPages.put(result);
                    } catch (Exception e) {
                        System.out.println("Thread Error");
                    }
                }
            }

            if (exitFlag) {
                break;
            }

            for (int i = 0; i < futures.size(); i++) {

                Future<HtmlDocument> future = futures.get(i);

                if (future.isDone()) {
                    try {
                        HtmlDocument result = future.get();
                        for (String link : result.getOutgoingLinks()) {
                            if (!visitedLinks.contains(link)) {
                                pendingPages.put(new CrawlTask(link, 0));
                                counter++;

                                if (counter > this.crawlPages) {
                                    exitFlag = true;
                                    break;
                                }
                            }
                        }
                        if (exitFlag) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Thread error 2");
                    }
                }
            }
        } while (true);

        this.crawlCompleted = true;

        CRAWL_SERVICE.shutdown();
    }

    public BlockingQueue<HtmlDocument> getFinishedPages() {
        return finishedPages;
    }


}
