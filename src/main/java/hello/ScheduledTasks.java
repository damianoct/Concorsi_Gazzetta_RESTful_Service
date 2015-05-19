package hello;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks
{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private ScheduledTasks()
    {

        GazzettaBrain gb = new GazzettaBrain(new ScraperHtml());
        ConcorsoBrain cb = new ConcorsoBrain(new ScraperHtml());

        //carico gazzette nella lista di GazzettaWrapper
        gb.gazzetteToWrapper();

        int numeroConcorsi = 0;

        for(int j = 0; j < GazzettaWrapper.getInstance().getGazzette().size(); j++)
        {
            if(GazzettaWrapper.getInstance().getGazzette().get(j).isValid())
            {
                cb.concorsiToWrapper(GazzettaWrapper.getInstance().getGazzette().get(j));
            }
            //if((j % 1) == 0)
            System.out.println("\n[-] Completed: "+ (100 * j) / GazzettaWrapper.getInstance().getGazzette().size() + "%\n");
        }

        for(GazzettaItem g: GazzettaWrapper.getInstance().getGazzette())
        {
            numeroConcorsi += g.getConcorsi().size();
        }

        System.out.println("-> Finished.\n" +
                            "-> Numero Gazzette: "+ GazzettaWrapper.getInstance().getGazzette().size() +"\n" +
                            "-> Numero concorsi aggiunti: "+numeroConcorsi);

    }

    //@Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println(" The time is now " + dateFormat.format(new Date()));
    }
}