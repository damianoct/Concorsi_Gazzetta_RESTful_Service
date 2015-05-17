package hello;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by damianodistefano on 18/05/15.
 */
public class ConcorsoWrapper
{
    private List<ConcorsoItem> concorsi;
    private static ConcorsoWrapper instance;

    private ConcorsoWrapper()
    {
        this.concorsi = new LinkedList<ConcorsoItem>();
    }

    public static ConcorsoWrapper getInstance()
    {
        if(instance == null)
        {
            instance = new ConcorsoWrapper();
        }
        return instance;
    }

    public void addConcorsiList(List<ConcorsoItem> concorsoItemList)
    {
        concorsi.addAll(concorsoItemList);
    }

    public List<ConcorsoItem> getConcorsi()
    {
        return concorsi;
    }
}
