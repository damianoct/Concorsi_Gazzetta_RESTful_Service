package dds.concorsi.gazzetta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.Json;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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
    //@Scheduled(initialDelay = 5000 ,fixedRate = 5000)
    public void stayAlive()
    {
        try
        {
            System.out.println("Stay alive!");

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet request = new HttpGet("https://fierce-retreat-4259.herokuapp.com/gazzette");
            request.addHeader("content-type","application/json");
            CloseableHttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            System.out.println(json);
            httpClient.close();



            /*URL wwww = new URL("https://fierce-retreat-4259.herokuapp.com/gazzette");
            BufferedReader br = new BufferedReader(new InputStreamReader(wwww.openStream()));
            while(br.readLine() != null)
            {
                System.out.println(br.readLine());
            }

            br.close();*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay=80000, fixedRate= 43200000) //after 70 secs to startup and every 12 hour.
    //@Scheduled(fixedRate= 43200000)
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