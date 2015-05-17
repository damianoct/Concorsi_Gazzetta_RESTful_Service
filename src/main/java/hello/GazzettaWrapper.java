package hello;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by damianodistefano on 17/05/15.
 */
public class GazzettaWrapper
{
    private List<GazzettaItem> gazzette;
    private static GazzettaWrapper instance;

    private GazzettaWrapper()
    {
        this.gazzette = new LinkedList<GazzettaItem>();
    }

    public static GazzettaWrapper getInstance()
    {
        if(instance == null)
        {
            instance = new GazzettaWrapper();
        }
        return instance;
    }

    public void setGazzette(List<GazzettaItem> gazzettaItemList)
    {
        this.gazzette = gazzettaItemList;
    }

    public List<GazzettaItem> getGazzette()
    {
        return gazzette;
    }

}