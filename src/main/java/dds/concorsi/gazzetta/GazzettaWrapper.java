package dds.concorsi.gazzetta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by damianodistefano on 17/05/15.
 */

/**
 * Support for LIFO queues
 */

public class GazzettaWrapper
{
    @JsonView(View.Summary.class)
    private List<GazzettaItem> gazzette;

    private static GazzettaWrapper instance;

    @JsonIgnore
    private DateFormat formatter;

    @JsonIgnore
    private GazzetteComparator comparator;

    private GazzettaWrapper()
    {
        this.gazzette = new LinkedList<GazzettaItem>();
        this.formatter = new SimpleDateFormat("ddMMyyyy");
        this.comparator = new GazzetteComparator();
    }

    public static GazzettaWrapper getInstance()
    {
        if(instance == null)
        {
            instance = new GazzettaWrapper();
        }
        return instance;
    }

    @JsonIgnore
    public boolean gazzettaIsNewer(String data)
    {
        if(gazzette.isEmpty())
            return true;

        try
        {
            Calendar c = Calendar.getInstance();
            c.setTime(formatter.parse(data));
            int year = c.get(Calendar.YEAR);

            return (getNewestGazzettaForYear(year).getPublishDate()
                                                    .compareTo(formatter.parse(data)) < 0) ? true : false;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return false;

        /*
        if(gazzette.isEmpty())
            return true;
        try {
            return (gazzette.get(0).getPublishDate().compareTo(formatter.parse(data)) < 0) ? true : false;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;*/

    }

    private GazzettaItem getNewestGazzettaForYear(int year)
    {
        List<GazzettaItem> gazzetteForYear = new LinkedList<GazzettaItem>();

        for(GazzettaItem g: gazzette)
            if(g.getPublicationYear() == year)
                gazzetteForYear.add(g);

        Collections.sort(gazzetteForYear,comparator);
        return gazzetteForYear.get(0);
    }

    public GazzettaItem getGazzettaByDate(String date)
    {
        for(GazzettaItem g: gazzette)
        {
            try {
                if (g.getPublishDate().equals(formatter.parse(date)))
                    return g;
            } catch (ParseException e) {
                return null;
            }
        }

        return null;
    }

    public List<GazzettaItem> getGazzette()
    {
        return gazzette;
    }

    @JsonIgnore
    public DateFormat getFormatter()
    {
        return formatter;
    }

    @JsonIgnore
    public GazzetteComparator getComparator()
    {
        return comparator;
    }

}