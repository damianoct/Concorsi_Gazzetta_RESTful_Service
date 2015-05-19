package dds.concorsi.gazzetta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.jsoup.nodes.Document;



@Component
public class ScheduledTasks
{


    private ScheduledTasks()
    {

        /*GazzettaBrain gb = new GazzettaBrain(new ScraperHtml());
        ConcorsoBrain cb = new ConcorsoBrain(new ScraperHtml());

        //carico gazzette nella lista di GazzettaWrapper
        gb.gazzetteToWrapper();


        // pulizia gazzette
        for(int j = 0; j < GazzettaWrapper.getInstance().getGazzette().size(); j++)
        {
            if (!GazzettaWrapper.getInstance().getGazzette().get(j).isValid())
            {
                GazzettaWrapper.getInstance().getGazzette().remove(j);
            }
        }


        System.out.println("Elenco Gazzette Presenti:\n");


        for(GazzettaItem g: GazzettaWrapper.getInstance().getGazzette())
        {
            System.out.println("\n\t"+ g.getPublishDate());
        }

        int numeroConcorsi = 0;

        for(int j = 0; j < GazzettaWrapper.getInstance().getGazzette().size(); j++)
        {
            if(GazzettaWrapper.getInstance().getGazzette().get(j).isValid())
            {
                cb.concorsiToWrapper(GazzettaWrapper.getInstance().getGazzette().get(j));
            }
            //gazzetta is not valid
            else
            {
                System.out.println("\nTrovata Gazzetta non valida ---> " + GazzettaWrapper.getInstance().getGazzette().get(j).getPublishDate());
                GazzettaWrapper.getInstance().getGazzette().remove(j);

            }
            System.out.println("\n[-] Completed: "+ (100 * j) / GazzettaWrapper.getInstance().getGazzette().size() + "%\n");
        }

        for(GazzettaItem g: GazzettaWrapper.getInstance().getGazzette())
        {
            numeroConcorsi += g.getConcorsi().size();
        }

        System.out.println("-> Finished.\n" +
                            "-> Numero Gazzette: "+ GazzettaWrapper.getInstance().getGazzette().size() +"\n" +
                            "-> Numero concorsi aggiunti: "+numeroConcorsi);

*/

    }

    /*
        TO STAY ALIVE
     */

    @Scheduled(initialDelay = 2400000 ,fixedRate = 2000000) //after 40 minutes to startup and every 30 minutes.
    public void stayAlive()
    {
        try {
        System.out.println("Stay alive!");
        URL wwww = new URL("https://fierce-retreat-4259.herokuapp.com/gazzette");
        BufferedReader br = new BufferedReader(new InputStreamReader(wwww.openStream()));
        br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay=80000, fixedRate= 43200000) //after 70 secs to startup and every 12 hour.
    public void reportCurrentTime() {
        GazzettaBrain gb = new GazzettaBrain(new ScraperHtml());
        ConcorsoBrain cb = new ConcorsoBrain(new ScraperHtml());

        //carico gazzette nella lista di GazzettaWrapper
        gb.gazzetteToWrapper();


        // pulizia gazzette
        for(int j = 0; j < GazzettaWrapper.getInstance().getGazzette().size(); j++)
        {
            if (!GazzettaWrapper.getInstance().getGazzette().get(j).isValid())
            {
                GazzettaWrapper.getInstance().getGazzette().remove(j);
            }
        }


        System.out.println("Elenco Gazzette Presenti:\n");


        for(GazzettaItem g: GazzettaWrapper.getInstance().getGazzette())
        {
            System.out.println("\n\t"+ g.getPublishDate());
        }

        int numeroConcorsi = 0;

        for(int j = 0; j < GazzettaWrapper.getInstance().getGazzette().size(); j++)
        {
            if(GazzettaWrapper.getInstance().getGazzette().get(j).isValid())
            {
                if(GazzettaWrapper.getInstance().getGazzette().get(j).getConcorsi().isEmpty())
                    cb.concorsiToWrapper(GazzettaWrapper.getInstance().getGazzette().get(j));
            }
            //gazzetta is not valid
            else
            {
                System.out.println("\nTrovata Gazzetta non valida ---> " + GazzettaWrapper.getInstance().getGazzette().get(j).getPublishDate());
                GazzettaWrapper.getInstance().getGazzette().remove(j);

            }
            System.out.println("\n[-] Completed: "+ (100 * j) / GazzettaWrapper.getInstance().getGazzette().size() + "%\n");
        }

        for(GazzettaItem g: GazzettaWrapper.getInstance().getGazzette())
        {
            numeroConcorsi += g.getConcorsi().size();
        }

        System.out.println("-> Finished.\n" +
                "-> Numero Gazzette: "+ GazzettaWrapper.getInstance().getGazzette().size() +"\n" +
                "-> Numero concorsi aggiunti: "+numeroConcorsi);    }
}