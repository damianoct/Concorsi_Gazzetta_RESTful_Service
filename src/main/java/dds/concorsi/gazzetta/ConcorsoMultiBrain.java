package dds.concorsi.gazzetta;

import java.util.List;

/**
 * Created by damianodistefano on 20/05/15.
 */
public class ConcorsoMultiBrain implements Runnable
{

    private List<GazzettaItem> gazzetteToWork;

    public ConcorsoMultiBrain(List<GazzettaItem> gazzetteToWork, Scraper myScraper)
    {
        this.gazzetteToWork = gazzetteToWork;
        this.myScraper = myScraper;
    }


    @Override
    public void run()
    {
        for(GazzettaItem g: gazzetteToWork)
        {
            long startTime = System.nanoTime();
            myScraper.createConcorsiFromGazzetta(g);
            long endTime = System.nanoTime();

            long duration = (endTime - startTime) / 1000000;

            System.out.println("\nAggiunta concorsi per gazzetta "+ g.getPublishDate() + " finito.\n\t" +
                    "Tempo impiegato: " + duration + "ms.");
        }
    }


    private Scraper myScraper;

    public void setMyScraper(Scraper myScraper) {
        this.myScraper = myScraper;
    }





}
