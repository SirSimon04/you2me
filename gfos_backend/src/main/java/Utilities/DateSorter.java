/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import java.util.Comparator;
import Entity.Chat;
/**
 * <h1>Die Klasse zum Sortieren der Chats</h1>
 * <p></p>
 */
/**
 *
 * @author simon
 */
public class DateSorter implements Comparator<Chat> {
    /**
     * Diese Methode vergleicht zwei Chats anhand des Datums der zuletzt gesendeten Nachricht. Sie kommt zum Einsatz, wenn die eigene CHatliste sortiert werden soll.
     * @param c1
     * @param c2
     * @return Int, welcher CHat zuerst kommt.
     */
    @Override
    public int compare(Chat c1, Chat c2){
        return c2.getLetztenachricht().getDatumuhrzeit().compareTo(c1.getLetztenachricht().getDatumuhrzeit());
    }
}
