package hello;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by damianodistefano on 18/05/15.
 */
public class ScraperHtml implements Scraper
{

    private final AtomicLong counter = new AtomicLong();


    @Override
    public void createGazzetteFromDocument(String year)
    {

        Document gazzettaDocument = null;
        try
        {
            gazzettaDocument = Jsoup.connect("http://www.concorsi.it/gazzetta_ufficiale_concorsi_anno/" + year).get();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (Element e : gazzettaDocument.select("a[href^=/gazzetta_ufficiale_concorsi_numero/]"))
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

            if(GazzettaWrapper.getInstance().getGazzette().size() < 2
                    && !GazzettaWrapper.getInstance()
                                        .gazzettaExistsForDate(giorno_gazzetta, returnNumberForMonth(mese_gazzetta), year))
            {
                GazzettaWrapper.getInstance().getGazzette()
                        .add(new GazzettaItem(counter.incrementAndGet(), numero_gazzetta,
                                giorno_gazzetta
                                        + returnNumberForMonth(mese_gazzetta)
                                        + year));
            }


        }

        //check size of list after the adding phase
        if(GazzettaWrapper.getInstance().getGazzette().size() < 2)
        {
            String previousYear = String.valueOf(Integer.parseInt(year) - 1);
            createGazzetteFromDocument(previousYear);
        }

    }

    @Override
    public void createConcorsiFromGazzetta(GazzettaItem gazzettaItem)
    {
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


            Element e1 = emett.nextElementSibling(); //sono dentro la classe "risultato"


            addConcorsoToGazzettaWithRubricaAndEmettitoreFromElement(gazzettaItem,rubrica,emett.text(),e1);

            Element tmp = e1;

            while(tmp.nextElementSibling() != null && tmp.nextElementSibling().hasClass("risultato"))
            {
                //add another contest
                tmp = tmp.nextElementSibling();
                addConcorsoToGazzettaWithRubricaAndEmettitoreFromElement(gazzettaItem,rubrica,emett.text(),tmp);

            }

        }
    }

    private void addConcorsoToGazzettaWithRubricaAndEmettitoreFromElement(GazzettaItem gazzettaItem, String rubrica, String emettitore, Element e)
    {
        Document bandoDocument = null;

        try
        {

            bandoDocument = Jsoup.connect("http://www.gazzettaufficiale.it/atto/stampa/concorsi/originario")
                    .data("annoVigenza", String.valueOf(gazzettaItem.getPublicationYear()))
                    .data("meseVigenza", String.valueOf(gazzettaItem.getPublicationMonth()))
                    .data("giornoVigenza", String.valueOf(gazzettaItem.getPublicationDay()))
                    .data("creaHTML", "Visualizza")
                    .data("dataPubblicazioneGazzetta", gazzettaItem.getPublicationYear()
                            + "-"
                            + gazzettaItem.getPublicationMonth()
                            + "-"
                            + gazzettaItem.getPublicationDay())
                    .data("codiceRedazionale", getContestTitleAndContestReferenceCode(e.getElementsByTag("a").get(1).text())[1]).get();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Elements articoli = bandoDocument.select("pre");
        List<String> articoliBando = new LinkedList<String>();

        for(int j = 1; j < articoli.size(); j++)

            articoliBando.add(articoli.get(j).text());

        gazzettaItem.getConcorsi().add(new ConcorsoItem(counter.incrementAndGet(), gazzettaItem.getIdGazzetta(), rubrica,
                emettitore,
                e.getElementsByTag("a").get(0).text(), //rubrica
                getContestTitleAndContestReferenceCode(e.getElementsByTag("a").get(1).text())[0], //contestTitle
                getContestTitleAndContestReferenceCode(e.getElementsByTag("a").get(1).text())[1], //referenceCode
                articoliBando));

    }

    private String returnNumberForMonth(String month)
    {
        String[] monthNames = {
                                "Gennaio",
                                "Febbraio",
                                "Marzo",
                                "Aprile",
                                "Maggio",
                                "Giugno",
                                "Luglio",
                                "Agosto",
                                "Settembre",
                                "Ottobre",
                                "Novembre",
                                "Dicembre"
                                            };

        if(month == null)

            return null;

        else
        {
            for (int i = 0; i < monthNames.length; i++)
            {

                if (month.equalsIgnoreCase(monthNames[i]))

                    return (i < 10) ? "0"+ Integer.toString(i + 1) : Integer.toString(i + 1);

            }

            return null;

        }
    }

    private String[] getContestTitleAndContestReferenceCode(String s)
    {

        String contestReferenceCode = s.split("\\s+")[(s.split("\\s+").length) - 3]
                .replace("[(]", "")
                .replaceAll("[()]", "");

        String contestTitle = s.replace(contestReferenceCode,"")
                .split("(?<=\\(\\)).*")[0]
                .replaceAll("[()]", "");

        return new String[] {contestTitle, contestReferenceCode};
    }

    private String buildUrlForGazzetta(GazzettaItem gazzettaItem)
    {

        return "http://www.gazzettaufficiale.it/gazzetta/concorsi/caricaDettaglio/home?dataPubblicazioneGazzetta="
                + gazzettaItem.getPublicationYear() + "-" //year
                + gazzettaItem.getPublicationMonth() + "-" //month
                + gazzettaItem.getPublicationDay()       //day
                + "&numeroGazzetta=" + gazzettaItem.getNumberOfPublication(); //numberOfPublication

    }

}
