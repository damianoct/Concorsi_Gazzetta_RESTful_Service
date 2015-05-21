package dds.concorsi.gazzetta;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * First try multithread support.
 */


@Component
public class ScheduledTasks
{

    GazzettaBrain gb;

    // to stay alive

    private ScheduledTasks()
    {
        this.gb = new GazzettaBrain(new ScraperHtml());
    }

    @Scheduled(initialDelay=80000, fixedRate= 43200000) //after 80 secs to startup and every 12 hour.
    public void scheduledWork() {

        long startTime = System.nanoTime();

        getData();

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;

        System.out.println("\nAggiunta concorsi per 60 gazzette finito.\n\t" +
                "Tempo impiegato: " + duration + "ms.");

    }

    public void getData()
    {
        gb.gazzetteToWrapper();
        cleanGazzette();

        int numeroConcorsi = 0;


        new Thread(new ConcorsoMultiBrain(GazzettaWrapper.getInstance()
                                                            .getGazzette()
                                                                .subList(0, 14),
                                                                    new ScraperHtml()), "Thread 1")
                                                                        .start();
        new Thread(new ConcorsoMultiBrain(GazzettaWrapper.getInstance()
                                                            .getGazzette()
                                                                .subList(15, 29),
                                                                    new ScraperHtml()), "Thread 2").start();
        new Thread(new ConcorsoMultiBrain(GazzettaWrapper.getInstance()
                                                            .getGazzette()
                                                                .subList(29, 44),
                                                                    new ScraperHtml()), "Thread 3")
                                                                        .start();
        new Thread(new ConcorsoMultiBrain(GazzettaWrapper.getInstance()
                                                            .getGazzette()
                                                                .subList(44, GazzettaWrapper
                                                                        .getInstance()
                                                                        .getGazzette().size()),
                                                                                new ScraperHtml()), "Thread 4")
                                                                                    .start();


        for(GazzettaItem g: GazzettaWrapper.getInstance().getGazzette())
        {
            numeroConcorsi += g.getConcorsi().size();
        }

        System.out.println("-> Finished.\n" +
                "-> Numero Gazzette: " + GazzettaWrapper.getInstance().getGazzette().size() + "\n" +
                "-> Numero concorsi aggiunti: " + numeroConcorsi);
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