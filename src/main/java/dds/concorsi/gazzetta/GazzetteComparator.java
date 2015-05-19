package dds.concorsi.gazzetta;

import java.util.Comparator;

/**
 * Created by damianodistefano on 19/05/15.
 */
public class GazzetteComparator implements Comparator<GazzettaItem>
{
    @Override
    public int compare(GazzettaItem o1, GazzettaItem o2)
    {
        //Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the
        //first argument is less than, equal to, or greater than the second.

        //per avere l'ordine decrescente inverto tutto.

        if (o1.getPublishDate().compareTo(o2.getPublishDate()) < 0)
            return 1;
        else if (o1.getPublishDate().compareTo(o2.getPublishDate()) > 0)
            return -1;
        else
            return 0;
    }
}
