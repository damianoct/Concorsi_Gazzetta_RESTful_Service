package hello;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by damianodistefano on 17/05/15.
 */
public class GazzettaWrapper
{
    @JsonView(View.Summary.class)
    private List<GazzettaItem> gazzette;
    private static GazzettaWrapper instance;
    @JsonIgnore
    private DateFormat formatter;

    private GazzettaWrapper()
    {
        this.gazzette = new LinkedList<GazzettaItem>();
        this.formatter = new SimpleDateFormat("ddMMyyyy");
    }

    public static GazzettaWrapper getInstance()
    {
        if(instance == null)
        {
            instance = new GazzettaWrapper();
        }
        return instance;
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
    public boolean gazzettaExistsForDate(String day, String month, String year)
    {



        for(GazzettaItem g: gazzette)
        {
            System.out.println("\nIngresso:\t"+day+month+year);
            System.out.println("\nIn Storage: \t"+ g.getPublishDate());
            try {
                if (g.getPublishDate().equals(formatter.parse(day + month + year)))
                    return true;
            } catch (ParseException e) {
                return false;
            }
        }

        return false;
    }

    @JsonIgnore
    public DateFormat getFormatter()
    {
        return formatter;
    }

}