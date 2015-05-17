package hello;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by damianodistefano on 10/05/15.
 */
public class ConcorsoBrain
{

    private final AtomicLong counter = new AtomicLong();

    public void concorsiToWrapper(GazzettaItem gazzettaItem)
    {
        ConcorsoWrapper.getInstance().addConcorsiList(createConcorsiFromGazzetta(gazzettaItem));
    }

    private List createConcorsiFromGazzetta(GazzettaItem gazzettaItem) {

        List<ConcorsoItem> concorsi = new LinkedList<ConcorsoItem>();
        Document concorsiDocument = null;
        String rubrica = null;

        try
        {
            concorsiDocument = Jsoup.connect(buildUrlForGazzetta(gazzettaItem)).get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (Element emett: concorsiDocument.getElementsByClass("emettitore"))
        {
            if(emett.previousElementSibling().hasClass("rubrica"))
            {
                rubrica = emett.previousElementSibling().text();
            }

            //entro dentro il concorso

            Element e1 = emett.nextElementSibling(); //sono dentro la classe "risultato"

            concorsi.add(new ConcorsoItem(counter.incrementAndGet(), gazzettaItem.getIdGazzetta(), rubrica,
                                            emett.text(),
                                            e1.getElementsByTag("a").get(0).text(),
                                            e1.getElementsByTag("a").get(1).text()));



            Element tmp = e1;

            while(tmp.nextElementSibling() != null && tmp.nextElementSibling().hasClass("risultato"))
            {
                //aggiungo un altro concorso
                tmp = tmp.nextElementSibling();
                concorsi.add(new ConcorsoItem(counter.incrementAndGet(), gazzettaItem.getIdGazzetta(), rubrica,
                                emett.text(),
                                tmp.getElementsByTag("a").get(0).text(),
                                tmp.getElementsByTag("a").get(1).text()));

            }

        }

        return concorsi;

    }

    private String buildUrlForGazzetta(GazzettaItem gazzettaItem)
    {
        return "http://www.gazzettaufficiale.it/gazzetta/concorsi/caricaDettaglio/home?dataPubblicazioneGazzetta="
                + gazzettaItem.getDateOfPublication().split("/")[2] + "-"
                + gazzettaItem.getDateOfPublication().split("/")[1] + "-"
                + gazzettaItem.getDateOfPublication().split("/")[0]
                + "&numeroGazzetta=" + gazzettaItem.getNumberOfPublication();
    }



}
