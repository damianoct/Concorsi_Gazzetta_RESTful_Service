package dds.concorsi.gazzetta;

import java.lang.management.ManagementFactory;

import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcorsiGazzettaController
{

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping("/uptime")
    public String getUptime()
    {
        long secondsToPrint, minutesToPrint, hoursToPrint;

        long uptimeInMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        long seconds = uptimeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        secondsToPrint = seconds;
        minutesToPrint = minutes;
        hoursToPrint = hours;
        return "Day: " + days + " Hour: " + hoursToPrint + " Minutes: " + minutesToPrint + " Seconds: " + secondsToPrint;
    }


    @RequestMapping("/concorso")
    public ConcorsoItem getConcorso(@RequestParam(value = "giorno") String giorno,
                                    @RequestParam(value = "mese") String mese,
                                    @RequestParam(value = "anno") String anno,
                                    @RequestParam(value = "codiceRedazionale") String referenceCode)
    {
        return GazzettaWrapper.getInstance()
                                .getGazzettaByDate(giorno+mese+anno)
                                .getConcorsoByReferenceCode(referenceCode);
    }

    @JsonView(View.GazzetteSummary.class)
    @RequestMapping("/gazzette")
    public GazzettaWrapper gazzette()
    {
        return GazzettaWrapper.getInstance();

    }

    @JsonView(View.GazzetteWithContests.class)
    @RequestMapping("/gazzetteWithContests")
    public GazzettaWrapper gazzetteWithDate()
    {
        return GazzettaWrapper.getInstance();
    }

    @RequestMapping("/gazzetteDettaglio")
    public GazzettaWrapper gazzetteDetailed()
    {
        return GazzettaWrapper.getInstance();

    }


}