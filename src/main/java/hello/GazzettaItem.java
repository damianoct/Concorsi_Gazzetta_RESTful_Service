package hello;


/**
 * Created by damianodistefano on 16/05/15.
 */
public class GazzettaItem
{
    private final long idGazzetta;
    private final String numberOfPublication;
    private final String dateOfPublication;

    public GazzettaItem(long idGazzetta, String numberOfPublication, String dateOfPublication)
    {
        this.numberOfPublication = numberOfPublication;
        this.dateOfPublication = dateOfPublication;
        this.idGazzetta = idGazzetta;

    }

    public long getIdGazzetta()
    {
        return idGazzetta;
    }

    public String getDateOfPublication()
    {
        return dateOfPublication;
    }

    public String getNumberOfPublication()
    {
        return numberOfPublication;
    }

    public boolean isValid()
    {
        if(numberOfPublication != null
                &&
                dateOfPublication.split("/")[0] != null
                &&
                dateOfPublication.split("/")[1] != null
                &&
                dateOfPublication.split("/")[2] != null
                )
        {
            return true;
        }
        else
            return false;
    }
}
