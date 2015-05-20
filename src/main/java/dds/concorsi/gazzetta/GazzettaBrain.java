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
        long startTime = System.nanoTime();
        myScraper.createGazzetteFromDocument("2015");
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;

        System.out.println("\nAggiunta gazzette finito.\n\t" +
                "Tempo impiegato: " + duration + "ms.");

    }

    public void setMyScraper(Scraper myScraper)
    {
        this.myScraper = myScraper;
    }





}
