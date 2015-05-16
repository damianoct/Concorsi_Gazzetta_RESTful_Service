package hello;

/**
 * Created by damianodistefano on 10/05/15.
 */
public class Concorso
{

    private final String emittente;
    private final String titoloConcorso;


    public Concorso (String emittente, String titoloConcorso)
    {
        this.emittente = emittente;
        this.titoloConcorso = titoloConcorso;
    }

    public String getEmittente()
    {
        return emittente;
    }

    public String getTitoloConcorso()
    {
        return titoloConcorso;
    }
}
