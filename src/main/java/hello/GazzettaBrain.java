package hello;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by damianodistefano on 16/05/15.
 */
public class GazzettaBrain
{

    Scraper myScraper;

    public GazzettaBrain(Scraper myScraper)
    {
        this.myScraper = myScraper;
    }

    public void gazzetteToWrapper()
    {
        myScraper.createGazzetteFromDocument("2015");
    }

    public void setMyScraper(Scraper myScraper)
    {
        this.myScraper = myScraper;
    }





}
