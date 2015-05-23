package dds.concorsi.gazzetta;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by damianodistefano on 20/05/15.
 */
public class ConcorsoMultiBrain implements Callable<String>
{

    private List<GazzettaItem> gazzetteToWork;

    public ConcorsoMultiBrain(Scraper myScraper, GazzettaItem ...gazzettaItem)
    {
        this.gazzetteToWork = Arrays.asList(gazzettaItem);
        this.myScraper = myScraper;
    }


    private Scraper myScraper;

    public void setMyScraper(Scraper myScraper) {
        this.myScraper = myScraper;
    }


    @Override
    public String call() throws Exception
    {
        int numComputeContest = 0;

        long startTime = System.nanoTime();

        for(GazzettaItem g: gazzetteToWork)
        {
            myScraper.createConcorsiFromGazzetta(g);
            numComputeContest += g.getConcorsi().size();
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        return ("\t\t" + Thread.currentThread().getName() + " finished.\n"
                            + "\t\t\tConcorsi aggiunti: "+ numComputeContest + " in " + duration + " ms.");



    }
}
