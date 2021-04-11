/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.util.Comparator;
import Entity.Chat;

/**
 * <h1>Die Klasse zum Sortieren der Chats.</h1>
 * <p>
 * Diese Klasse ist für das sortieren der Chats in der eigenen Chatliste
 * vcerantwortlich.</p>
 */
public class NameSorter implements Comparator<Chat>{

    /**
     * Diese Methode vergleicht zwei Chats anhand des Datums der zuletzt
     * gesendeten Nachricht. Sie kommt zum Einsatz, wenn die eigene CHatliste
     * sortiert werden soll.
     *
     * @param c1 Chat 1
     * @param c2 Chat 2
     * @return Int, welcher CHat zuerst kommt.
     */
    @Override
    public int compare(Chat c1, Chat c2){
        return c1.getName().compareToIgnoreCase(c2.getName());
    }
}
