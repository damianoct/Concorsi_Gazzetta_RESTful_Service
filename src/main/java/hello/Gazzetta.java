package hello;

import java.util.Date;

/**
 * Created by damianodistefano on 16/05/15.
 */
public class Gazzetta
{
    private final int numberOfPublication;
    private final Date dateOfPublication;

    public Gazzetta(int numberOfPublication, Date dateOfPublication)
    {
        this.numberOfPublication = numberOfPublication;
        this.dateOfPublication = dateOfPublication;
    }

    public Date getDateOfPublication()
    {
        return dateOfPublication;
    }

    public int getNumberOfPublication()
    {
        return numberOfPublication;
    }
}
