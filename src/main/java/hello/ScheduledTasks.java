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

        GazzettaBrain gb = new GazzettaBrain();
        ConcorsoBrain cb = new ConcorsoBrain();

        //carico gazzetta nella lista di GazzettaWrapper
        gb.gazzetteToWrapper();

        //per ogni entry (gazzetta) del wrapper carico i concorsi

        for(GazzettaItem g: GazzettaWrapper.getInstance().getGazzette())
        {
            if(g.isValid())
                cb.concorsiToWrapper(g);
        }

        System.out.println("-> Finished.\n-> Numero concorsi aggiunti: "+ConcorsoWrapper.getInstance().getConcorsi().size());

    }

    //@Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println(" The time is now " + dateFormat.format(new Date()));
    }
}