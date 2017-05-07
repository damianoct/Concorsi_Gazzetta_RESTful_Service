package dds.concorsi.gazzetta;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by damianodistefano on 18/05/15.
 */
public class ScraperHtml extends Observable implements Scraper
{
    private Observer observer;
    private final AtomicLong counter = new AtomicLong();
    private final AtomicLong addGazzetteCounter = new AtomicLong();


    public ScraperHtml() {}

    public ScraperHtml(Observer o)
    {
        addObserver(o);
    }

    @Override
    public void createGazzetteFromDocument(String year)
    {
        String previousYear = String.valueOf(Integer.parseInt(year) - 1);

        Document gazzettaDocument = null;
        try
        {
            gazzettaDocument = Jsoup.connect("http://www.gazzettaufficiale.it/ricercaArchivioCompleto/concorsi/" + year).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!gazzettaDocument.location().contains(year))
        {
            createGazzetteFromDocument(previousYear);
        }
        else
        {
            for (Element e : gazzettaDocument.getElementsByClass("elenco_gazzette")) {

                String dateOfPublication = e.text().split("\\s+")[3].split("-")[0]  //day
                        + e.text().split("\\s+")[3].split("-")[1] //month
                        + e.text().split("\\s+")[3].split("-")[2] //year
                        .replaceAll("\\D+", ""); //remove all non-digit chars.

                String numberOfPublication = e.text().split("\\s+")[1].replaceAll("\\s+", ""); // number of publication


                addGazzettaToList(numberOfPublication, dateOfPublication);
            }

            //check size of list after the adding phase

            if (GazzettaWrapper.getInstance().getGazzette().size() < Application.NUMBERS_OF_GAZZETTE_MAX) {
                createGazzetteFromDocument(previousYear);
            }
            else // finish
            {
                setChanged();
                notifyObservers(addGazzetteCounter.getAndSet(0));
                deleteObserver(observer);
            }
        }

    }

    @Override
    public void createConcorsiFromGazzetta(GazzettaItem gazzettaItem)
    {

        if(!gazzettaItem.getConcorsi().isEmpty()) //already full
            return;

        Document concorsiDocument = null;
        String areaDiInteresse = null;

        try
        {
            concorsiDocument = Jsoup.connect(buildUrlForGazzetta(gazzettaItem)).timeout(40 * 1000).get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (Element emett: concorsiDocument.getElementsByClass("emettitore"))
        {

            if(emett.previousElementSibling().hasClass("rubrica"))
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

                addGazzetteCounter.incrementAndGet();

                Collections.sort(GazzettaWrapper.getInstance().getGazzette(),
                        GazzettaWrapper.getInstance().getComparator());
            }

            else

                return;

        }

        else //la lista non è piena, posso aggiungere tranquillamente
        {
            GazzettaWrapper.getInstance().getGazzette()
                    .add(new GazzettaItem(counter.incrementAndGet(),
                            numberOfPublication,
                            dateOfPublication));

	    addGazzetteCounter.incrementAndGet();

            Collections.sort(GazzettaWrapper.getInstance().getGazzette(),
                    GazzettaWrapper.getInstance().getComparator());
        }


    }

    public static ConcorsoItem downloadContest(String anno, String mese, String giorno, String codiceRedazionale)
    {
        Document bandoDocument = null;

        try {

            bandoDocument = Jsoup.connect("http://www.gazzettaufficiale.it/atto/stampa/concorsi/originario")
                    .data("annoVigenza", anno)
                    .data("meseVigenza", mese)
                    .data("giornoVigenza", giorno)
                    .data("creaHTML", "Visualizza")
                    .data("dataPubblicazioneGazzetta", anno
                            + "-"
                            + mese
                            + "-"
                            + giorno)
                    .data("codiceRedazionale", codiceRedazionale)
                    .timeout(40 * 1000)
                    .get();

        }
        catch(HttpStatusException hse)
        {
            System.out.println("Status Exception. Response Code: [" + hse.getStatusCode() + "]");
        }

        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        Elements articoli = bandoDocument.select("pre");
        List<String> articoliBando = new LinkedList<String>();

        for(int j = 1; j < articoli.size(); j++)

            articoliBando.add(articoli.get(j).text());

        return new ConcorsoItem(0, 0, "CEPPA",
                "CEPPA",
                "CEPPA", //areaDiInteresse
                codiceRedazionale, //contestTitle
                codiceRedazionale,
                articoliBando);
    }

    private void addConcorsoToGazzettaWithRubricaAndEmettitoreFromElement(GazzettaItem gazzettaItem, String areaDiInteresse, String emettitore, Element e)
    {
        Document bandoDocument = null;

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

    private static String[] getContestTitleAndContestReferenceCode(String s)
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
