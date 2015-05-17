package hello;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController
{

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final GazzettaBrain gs = new GazzettaBrain();


    @RequestMapping("/concorsi")
    public List concorsi(@RequestParam(value = "name", defaultValue = "World") String name)
    {
        List greetingObjects = new LinkedList<Greeting>();
        greetingObjects.add(new Greeting(counter.incrementAndGet(), "Ereoto"));
        greetingObjects.add(new Greeting(counter.incrementAndGet(), String.format(template, name)));
        return greetingObjects;
    }


    @RequestMapping("/vola")
    public ConcorsoWrapper cocnorsi()
    {
        return ConcorsoWrapper.getInstance();

    }

    @RequestMapping("/gazzette")
    public GazzettaWrapper gazzette()
    {
        return GazzettaWrapper.getInstance();

    }
}