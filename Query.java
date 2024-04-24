package services.query_processor;

import application.Response;
import services.indexer.Indexer;
import util.ArrayIndexComparator;
import util.StopWords;
import util.Tuple;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Query {
    ArrayList<String> allwords = new ArrayList<String>();
    Indexer indexer;
    StopWords s = new StopWords("stopWords.txt");
    double[] documentsWeight;
    double accumulator[];
    int indexForAccumulator[];
    int k;

    public Query(String query, Indexer indexer, int k) {
        this.k = k;
        this.indexForAccumulator = new int[this.k];
        this.indexer = indexer;
        String str[] = query.split(" ");
        for (String word : str) {
            if (!s.getStopWordsSet().contains(word)) {
                allwords.add(word);
            }
        }

        documentsWeight = new double[indexer.getSources().size()];
        System.out.println(indexer.getSources().size());

        for (String s : indexer.getDictionary().keySet()) {
            for (Tuple d : indexer.getDictionary().get(s)) {
                this.documentsWeight[d.getDocumentId()] += Math.pow((1 + Math.log(d.getFtd())), 2);
            }
        }
    }

    public void run() {
        int N = indexer.getSources().size();
        accumulator = new double[N];

        for (String s : this.allwords) {
            //Query term frequency in query
            double ftq = Collections.frequency(allwords, s);
            double tftq = 1 + Math.log(ftq);
            if (indexer.getDictionary().get(s) != null) {
                double idft = Math.log(1 + N / indexer.getDictionary().get(s).size());
                double wtq = idft * tftq;
                for (Tuple d : indexer.getDictionary().get(s)) {
                    double tftd = 1 + Math.log(d.getFtd());
                    double wtd = tftd;
                    accumulator[d.getDocumentId()] += wtq * wtd;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            accumulator[i] /= Math.sqrt(documentsWeight[i]);
        }
    }

    public Response getResults() {

        ArrayIndexComparator comparator = new ArrayIndexComparator(this.accumulator);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        Response result = new Response(this.k);

        for (int i = 0; i < this.k; i++) {
            result.addWebsite(indexer.getSources().get(indexes[indexes.length - 1 - i]));
        }
        return result;
    }

}
