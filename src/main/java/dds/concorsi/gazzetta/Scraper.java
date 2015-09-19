package dds.concorsi.gazzetta;

/**
 * Created by damianodistefano on 18/05/15.
 */
public interface Scraper
{

    void createGazzetteFromDocument(String year);


    /**
     * Aggiunge i concorsi per la gazzatta data.
     * @param gazzettaItem La Gazzetta di riferimento.
     */
    void createConcorsiFromGazzetta(GazzettaItem gazzettaItem);


}
