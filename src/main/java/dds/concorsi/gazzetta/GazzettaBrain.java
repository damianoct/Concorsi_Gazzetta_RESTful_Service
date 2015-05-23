package dds.concorsi.gazzetta;

import java.util.Calendar;
import java.util.concurrent.Callable;

/**
 * Created by damianodistefano on 16/05/15.
 */
public class GazzettaBrain implements Callable<Integer>
{

    Scraper myScraper;

    public GazzettaBrain(Scraper myScraper)
    {
        this.myScraper = myScraper;
    }

    public void setMyScraper(Scraper myScraper)
    {
        this.myScraper = myScraper;
    }


    @Override
    public Integer call() throws Exception
    {
        myScraper.createGazzetteFromDocument(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        cleanGazzette();
        return GazzettaWrapper.getInstance().getGazzette().size();

    }

    public void cleanGazzette()
    {
        for(int j = 0; j < GazzettaWrapper.getInstance().getGazzette().size(); j++)
        {
            if (!GazzettaWrapper.getInstance().getGazzette().get(j).isValid())
            {
                GazzettaWrapper.getInstance().getGazzette().remove(j);
            }
        }
    }
}
