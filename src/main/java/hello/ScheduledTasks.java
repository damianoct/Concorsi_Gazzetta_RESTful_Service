package hello;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks
{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final String chars;

    private ScheduledTasks()
    {
        System.out.println("\nRichiamo il costruttore...\n\n\n");
        this.chars = "Stringa costruttore";
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println(chars + " The time is now " + dateFormat.format(new Date()));
    }
}