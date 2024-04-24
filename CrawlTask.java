package services.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.HtmlDocument;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class CrawlTask implements Callable<HtmlDocument> {

    private HashSet<String> outgoingLinks;
    private String URL;
    private int docId;
    private int counter;

    public CrawlTask(String URL, int docId) {
        this.docId = docId;
        this.URL = URL;
        this.outgoingLinks = new HashSet<String>();
        this.counter = counter;
    }

    @Override
    public HtmlDocument call() throws Exception {

        HtmlDocument document = null;

        try {
            //1. Fetch the HTML code
            Document doc = Jsoup.connect(URL).get();

            //2. Get the html text and create an object HtmlDocument
            document = new HtmlDocument(this.docId, this.URL, doc.text());
            //3. Parse the HTML to extract links to other URLs
            Elements linksOnPage = doc.select("a[href]");
            
            for (Element page : linksOnPage) {
                String link = page.attr("abs:href");
                if (!(link.endsWith(".pdf") || link.endsWith(".zip") || link.endsWith(".png"))) {
                    this.outgoingLinks.add(page.attr("abs:href"));
                }
            }
            document.setOutgoingLinks(this.outgoingLinks);

        } catch (IOException e) {
            System.err.println("Error on page : '" + URL + "': \n");
        }

        document.generateHashSet();
        return document;
    }
}
