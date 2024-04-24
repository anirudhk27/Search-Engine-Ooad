package application;

import java.util.ArrayList;

public class Response {

    private ArrayList<String> results;

    public Response(int k){
        this.results = new ArrayList<String>(k);
    }

    public void addWebsite(String URL){
        this.results.add(URL);
    }

    public ArrayList<String> getResults() {
        return results;
    }
}
