package dds.concorsi.gazzetta;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController
{

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping("/concorsi")
    public List concorsi(@RequestParam(value = "name", defaultValue = "World") String name)
    {
        List greetingObjects = new LinkedList<Greeting>();
        greetingObjects.add(new Greeting(counter.incrementAndGet(), "Ereoto"));
        greetingObjects.add(new Greeting(counter.incrementAndGet(), String.format(template, name)));
        return greetingObjects;
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

    @RequestMapping("/vola")
    public ConcorsoWrapper cocnorsi()
    {
        return ConcorsoWrapper.getInstance();

    }

    @JsonView(View.Summary.class)
    @RequestMapping("/gazzette")
    public GazzettaWrapper gazzette()
    {
        return GazzettaWrapper.getInstance();

    }

    @JsonView(View.Summary1.class)
    @RequestMapping("/gazzetteWithDate")
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