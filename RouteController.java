package application;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.crawler.Crawler;
import services.indexer.Indexer;
import services.query_processor.Query;

@RestController
public class RouteController {

    @RequestMapping(value = "/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response search(@RequestParam(value = "query", defaultValue = " ") String query,
                           @RequestParam(value = "k", defaultValue = "10") String k) {

        Query q = new Query(query, Application.i, Integer.parseInt(k));
        q.run();

        return q.getResults();
    }
}

