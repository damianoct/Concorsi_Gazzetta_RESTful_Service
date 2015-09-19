package dds.concorsi.gazzetta;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

/**
 * Created by damianodistefano on 10/05/15.
 */
public class ConcorsoItem
{

    private final long idConcorso;
    private final long idGazzetta;
    @JsonView(View.GazzetteWithContests.class)
    private final String emettitore;
    @JsonView(View.GazzetteWithContests.class)
    private final String areaDiInteresse;
    @JsonView(View.GazzetteWithContests.class)
    private final String titoloConcorso;
    @JsonView(View.GazzetteWithContests.class)
    private final String tipologia;
    @JsonView(View.GazzetteWithContests.class)
    private final String codiceRedazionale;
    private final List<String> articoliBando;
    @JsonView(View.GazzetteWithContests.class)
    private final int numeroArticoli;



    public ConcorsoItem(long idConcorso, long idGazzetta, String areaDiInteresse,
                        String emettitore, String tipologia, String titoloConcorso,
                        String codiceRedazionale, List<String> articoliBando)
    {
        this.idConcorso = idConcorso;
        this.idGazzetta = idGazzetta;
        this.areaDiInteresse = areaDiInteresse;
        this.emettitore = emettitore;
        this.tipologia = tipologia;
        this.titoloConcorso = titoloConcorso;
        this.codiceRedazionale = codiceRedazionale;
        this.articoliBando = articoliBando;
        this.numeroArticoli = this.articoliBando.size();
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

    public String getCodiceRedazionale()
    {
        return codiceRedazionale;
    }

    public int getNumeroArticoli()
    {
        return numeroArticoli;
    }

    public List<String> getArticoliBando()
    {
        return articoliBando;
    }
}
