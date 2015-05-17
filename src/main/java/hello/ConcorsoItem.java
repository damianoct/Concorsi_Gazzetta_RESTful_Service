package hello;

/**
 * Created by damianodistefano on 10/05/15.
 */
public class ConcorsoItem
{

    private final long idConcorso;
    private final long idGazzetta;
    private final String emettitore;
    private final String areaDiInteresse;
    private final String titoloConcorso;
    private final String tipologia;


    public ConcorsoItem(long idConcorso, long idGazzetta, String areaDiInteresse, String emettitore, String tipologia, String titoloConcorso)
    {
        this.idConcorso = idConcorso;
        this.idGazzetta = idGazzetta;
        this.areaDiInteresse = areaDiInteresse;
        this.emettitore = emettitore;
        this.tipologia = tipologia;
        this.titoloConcorso = titoloConcorso;
    }

    public long getIdConcorso()
    {
        return idConcorso;
    }

    public long getIdGazzetta()
    {
        return idGazzetta;
    }

    public String getEmettitore()
    {
        return emettitore;
    }

    public String getAreaDiInteresse()
    {
        return areaDiInteresse;
    }

    public String getTitoloConcorso()
    {
        return titoloConcorso;
    }

    public String getTipologia()
    {
        return tipologia;
    }
}
