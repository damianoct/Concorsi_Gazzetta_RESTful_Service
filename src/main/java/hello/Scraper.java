package hello;

import java.util.List;

/**
 * Created by damianodistefano on 18/05/15.
 */
public interface Scraper
{
    void createGazzetteFromDocument(String year);

    void createConcorsiFromGazzetta(GazzettaItem gazzettaItem);


}
