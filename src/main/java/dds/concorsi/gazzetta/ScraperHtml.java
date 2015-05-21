package dds.concorsi.gazzetta;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
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
            gazzettaDocument = Jsoup.connect("http://www.gazzettaufficiale.it/ricercaArchivioCompleto/concorsi/" + year).get();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (Element e : gazzettaDocument.getElementsByClass("elenco_gazzette"))
        {

            String dateOfPublication = e.text().split("\\s+")[3].split("-")[0]  //day
                                        + e.text().split("\\s+")[3].split("-")[1] //month
                                        + e.text().split("\\s+")[3].split("-")[2] //year
                                        .replaceAll("\\D+", ""); //remove all non-digit chars.

            String numberOfPublication = e.text().split("\\s+")[1].replaceAll("\\s+",""); // number of publication


            addGazzettaToList(numberOfPublication,dateOfPublication);


        }

        //check size of list after the adding phase
        if(GazzettaWrapper.getInstance().getGazzette().size() < Application.NUMBERS_OF_GAZZETTE_MAX)
        {
            String previousYear = String.valueOf(Integer.parseInt(year) - 1);
            createGazzetteFromDocument(previousYear);
        }


    }

    @Override
    public void createConcorsiFromGazzetta(GazzettaItem gazzettaItem)
    {


        Document concorsiDocument = null;
        String areaDiInteresse = null;

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

            if(emett.previousElementSibling().hasClass("areaDiInteresse"))
            {
                areaDiInteresse = emett.previousElementSibling().text();
            }


            Element e1 = emett.nextElementSibling(); //sono dentro la classe "risultato"

            addConcorsoToGazzettaWithRubricaAndEmettitoreFromElement(gazzettaItem,areaDiInteresse,emett.text(),e1);

             Element tmp = e1;

            while(tmp.nextElementSibling() != null && tmp.nextElementSibling().hasClass("risultato"))
            {
                //add another contest
                tmp = tmp.nextElementSibling();

                addConcorsoToGazzettaWithRubricaAndEmettitoreFromElement(gazzettaItem,areaDiInteresse,emett.text(),tmp);

            }

        }

    }

    private synchronized void addGazzettaToList(String numberOfPublication, String dateOfPublication)
    {
        //ho un nuova gazzetta da aggiungere

        //se una gazzetta per questa data esiste già ritorno.
        if (GazzettaWrapper.getInstance().getGazzettaByDate(dateOfPublication) != null)
            return;

        //vedo se la lista è piena

        if(GazzettaWrapper.getInstance().getGazzette().size() == Application.NUMBERS_OF_GAZZETTE_MAX)
        {
            //la lista è piena, quindi prima di aggiungere devo controllare che sia una gazzetta
            //nuova rispetto alle precedenti

            if(GazzettaWrapper.getInstance().gazzettaIsNewer(dateOfPublication))
            {
                //rimuovo l'ultima gazzetta (la più vecchia) e aggiungo
                GazzettaWrapper.getInstance().getGazzette()
                            .remove(GazzettaWrapper.getInstance().getGazzette().size() - 1);

                GazzettaWrapper.getInstance().getGazzette()
                            .add(new GazzettaItem(counter.incrementAndGet(),
                                    numberOfPublication,
                                    dateOfPublication));

                Collections.sort(GazzettaWrapper.getInstance().getGazzette(),
                        GazzettaWrapper.getInstance().getComparator());
            }

            else
            {
                return;
            }

        }

        else //la lista non è piena, posso aggiungere tranquillamente
        {
            GazzettaWrapper.getInstance().getGazzette()
                    .add(new GazzettaItem(counter.incrementAndGet(),
                                            numberOfPublication,
                                            dateOfPublication));

            Collections.sort(GazzettaWrapper.getInstance().getGazzette(),
                    GazzettaWrapper.getInstance().getComparator());
        }


    }

    private void addConcorsoToGazzettaWithRubricaAndEmettitoreFromElement(GazzettaItem gazzettaItem, String areaDiInteresse, String emettitore, Element e)
    {
        Document bandoDocument = null;

        long startTime = System.nanoTime();

        try {

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
                    .data("codiceRedazionale", getContestTitleAndContestReferenceCode(e.getElementsByTag("a").get(1).text())[1])
                    .timeout(40 * 1000)
                    .get();

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Elements articoli = bandoDocument.select("pre");
        List<String> articoliBando = new LinkedList<String>();

        for(int j = 1; j < articoli.size(); j++)

           articoliBando.add(articoli.get(j).text());


        gazzettaItem.getConcorsi().add(new ConcorsoItem(counter.incrementAndGet(), gazzettaItem.getIdGazzetta(), areaDiInteresse,
                emettitore,
                e.getElementsByTag("a").get(0).text(), //areaDiInteresse
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
