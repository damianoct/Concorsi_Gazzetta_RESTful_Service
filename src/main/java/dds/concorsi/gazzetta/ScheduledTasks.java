package dds.concorsi.gazzetta;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks implements Observer
{
    ExecutorService executor;
    GazzettaBrain gBrain;

    private ScheduledTasks()
    {
        this.executor = Executors.newCachedThreadPool();
        this.gBrain = new GazzettaBrain(new ScraperHtml(this));
    }

    @Scheduled(initialDelay=80000, fixedRate= 43200000) //after 80 secs to startup and every 12 hour.
    public void scheduledWork()
    {
        executor.submit(gBrain);
    }


    //only for Heroku
    /*
    @Scheduled(initialDelay = 2400000 ,fixedRate = 2000000) //after 40 minutes to startup and every 30 minutes.
    public void stayAlive()
    {
        try
        {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet request = new HttpGet("https://fierce-retreat-4259.herokuapp.com/gazzette");
            request.addHeader("content-type","application/json");
            CloseableHttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            httpClient.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }	
    */


    @Override
    public void update(Observable o, Object arg)
    {
        long numberOfGazzetteAdded = (long) arg;
        if(numberOfGazzetteAdded == 1)
        {
            System.out.println("Lanciato thread -> gazzetta singola del: " +
                                    GazzettaWrapper.getInstance().getGazzette()
                                        .get(0).getDateOfPublication());

            executor.submit(new ConcorsoMultiBrain(new ScraperHtml(),
                    GazzettaWrapper.getInstance().getGazzette()
                            .get(0)));
        }
        else if(numberOfGazzetteAdded == 0)
        {
            System.out.println("\n\n\tOut of date.\n\n\n");
        }
        else // first load
        {
            int tmp = 0;
            for(int j = 1; j <= GazzettaWrapper.getInstance().getGazzette().size(); j++)
            {
                if((j % 15) == 0)
                {
                    System.out.println("Lanciato thread -> subList Gazzette: ("+ tmp + "," + j + ").");
                    executor.submit(new ConcorsoMultiBrain(new ScraperHtml(),
                            GazzettaWrapper.getInstance().getGazzette()
                                    .subList(tmp, j).toArray(new GazzettaItem[0])));
                    tmp = j;
                }
            }
        }
    }
}
