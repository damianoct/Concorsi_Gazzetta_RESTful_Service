package hello;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by damianodistefano on 10/05/15.
 */
public class ConcorsoBrain
{

    private final AtomicLong counter = new AtomicLong();
    private Scraper myScraper;

    public ConcorsoBrain(Scraper myScraper) {
        this.myScraper = myScraper;
    }

    public void concorsiToWrapper(GazzettaItem gazzettaItem) {
        //gazzettaItem.setConcorsi(createConcorsiFromGazzetta(gazzettaItem));
        //gazzettaItem.setConcorsi(myScraper.createConcorsiFromGazzetta(gazzettaItem));
        //ConcorsoWrapper.getInstance().addConcorsiList(gazzettaItem.getConcorsi());

        long startTime = System.nanoTime();
        myScraper.createConcorsiFromGazzetta(gazzettaItem);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;

        System.out.println("\nAggiunta concorsi per gazzetta finito.\n\t" +
                                    "Tempo impiegato: " + duration + "ms.");


    }

    public void setMyScraper(Scraper myScraper) {
        this.myScraper = myScraper;
    }

}