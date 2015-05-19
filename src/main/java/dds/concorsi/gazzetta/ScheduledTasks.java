package dds.concorsi.gazzetta;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks
{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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

    @Scheduled(initialDelay=70000, fixedRate= 3600000)
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