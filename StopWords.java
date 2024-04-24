package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class StopWords {

    private HashSet<String> stopWordsSet;

    public StopWords(String filePath) {

        this. stopWordsSet = new HashSet<String>(200);

        File file = new File(filePath);
        Scanner input = null;

        try {
            input = new Scanner(file);
            while (input.hasNext()) {
                String word = input.next();
                this.stopWordsSet.add(word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashSet<String> getStopWordsSet(){
        return this.stopWordsSet;
    }


}
