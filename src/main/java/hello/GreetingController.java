package hello;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController
{

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final GazzettaStore gs = new GazzettaStore();

    @RequestMapping("/concorsi")
    public List concorsi(@RequestParam(value = "name", defaultValue = "World") String name)
    {
        List greetingObjects = new LinkedList<Greeting>();
        greetingObjects.add(new Greeting(counter.incrementAndGet(), "Ereoto"));
        greetingObjects.add(new Greeting(counter.incrementAndGet(), String.format(template, name)));
        return greetingObjects;
    }

    @RequestMapping("/gazzette")
    public List gazzette()
    {
        List<String> listString = new LinkedList<String>();
        for(Element e: gs.getGazzetteOfLast30Days())
        {
            listString.add(e.text());
        }
        System.out.println("\n\n\n\nIl controller ha eseguito gazzette().\n\n\n\n\n");
        return listString;
    }
}