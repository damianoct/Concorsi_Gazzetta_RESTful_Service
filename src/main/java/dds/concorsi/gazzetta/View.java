package dds.concorsi.gazzetta;

/**
 * Created by damianodistefano on 18/05/15.
 */
public class View {

    interface GazzetteSummary {}
    interface GazzetteWithContests extends GazzetteSummary {}
    interface SummaryWithRecipients extends GazzetteSummary {}

}