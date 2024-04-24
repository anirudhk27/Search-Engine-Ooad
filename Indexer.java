package services.indexer;

import services.crawler.Crawler;
import util.HtmlDocument;
import util.Tuple;

import javax.swing.plaf.synth.SynthMenuBarUI;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Indexer {

    private final ExecutorService INDEXING_SERVICE;
    private ConcurrentHashMap<Integer, String> sources;
    private ConcurrentHashMap<Integer, String> sourcesTitles;
    private ConcurrentHashMap<String, Vector<Tuple>> dictionary;

    public Indexer() {
        this.dictionary = new ConcurrentHashMap<String, Vector<Tuple>>();
        this.sources = new ConcurrentHashMap<Integer, String>();
        this.INDEXING_SERVICE = Executors.newFixedThreadPool(8);
    }

    public void run(Crawler crawler) throws InterruptedException {
        while (!crawler.getFinishedPages().isEmpty()) {
            Crawler.RUNNING_TASKS.incrementAndGet();
            this.INDEXING_SERVICE.submit(new IndexingTask(this.dictionary, sources, crawler.getFinishedPages().take()));
        }
        while (Crawler.RUNNING_TASKS.get() > 0){
            Thread.sleep(10000);
        }

        this.INDEXING_SERVICE.shutdown();

    }

    public void saveToFile(String dictionaryPath, String sourcesPath) {

        File dictionaryFile = new File(dictionaryPath);
        File sourcesFile = new File(sourcesPath);
        BufferedWriter brDictionary = null;
        BufferedWriter brSources = null;

        try {
            brDictionary = new BufferedWriter(new FileWriter(dictionaryFile));
            brSources = new BufferedWriter(new FileWriter(sourcesFile));
            for (String word : this.dictionary.keySet()) {
                brDictionary.write(word + ";" + this.dictionary.get(word).toString() + "\n");
            }
            for (Integer id : this.sources.keySet()) {
                brSources.write(id + ";" + this.sources.get(id) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                brDictionary.close();
                brSources.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadIndex(String dictionaryPath, String sourcesPath) {
        File dictionaryFile = new File(dictionaryPath);
        File sourceFile = new File(sourcesPath);

        BufferedReader brDictionary = null;
        BufferedReader brSources = null;

        try {

            brDictionary = new BufferedReader(new FileReader(dictionaryFile));
            brSources = new BufferedReader(new FileReader(sourceFile));
            String st;
            int tempDocId = 0;
            while ((st = brSources.readLine()) != null) {
                String[] components = st.split(";");
                this.sources.put(Integer.parseInt(components[0]), components[1]);
            }

            while ((st = brDictionary.readLine()) != null) {
                String[] components = st.split(";");
                if (components[1].equals("[]")){
                    continue;
                }
                components[1] = components[1].replaceAll("^.", "");
                components[1] = components[1].replaceAll(".$", "");
                String[] pairs = components[1].split(", ");
                this.dictionary.put(components[0], new Vector<Tuple>());
                for (String pair : pairs) {
                    pair = pair.replaceAll("^.","");
                    pair = pair.replaceAll(".$","");
                    int docId = Integer.parseInt(pair.split(",")[0]);
                    int frequency =Integer.parseInt(pair.split(",")[1]);
                    Tuple entry = new Tuple(docId,frequency);
                    this.dictionary.get(components[0]).add(entry);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                brDictionary.close();
                brSources.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Load Finished");
    }

    @Override
    public String toString() {
        String result = "";
        for (String word : this.dictionary.keySet()) {
            result += word + "\t\t\t\t\t\t";
            for (Tuple combination : this.dictionary.get(word)) {
                result += combination.toString();
            }
            result += "\n";
        }
        return result;
    }

    public ConcurrentHashMap<Integer,String> getSources(){
//        HashSet<String> result = new HashSet<String>();
//        for(Integer source : this.sources.keySet()){
//            result.add(this.sources.get(source));
//        }
        return this.sources;
    }

    public int getMaxSourceId(){
        return  this.sources.keySet().size()-1;
    }

    public ConcurrentHashMap<String, Vector<Tuple>> getDictionary(){
        return this.dictionary;
    }
}
