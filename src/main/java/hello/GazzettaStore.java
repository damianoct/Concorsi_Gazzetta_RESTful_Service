package hello;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by damianodistefano on 16/05/15.
 */
public class GazzettaStore
{
    private String link;
    private Document gazzettaDocument;

    public GazzettaStore()
    {
        this.link = "http://www.gazzettaufficiale.it/30giorni/concorsi";
        try
        {
            this.gazzettaDocument = Jsoup.connect(link).get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Elements getGazzetteOfLast30Days()
    {
        Elements gazzette = gazzettaDocument.select(".elenco_ugazzette");
        System.out.println(gazzette.size());
        return gazzette;
    }

}
