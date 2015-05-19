package dds.concorsi.gazzetta;

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
