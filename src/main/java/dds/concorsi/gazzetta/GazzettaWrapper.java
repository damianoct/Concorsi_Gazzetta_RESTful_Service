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

public class GazzettaWrapper
{
    @JsonView(View.GazzetteSummary.class)
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

    //wrapper instance for the latest gazzette requested.
    public GazzettaWrapper(List<GazzettaItem> gazzetteNewerThan)
    {
        this.gazzette = gazzetteNewerThan;
    }

    public static GazzettaWrapper getInstance()
    {
        if(instance == null)
        {
            instance = new GazzettaWrapper();
        }
        return instance;
    }

    public List<GazzettaItem> getGazzetteNewerThan(String date)
    {

        List<GazzettaItem> gazzetteList = new LinkedList<>();

        for(GazzettaItem g: gazzette) {
            try {
                if (g.getPublishDate().after(formatter.parse(date)))
                    gazzetteList.add(g);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return gazzetteList;
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