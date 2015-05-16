package hello;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by damianodistefano on 10/05/15.
 */
public class ConcorsoStore
{

    private String link;
    private Document concorsiDocumenti;

    public ConcorsoStore()
    {
        this.link = "http://www.gazzettaufficiale.it/gazzetta/concorsi/caricaDettaglio/home?dataPubblicazioneGazzetta=2015-05-08&numeroGazzetta=35";
        try
        {
            this.concorsiDocumenti = Jsoup.connect(link).get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public List getConcorsi()
    {

        Elements emettitori = concorsiDocumenti.select(".emettitore");
        return emettitori;

    }

}
