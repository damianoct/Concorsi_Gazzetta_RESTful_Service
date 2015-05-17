package hello;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by damianodistefano on 16/05/15.
 */
public class GazzettaBrain
{
    private String linkGazzetteList;
    private Document gazzettaDocument;

    private Calendar now = Calendar.getInstance();
    private final AtomicLong counter = new AtomicLong();



    public GazzettaBrain()
    {
        this.linkGazzetteList = "http://www.concorsi.it/gazzetta_ufficiale_concorsi_anno/"
                                + String.valueOf(now.get(Calendar.YEAR));
        try
        {
            this.gazzettaDocument = Jsoup.connect(linkGazzetteList).get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void gazzetteToWrapper()
    {
        Elements gazzetteElements = gazzettaDocument.select("a[href^=/gazzetta_ufficiale_concorsi_numero/]");
        GazzettaWrapper.getInstance().setGazzette(createGazzetteFromDocument(gazzetteElements));
    }

    private String returnIntegerForMonth(String month)
    {
        String[] monthNames = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};

        if(month == null)
            return null;

        else
        {
            for (int i = 0; i < monthNames.length; i++) {
                if (month.equals(monthNames[i]))
                    if(i < 10)
                        return "0"+ Integer.toString(i + 1);
                    else
                        return Integer.toString(i + 1);
            }

            return null;

        }
    }

    private List<GazzettaItem> createGazzetteFromDocument(Elements el)
    {
        List<GazzettaItem> gazzette = new LinkedList<GazzettaItem>();

        for (Element e : el)
        {
            String gazzetta = e.text();
            String[] splited = gazzetta.split("\\s+");
            String numero_gazzetta = null;
            String giorno_gazzetta = null;
            String mese_gazzetta = null;

            for (int i = (splited.length - 1); i >= 4; i--)
            {
                if(splited[i] != null)
                {
                    switch (i) {
                        case 6: {
                            giorno_gazzetta = splited[i];
                            giorno_gazzetta.replaceAll("\\s+", "");
                            break;
                        }
                        case 4: {
                            numero_gazzetta = splited[i];
                            numero_gazzetta.replaceAll("\\s+", "");
                            break;
                        }
                        case 7: {
                            mese_gazzetta = splited[i];
                            mese_gazzetta.replaceAll("\\s+", "");
                            break;
                        }
                        default:
                            break;
                    }
                }
                else
                {
                    break;
                }
            }

            if(gazzette.size() <= 60)
                gazzette.add(new GazzettaItem(counter.incrementAndGet(),numero_gazzetta,giorno_gazzetta
                                                            + "/"
                                                            + returnIntegerForMonth(mese_gazzetta)
                                                            + "/"
                                                            + String.valueOf(now.get(Calendar.YEAR))));
            else
                return gazzette;


        }

        return gazzette;

    }



}
