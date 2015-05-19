package hello;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by damianodistefano on 16/05/15.
 */
public class GazzettaItem
{
    @JsonView(View.Summary.class)
    private final long idGazzetta;
    @JsonView(View.Summary.class)
    private final String numberOfPublication;
    @JsonView(View.Summary.class)
    private Date dateOfPublication = null;
    private List<ConcorsoItem> concorsi;

    @JsonIgnore
    private Calendar calendar = Calendar.getInstance();


    public GazzettaItem(long idGazzetta, String numberOfPublication, String dateOfPublication)
    {
        this.concorsi = new LinkedList<ConcorsoItem>();
        this.numberOfPublication = numberOfPublication;
        this.idGazzetta = idGazzetta;
        try
        {
            this.dateOfPublication = GazzettaWrapper.getInstance()
                                        .getFormatter()
                                        .parse(dateOfPublication);
            this.calendar.setTime(this.dateOfPublication);

        }
        catch (ParseException e1)
        {
            this.dateOfPublication = null;
        }


    }

    public long getIdGazzetta()
    {
        return idGazzetta;
    }

    public String getDateOfPublication()
    {
        return GazzettaWrapper.getInstance()
                        .getFormatter()
                        .format(dateOfPublication);
    }

    public String getNumberOfPublication()
    {
        return numberOfPublication;
    }

    public List<ConcorsoItem> getConcorsi()
    {
        return concorsi;
    }

    public ConcorsoItem getConcorsoByReferenceCode(String referenceCode)
    {
        for(ConcorsoItem c: concorsi)
            if(c.getCodiceRedazionale().equals(referenceCode))
                return c;

        return null;
    }

    @JsonIgnore
    public boolean isValid()
    {
        return (numberOfPublication != null && dateOfPublication != null) ? true : false;
    }

    @JsonIgnore
    public int getPublicationDay()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @JsonIgnore
    public int getPublicationMonth() //month 0 - 11
    {
        return calendar.get(Calendar.MONTH) + 1;
    }

    @JsonIgnore
    public int getPublicationYear()
    {
        return calendar.get(Calendar.YEAR);
    }

    @JsonIgnore
    public Date getPublishDate()
    {
        return dateOfPublication;
    }
}
