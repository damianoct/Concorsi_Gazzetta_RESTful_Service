package dds.concorsi.gazzetta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class Application
{

    public static final int NUMBERS_OF_GAZZETTE_MAX = 2;

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
