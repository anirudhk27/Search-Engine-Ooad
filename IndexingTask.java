package services.indexer;

import services.crawler.Crawler;
import util.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IndexingTask implements Runnable {

    private ConcurrentHashMap<String, Vector<Tuple>> dictionary;
    private HtmlDocument document;

    public IndexingTask(ConcurrentHashMap<String, Vector<Tuple>> dictionary,
                        ConcurrentHashMap<Integer, String> sources, HtmlDocument document) {
        this.dictionary = dictionary;
        this.document = document;
        sources.putIfAbsent(document.getDocId(), document.getURL());
    }

    @Override
    public void run() {
        for (String word : document.getTerms()) {
            this.dictionary.computeIfAbsent(word, k -> new Vector<Tuple>())
                    .add(new Tuple(document.getDocId(), document.getWordFrequency(word)));
            Crawler.RUNNING_TASKS.decrementAndGet();
        }
    }
}
