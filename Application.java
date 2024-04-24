package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import services.crawler.Crawler;
import services.indexer.Indexer;

@SpringBootApplication
public class Application {

    static Crawler c;
    static Indexer i;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        String startPage = args[0];
        int noOfPages = Integer.parseInt(args[1]);
        int noOfThreads = Integer.parseInt(args[2]);
        Boolean useOldData = Boolean.parseBoolean(args[3]);
        Boolean noCrawling = Boolean.parseBoolean(args[4]);

        System.out.println(startPage + " " + noOfPages + " " + noOfThreads + " " + useOldData + " " + noCrawling);

        int startingDocId = 0;
        i = new Indexer();

        if (useOldData) {
            i.loadIndex("dictionary.txt", "sources.txt");
            startingDocId = i.getMaxSourceId();
            System.out.println("Data Loaded");
        }

        if (!noCrawling) {
            try {
                c = new Crawler(startPage, noOfPages, startingDocId, noOfThreads);
                System.out.println("Crawler created");
                if (i.getSources().contains(startPage)) {
                    System.out.println("Page already visited");
                } else {
                    c.run();
                    System.out.println("Crawler Finished");
                    i.run(c);
                    i.saveToFile("dictionary.txt", "sources.txt");
                    System.out.println("Saved to file");
                }
            } catch (Exception e) {
                System.out.println("Oops something went wrong in route controller");
                e.printStackTrace();
            }
        }
    }
}

