package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class HtmlDocument {

    private int docId;
    private String URL;
    private String docTitle;
    private String text;
    private HashSet<String> uniqueTerms;
    private ArrayList<String> allTerms;
    private HashSet<String> outgoingLinks;

    public HtmlDocument(int docId, String URL, String text) {
        this.docId = docId;
        this.URL = URL;
        this.text = text;
        this.docTitle = docTitle;
        this.uniqueTerms = new HashSet<String>();
        this.allTerms = new ArrayList<String>();
        this.outgoingLinks = new HashSet<String>();
    }

    public void generateHashSet() {

        for (String word : this.text.split("\\W+")) {
            this.uniqueTerms.add(word.toLowerCase());
            this.allTerms.add(word.toLowerCase());
        }
        stopWordFiltering(new StopWords("StopWords.txt"));
    }

    private void stopWordFiltering(StopWords stopwords) {
        this.uniqueTerms.removeAll(stopwords.getStopWordsSet());
    }

    public int getWordFrequency(String word) {
        return Collections.frequency(this.allTerms, word);
    }

    public HashSet<String> getTerms() {
        return this.uniqueTerms;
    }

    public int getDocId() {
        return this.docId;
    }

    public void setDocId(int docId){
        this.docId = docId;
    }

    public void setOutgoingLinks(HashSet<String> outgoingLinks) {
        this.outgoingLinks = outgoingLinks;
    }

    public HashSet<String> getOutgoingLinks() {
        return this.outgoingLinks;
    }

    public String getURL() {
        return this.URL;
    }

    public ArrayList<String> getAllTerms() {
        return allTerms;
    }
}